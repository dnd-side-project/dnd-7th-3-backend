package com.dnd.secondgo.domain.util;

import com.dnd.secondgo.domain.dto.KakaoResponseDto;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

public class JsonParserUtils {

    public static String StringToJsonInWorldCupInfos(List<KakaoResponseDto.Document> documents, int idx){
        JSONObject reqBodyJsonObj = new JSONObject();
        reqBodyJsonObj.put("startDate", DateParsingUtils.weekAgoDateToyyyyMMdd());
        reqBodyJsonObj.put("endDate", DateParsingUtils.nowDateToyyyyMMdd());
        reqBodyJsonObj.put("timeUnit", "date");

        JSONArray keywordsDataArr = new JSONArray();
        JSONObject keywordGroupsSubData = new JSONObject();
        JSONArray keywordGroupsSubDataArr = new JSONArray();
        for (int i = 0; i < documents.size(); i++) {

        }

        for(KakaoResponseDto.Document doc : documents){

            keywordsDataArr = new JSONArray();
            keywordsDataArr.add(doc.place_name);

            keywordGroupsSubData = new JSONObject();
            keywordGroupsSubData.put("groupName", doc.place_name);
            keywordGroupsSubData.put("keywords", keywordsDataArr);
            keywordGroupsSubDataArr.add(keywordGroupsSubData);

        }

        reqBodyJsonObj.put("keywordGroups",keywordGroupsSubDataArr);

        return reqBodyJsonObj.toJSONString();
    }

    public static List<KakaoResponseDto.Document> setRatioAverageValueToDocument(List<KakaoResponseDto.Document> documents, String resJsonVal) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(resJsonVal);
        JSONArray resultArr = (JSONArray) jsonObject.get("results");

        for (int i = 0; i < resultArr.size(); i++) {
            JSONObject resultObject = (JSONObject) resultArr.get(i);
            JSONArray dataArray = (JSONArray) resultObject.get("data");

            if (dataArray.size() > 0) {
                float ratioAverage = 0.0f;
                for (int j = 0; j < dataArray.size(); j++) {
                    JSONObject  dataObj = (JSONObject) dataArray.get(i);
                    ratioAverage += Float.parseFloat(dataObj.get("ratio").toString());
                }
                //FIXME float으로 내릴 시 "NaN" 값이 들어가는 부분이 있어 일단 int형으로 파싱
                documents.get(i).setRatioAverage((int) ratioAverage / dataArray.size());
            }else
                documents.get(i).setRatioAverage(0);
        }

        return documents;
    }
}
