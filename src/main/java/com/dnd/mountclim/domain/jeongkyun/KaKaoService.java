package com.dnd.mountclim.domain.jeongkyun;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KaKaoService {

    private final String KAKAO_APIKEY;
    private final String category_group_code = "FD6";

    public ResponseEntity<?> categorySearch(
            String food,
            String latitude,
            String longitude,
            String radius
    ) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);


        int page = 1;
        httpHeaders.set("Authorization", "KakaoAK " + KAKAO_APIKEY);
        String apiURL = "https://dapi.kakao.com/v2/local/search/category.JSON?"
                + "&category_group_code=" + category_group_code
                + "&x=" + longitude
                + "&y=" + latitude
                + "&radius=" + radius
                + "&page=" + page;

                ResponseEntity<KaKaoSearchResponseDto> responseEntity
                        = restTemplate.exchange(apiURL, HttpMethod.GET, entity, KaKaoSearchResponseDto.class);

        KaKaoSearchResponseDto kakaoSearchResponseDto =
                        KaKaoSearchResponseDto
                        .builder()
                        .documents(responseEntity.getBody().getDocuments())
                        .meta(responseEntity.getBody().getMeta())
                        .build();

        return ResponseEntity.ok(kakaoSearchResponseDto);

    }
}