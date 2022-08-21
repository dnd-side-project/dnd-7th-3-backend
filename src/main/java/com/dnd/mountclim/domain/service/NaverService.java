package com.dnd.mountclim.domain.service;

import com.dnd.mountclim.domain.dto.NaverResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NaverService {

    @Value("${naver.client.id.key}")
    private String NAVER_API_IDKEY;

    @Value("${naver.client.secret.key}")
    private String NAVER_API_SECRETKEY;

    public int naverApi(String place_name){
        NaverResponseDto NaverResponseDto;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        try{
            HttpEntity<String> entity = new HttpEntity<>(headers);


            headers.set("X-Naver-Client-Id", NAVER_API_IDKEY);
            headers.set("X-Naver-Client-Secret", NAVER_API_SECRETKEY);
            String apiURL = "https://openapi.naver.com/v1/search/blog?"
                    + "query=" + place_name;

            ResponseEntity<NaverResponseDto> responseEntity = restTemplate.exchange(apiURL, HttpMethod.GET, entity, NaverResponseDto.class);
            NaverResponseDto = responseEntity.getBody();

            return NaverResponseDto.getTotal();


        }catch (Exception e){
            throw e;
        }
    }
}
