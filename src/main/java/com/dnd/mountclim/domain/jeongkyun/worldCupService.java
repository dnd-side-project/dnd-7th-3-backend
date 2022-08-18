package com.dnd.mountclim.domain.jeongkyun;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class worldCupService {

    @Value("${kakao.api.key}")
    private String KAKAO_APIKEY;

    private final CrawlingService crawlingService;
    private final String category_group_code = "FD6"; //음식점

    private RestTemplate restTemplate;
    private HttpHeaders httpHeaders;
    private HttpEntity<String> httpEntity;

    /* 카테고리
        1. 국밥, 감자탕
        2. 육류, 고기
        3. 해물, 생선
        4. 찌개, 전골
        5. 양꼬치
        6. 치킨
        7. 호프, 요리주점
        8. 실내포장마차
        9. 일본식주점
        10. 바
    */
    public ResponseEntity<?> worldCupSearch(
            String latitude,
            String longitude,
            String radius,
            int round,
            String category
    ) {
        init();

        int page = 1;
        httpHeaders.set("Authorization", "KakaoAK " + KAKAO_APIKEY);
        String apiURL = "https://dapi.kakao.com/v2/local/search/category.JSON?"
                + "&category_group_code=" + category_group_code
                + "&x=" + longitude
                + "&y=" + latitude
                + "&radius=" + radius
                + "&page=" + page;

        ResponseEntity<KaKaoSearchResponseDto> responseEntity
                        = restTemplate.exchange(apiURL, HttpMethod.GET, httpEntity, KaKaoSearchResponseDto.class);

        KaKaoSearchResponseDto kakaoSearchResponseDto =
                        KaKaoSearchResponseDto
                        .builder()
                        .documents(responseEntity.getBody().getDocuments())
                        .meta(responseEntity.getBody().getMeta())
                        .build();

        List<WorldCupDto> worldCupDtoList = new ArrayList<>();
        List<KaKaoSearchResponseDto.Document> documents
                = kakaoSearchResponseDto.getDocuments();

        int resultCnt = documents.size();
        for (int i = 0; i < resultCnt; i++) {
            worldCupDtoList.add(
                    WorldCupDto
                            .builder()
                            .placeName(documents.get(i).getPlaceName())
                            .distance(documents.get(i).getDistance())
                            .phone(documents.get(i).getPhone())
                            .addressName(documents.get(i).getAddressName())
                            .build()
            );
        }

//        crawlingService.naverPlaceCrawling(documents);
        close();
        return ResponseEntity.ok(worldCupDtoList);
    }

    private void init(){
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();
        httpEntity = new HttpEntity<>(httpHeaders);
    }

    private void close(){
        restTemplate = null;
        httpHeaders = null;
        httpEntity = null;
    }
}