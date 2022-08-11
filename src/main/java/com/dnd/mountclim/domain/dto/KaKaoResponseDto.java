package com.dnd.mountclim.domain.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "카카오 API 정보", description = "주소, 전화번호, 위도, 경도 등 등을 가진 Dto Class")
public class KaKaoResponseDto {
	public List<Document> documents;
	public Meta meta;
	
	@Data
	public static class Document {
		@ApiModelProperty(value = "전체 지번 주소", example = "서울 용산구 이태원동 127-28")
		public String address_name;
		@ApiModelProperty(value = "중요 카테고리만 그룹핑한 카테고리 그룹 코드", example = "FD6")
		public String category_group_code;
		@ApiModelProperty(value = "중요 카테고리만 그룹핑한 카테고리 그룹명", example = "음식점")
		public String category_group_name;
		@ApiModelProperty(value = "카테고리 이름", example = "음식점 > 한식 > 육류,고기")
		public String category_name;
		@ApiModelProperty(value = "중심좌표까지의 거리 (단, x,y 파라미터를 준 경우에만 존재) (단위: 미터(m))", example = "842")
		public String distance;
		@ApiModelProperty(value = "장소 ID", example = "1376253571")
		public String id;
		@ApiModelProperty(value = "전화번호", example = "02-793-2268")
		public String phone;
		@ApiModelProperty(value = "장소명, 업체명", example = "로우앤슬로우")
		public String place_name;
		@ApiModelProperty(value = "장소 상세 페이지 URL", example = "http://place.map.kakao.com/1376253571")
		public String place_url;
		@ApiModelProperty(value = "전체 도로명 주소", example = "서울 용산구 보광로 126")
		public String road_address_name;
		@ApiModelProperty(value = "X 좌표 혹은 경도(longitude)", example = "126.99421849699539")
		public String x;
		@ApiModelProperty(value = "Y 좌표 혹은 위도(latitude)", example = "37.53401162895581")
		public String y;
		@ApiModelProperty(value = "결과 페이지 번호", example = "1~45 사이의 값 (기본값: 1)")
		public int page;
	}
	@Data 
	public static class Meta {
		@ApiModelProperty(value = "검색어에 검색된 문서 수", example = "0")
		public Integer total_count;
		@ApiModelProperty(value = "total_count 중 노출 가능 문서 수 (최대: 45)", example = "0")
		public Integer pageable_count;
		@ApiModelProperty(value = "현재 페이지가 마지막 페이지인지 여부. 값이 false면 다음 요청 시 page 값을 증가시켜 다음 페이지 요청 가능", example = "false")
		public Boolean is_end;
		@ApiModelProperty(value = "질의어의 지역 및 키워드 분석 정보")
		public List<SameName> same_name;
		
		@Data
		public static class SameName {
			@ApiModelProperty(value = "질의어에서 인식된 지역의 리스트", example = "'중앙로 맛집' 에서 중앙로에 해당하는 지역 리스트")
			public String[] region;
			@ApiModelProperty(value = "질의어에서 지역 정보를 제외한 키워드", example = "'중앙로 맛집' 에서 '맛집'")
			public String keyword;
			@ApiModelProperty(value = "인식된 지역 리스트 중, 현재 검색에 사용된 지역 정보")
			public String selected_region;
		}
	}
}
