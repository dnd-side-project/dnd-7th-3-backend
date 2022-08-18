package com.dnd.mountclim.domain.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
public class KakaoService {
	
	@Autowired
	private DinignCodeService dinignCodeService;
	
	@Value("${kakao.api.key}")
	private String KAKAO_APIKEY;	
	
	KakaoResponseDto newKakaoResponseDto;
	List<Document> newDocuments;
	
	public ResponseEntity<KakaoResponseDto> kakaoApi(
		String food,
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
			
			int page = 1;
			while(true) {	
				headers.set("Authorization", "KakaoAK " + KAKAO_APIKEY);
				String apiURL = "https://dapi.kakao.com/v2/local/search/category.JSON?"
						+ "&category_group_code=" + "FD6"
						+ "&x=" + longitude
						+ "&y=" + latitude
						+ "&radius=" + radius
						+ "&page=" + page;
				
				ResponseEntity<KakaoResponseDto> responseEntity = restTemplate.exchange(apiURL, HttpMethod.GET, entity, KakaoResponseDto.class);
				kakaoResponseDto = responseEntity.getBody();	
				
				if(kakaoResponseDto.meta.is_end) {
					this.foodClassification(kakaoResponseDto, food); 
					break;
				} else {
					this.foodClassification(kakaoResponseDto, food); 
					page++;
				}
			}
			// 제일 많은 리뷰 순서대로 줄 세우고 round 갯수만큼 뽑아내기
			List<Document> orderByDocument = new ArrayList<>();
			if(newDocuments.size() > 0) {
				newDocuments = newDocuments.stream().filter(x -> x.review != null).sorted(Comparator.comparing(Document::getReview).reversed()).collect(Collectors.toList());					
				int max = 0;
				if(newDocuments.size() >= Integer.parseInt(round)) {
					max = Integer.parseInt(round);
				} else {
					max = newDocuments.size();
				}
				for(int i = 0; i < max; i++) {
					orderByDocument.add(newDocuments.get(i));
				}
			}
			newKakaoResponseDto.setDocuments(orderByDocument);
			newKakaoResponseDto.setMeta(kakaoResponseDto.meta);
		} catch(Exception e) {
			throw e;
		}
		return new ResponseEntity<KakaoResponseDto>(newKakaoResponseDto, headers, HttpStatus.valueOf(200));
	}
	
	public void foodClassification(KakaoResponseDto kakaoResponseDto, String food) throws Exception {
		List<Document> documents = kakaoResponseDto.documents;
		try {
//			for(Document document : documents) {
//				if(food.equals("국밥,감자탕")){
//					if(document.category_name.contains("국밥") || document.category_name.contains("감자탕")){
//						newDocuments.add(document);
//					}
//				}
//				else if(food.equals("바")){
//					if(document.category_name.contains("와인바") || document.category_name.contains("오뎅바")
//							|| document.category_name.contains("칵테일바")){
//						newDocuments.add(document);
//					}
//				}
//				else if(food.equals("기타")){
//					if(document.category_name.contains("실내포장마차") || document.category_name.contains("일본식주점")){
//						newDocuments.add(document);
//					}
//				}
//				else if(document.category_name.contains(food)){
//					newDocuments.add(document);
//				}
//			}
			int count = 1;
			for(Document document : documents) {
				String category_name = document.category_name.replace(" ", "").split(">")[1];

				if(category_name.equals(food)) {
					newDocuments.add(document);
					dinignCodeService.dinignCodeCrawling(document);
					System.err.println("갯수 : " + count);
					count++;
				}
				
				if(count == 16) {
					break;
				}
			}
			dinignCodeService.driverClose();
		} catch(Exception e) {
			throw e;
		}
	}
}
