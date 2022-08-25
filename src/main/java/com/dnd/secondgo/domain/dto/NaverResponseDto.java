package com.dnd.secondgo.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "네이버 API 정보", description = "검색 개수, 제목, link등을 가진 Dto Class")
public class NaverResponseDto {

    @ApiModelProperty(value = "검색 결과를 생성한 시간", example = "Fri, 19 Aug 2022 19:53:22 +0900")
    public String lastBuildDate;

    @ApiModelProperty(value = "검색 결과 문서의 총 개수", example = "4326")
    public int total;

    @ApiModelProperty(value = "문서의 시작점", example = "1")
    public int start;

    @ApiModelProperty(value = "검색된 검색 결과의 개수", example = "10")
    public int display;


    public List<Item> items;

    @Data
    public static class Item {
        @ApiModelProperty(value = "문서의 제목", example = "수원역삼겹살 고기 구워주는 <b>마장동김씨</b> 수원역점")
        public String title;
        @ApiModelProperty(value = "문서의 하이퍼텍스트 link", example = "https://blog.naver.com/kurohana?Redirect=Log&logNo=222842437086")
        public String link;
        @ApiModelProperty(value = "문서의 내용을 요약한 페시지 정보", example = "수원역삼겹살 <b>마장동 김씨</b>는 약 10년간의 요식업 경험을 토대로 수많은 연구와 노력 끝에 만들어진... 저희는 2인 추천 마장동 모둠 소를 주문했습니다. 모둠 소 메뉴는 통삼겹살 * 눈꽃목살 400g + 칼집 껍데기... ")
        public String description;
        @ApiModelProperty(value = "블로거 이름", example = "클로하 HOME : Daily Log")
        public String bloggername;
        @ApiModelProperty(value = "블로거의 하이퍼텍스트 link", example = "https://blog.naver.com/kurohana")
        public String bloggerlink;
        @ApiModelProperty(value = "블로그 포스트를 작성한 날짜", example = "20220808")
        public String postdata;

    }

}
