package com.dnd.mountclim.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dnd.mountclim.domain.dto.KaKaoResponseDto;
import com.dnd.mountclim.domain.dto.KaKaoResponseDto.Document;

@Service
public class HaeyongService {

	private final String KAKAO_APIKEY = "";	
	
	KaKaoResponseDto newKakaoResponseDto;
	List<Document> newDocuments;
	
	public ResponseEntity<KaKaoResponseDto> kakaoApi(
		String food,
		String latitude,
		String longitude,
		String radius) throws Exception {
		RestTemplate restTemplate = new RestTemplate();			
		HttpHeaders headers = new HttpHeaders();
		KaKaoResponseDto kakaoResponseDto = null;
		newKakaoResponseDto = new KaKaoResponseDto();
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
				
				ResponseEntity<KaKaoResponseDto> responseEntity = restTemplate.exchange(apiURL, HttpMethod.GET, entity, KaKaoResponseDto.class);
				kakaoResponseDto = responseEntity.getBody();	
				
				if(kakaoResponseDto.meta.is_end) {
					this.foodClassification(kakaoResponseDto, food); 
					break;
				} else {
					this.foodClassification(kakaoResponseDto, food); 
					page++;
				}
			}
			newKakaoResponseDto.setDocuments(newDocuments);
			newKakaoResponseDto.setMeta(kakaoResponseDto.meta);
		} catch(Exception e) {
			throw e;
		}
		return new ResponseEntity<KaKaoResponseDto>(newKakaoResponseDto, headers, HttpStatus.valueOf(200));
	}
	
	public void foodClassification(KaKaoResponseDto kakaoResponseDto, String food) throws Exception {
		List<Document> documents = kakaoResponseDto.documents;
		try {
			for(Document document : documents) {
				String category_name = document.category_name.replace(" ", "").split(">")[1];
				
				if(category_name.equals(food)) {
					newDocuments.add(document);
				}
			}
		} catch(Exception e) {
			throw e;
		}
	}
	
	public void placeUrlCrawling() {
		
	}
}
