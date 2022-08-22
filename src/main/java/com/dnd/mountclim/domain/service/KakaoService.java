package com.dnd.mountclim.domain.service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.dnd.mountclim.domain.dto.LocationPoint;
import com.dnd.mountclim.domain.dto.RectanglePoints;
import com.dnd.mountclim.domain.util.DeduplicationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dnd.mountclim.domain.dto.KakaoResponseDto;
import com.dnd.mountclim.domain.dto.KakaoResponseDto.Document;

@Service
@RequiredArgsConstructor
public class KakaoService {

	private final DistanceService distanceService;
	private final DinignCodeService dinignCodeService;
	private final NaverService naverService;
	
	@Value("${kakao.api.key}")
	private String KAKAO_APIKEY;	
	
	KakaoResponseDto newKakaoResponseDto;
	List<Document> newDocuments;

	public ResponseEntity<KakaoResponseDto> getWorldCupInfo(
		String food,
		List<RectanglePoints> listRectanglePoints,
		String latitude,
		String longitude,
		String radius,
		String round) throws Exception {
		RestTemplate restTemplate = new RestTemplate();			
		HttpHeaders headers = new HttpHeaders();
		KakaoResponseDto kakaoResponseDto = null;
		newKakaoResponseDto = new KakaoResponseDto();
		newDocuments = new ArrayList<>();
		try {
			HttpEntity<String> entity = new HttpEntity<String>(headers);

			// FIXME 16분할 했으므로, divisionRadius는 'divisionRadius는 = radius / 4;'로 탐색, 분할 개수 바뀌면 수정해야함.
			String divisionRadius = String.valueOf(Double.parseDouble(radius) / 4);

			int page = 1;

			for(RectanglePoints rp : listRectanglePoints) {
				for (LocationPoint lp : rp.getRectanglePoints()) {

					while (true) {
						headers.set("Authorization", "KakaoAK " + KAKAO_APIKEY);
						String apiURL = "https://dapi.kakao.com/v2/local/search/category.JSON?"
								+ "&category_group_code=" + "FD6"
								+ "&x=" + lp.getLongitude()
								+ "&y=" + lp.getLatitude()
								+ "&radius=" + divisionRadius
								+ "&page=" + page;

						ResponseEntity<KakaoResponseDto> responseEntity = restTemplate.exchange(apiURL, HttpMethod.GET, entity, KakaoResponseDto.class);
						kakaoResponseDto = responseEntity.getBody();

						if (kakaoResponseDto.meta.is_end) {
							this.foodClassification(kakaoResponseDto, food);
							break;
						} else {
							this.foodClassification(kakaoResponseDto, food);
							page++;
						}
					}
				}
			}

			// 중복제거
			List<Document> distinctNewDocuments = DeduplicationUtils.deduplication(newDocuments, Document::getId);
			// 거리 업데이트 // 분할된 지점으로부터 거리가 나오기 때문에, 원위치로부터 거리 다시 계산
			for(Document d : distinctNewDocuments){
				double distanceMeter = distanceService.distance(Double.parseDouble(d.getY()), Double.parseDouble(d.getX()), Double.parseDouble(latitude), Double.parseDouble(longitude), "meter");
				d.setDistance(String.valueOf(distanceMeter));
			}

			// 검색 개수와 거리를 이용해서 정렬
			// 검색개수 가중치 : 70%, 거리 가중치 : 30%
			float search_weight = 70;
			float distance_weight = 30;
			Map<Document, Float> map = new HashMap<>();
			for(Document d : distinctNewDocuments){
				float score;
				int total = naverService.naverApi(d.getPlace_name());
				double distance = Double.parseDouble(d.getDistance());

				if(total < 500)
					score =  1.0f / 3.0f * search_weight;
				else if(total < 1000)
					score = 2.0f / 3.0f * search_weight;
				else
					score = search_weight;

				if(distance < Double.parseDouble(radius) / 3.0d)
					score += distance_weight;
				else if(distance < Double.parseDouble(radius) * 2.0d / 3.0d)
					score += 2.0f / 3.0f * distance_weight;
				else
					score += 1.0f / 3.0f * distance_weight;

				map.put(d, score);
				TimeUnit.MILLISECONDS.sleep(50); //FIXME '429 Too Many Requests'에 대한 해결책
			}

			List<Document> keySetList = new ArrayList<>(map.keySet());

			Collections.sort(keySetList, (o1, o2) -> (map.get(o2).compareTo(map.get(o1))));

			if(keySetList.size() > Integer.parseInt(round)){
				keySetList = keySetList.subList(0, Integer.parseInt(round));
				
				dinignCodeService.driverInit();
				
				for(Document document : keySetList) {
					dinignCodeService.dinignCodeCrawling(document);
				}
				// 이미지 url이 없다면 제거
				keySetList = keySetList.stream().filter(x -> x.img_url != null).collect(Collectors.toList());
			}

			newKakaoResponseDto.setDocuments(keySetList);
			newKakaoResponseDto.setMeta(kakaoResponseDto.meta);
		} catch(Exception e) {
			throw e;
		} finally {
			dinignCodeService.driverQuit();
		}
		return new ResponseEntity<KakaoResponseDto>(newKakaoResponseDto, headers, HttpStatus.valueOf(200));
	}

	public void foodClassification(KakaoResponseDto kakaoResponseDto, String food) {
		List<Document> documents = kakaoResponseDto.documents;
		try {
			for(Document document : documents) {
				if(food.equals("국밥,감자탕")){
					if(document.category_name.contains("국밥") || document.category_name.contains("감자탕")){
						newDocuments.add(document);
					}
				}
				else if(food.equals("바")){
					if(document.category_name.contains("와인바") || document.category_name.contains("오뎅바")
							|| document.category_name.contains("칵테일바")){
						newDocuments.add(document);
					}
				}
				else if(food.equals("기타")){
					if(document.category_name.contains("실내포장마차") || document.category_name.contains("일본식주점")){
						newDocuments.add(document);
					}
				}
				else if(document.category_name.contains(food)){
					newDocuments.add(document);
				}
			}
		} catch(Exception e) {
			throw e;
		}
	}
}
