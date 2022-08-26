package com.dnd.secondgo.domain.service;

import com.dnd.secondgo.domain.dto.KakaoResponseDto;
import com.dnd.secondgo.domain.dto.NaverResponseDto;
import com.dnd.secondgo.domain.util.DateParsingUtils;
import com.dnd.secondgo.domain.util.JsonParserUtils;
import lombok.NoArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NaverService {

    @Value("${naver.api.id.key}")
    private String NAVER_API_IDKEY;

    @Value("${naver.api.secret.key}")
    private String NAVER_API_SECRETKEY;

    public int naverPlaceSearchApi(String place_name){
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

    public List<KakaoResponseDto.Document> naverPlaceSearchKeywordsRatioInfo(List<KakaoResponseDto.Document> documents) throws ParseException {
        String apiUrl = "https://openapi.naver.com/v1/datalab/search";
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", NAVER_API_IDKEY);
        requestHeaders.put("X-Naver-Client-Secret", NAVER_API_SECRETKEY);
        requestHeaders.put("Content-Type", "application/json");

        String responseBody = "";
        for (int i = 0; i < documents.size(); i++) {
            if(i % 5 == 0){
                String reqBody = JsonParserUtils.StringToJsonInWorldCupInfos(documents, i);
                System.out.println("reqBody : " + reqBody);
                responseBody = post(apiUrl, requestHeaders, reqBody);
                System.out.println("responseBody : " + responseBody);
            }
        }

        return JsonParserUtils.setRatioAverageValueToDocument(documents, responseBody);
    }

    private static String post(String apiUrl, Map<String, String> requestHeaders, String requestBody) {
        HttpURLConnection con = connect(apiUrl);

        try {
            con.setRequestMethod("POST");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(requestBody.getBytes());
                wr.flush();
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 응답
                return readBody(con.getInputStream());
            } else {  // 에러 응답
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect(); // Connection을 재활용할 필요가 없는 프로세스일 경우
        }
    }

    private static HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body, StandardCharsets.UTF_8);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }
}
