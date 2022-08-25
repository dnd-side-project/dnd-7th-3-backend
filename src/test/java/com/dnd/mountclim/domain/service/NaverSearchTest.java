package com.dnd.mountclim.domain.service;


import com.dnd.mountclim.domain.util.DateParsing;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class NaverSearchTest {
    @Value("${naver.api.id.key}")
    private String clientId;

    @Value("${naver.api.secret.key}")
    private String clientSecret;

    @Test
    public void naverSearchTest(){

        String apiUrl = "https://openapi.naver.com/v1/datalab/search";
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        requestHeaders.put("Content-Type", "application/json");

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("startDate", DateParsing.weekAgoDateToyyyyMMdd());
        jsonObject.put("endDate", DateParsing.nowDateToyyyyMMdd());
        jsonObject.put("timeUnit", "date");

        JSONArray keywordsDataArr = new JSONArray();
        keywordsDataArr.add("은행골");

        //반복
        JSONObject keywordGroupsSubData = new JSONObject();
        keywordGroupsSubData.put("groupName", "은행골");
        keywordGroupsSubData.put("keywords", keywordsDataArr);

        JSONArray keywordGroupsSubDataArr = new JSONArray();
        keywordGroupsSubDataArr.add(keywordGroupsSubData);

        jsonObject.put("keywordGroups",keywordGroupsSubDataArr);

        System.out.println(jsonObject.toJSONString());

        String requestBody = "{\"startDate\":\""+ DateParsing.weekAgoDateToyyyyMMdd()+ "\"," +
                "\"endDate\":\"" + DateParsing.nowDateToyyyyMMdd() + "\"," +
                "\"timeUnit\":\"date\"," +
                "\"keywordGroups\":[{\"groupName\":\"은행골\"," + "\"keywords\":[\"은행골\"]}," +
                "{\"groupName\":\"청진동해장국\"," + "\"keywords\":[\"청진동해장국\"]}]}";
        System.out.println(requestBody);

        String responseBody = post(apiUrl, requestHeaders, jsonObject.toJSONString());
        System.out.println(responseBody);
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
