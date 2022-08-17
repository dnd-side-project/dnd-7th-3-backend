package com.dnd.mountclim.domain.controller;

import com.dnd.mountclim.domain.dto.RectanglePoints;
import com.dnd.mountclim.domain.service.PointService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dnd.mountclim.domain.dto.KakaoResponseDto;
import com.dnd.mountclim.domain.service.KakaoService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kakao")
public class KakaoController {

	private final KakaoService haeyongService;

	private final PointService pointService;
	
	@GetMapping("/kakao-api")
	@ApiOperation(value = "카카오 API 연동", notes = "카카오 API 를 통해 음식점 데이터를 가져온다.")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "food", value = "음식 종류", required = false, dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "latitude", value = "위도", required = true, dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "longitude", value = "경도", required = true, dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "radius", value = "거리", required = true, dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "round", value = "대진표 갯수", required = true, dataType = "string", paramType = "query")
	})
	public ResponseEntity<KakaoResponseDto> kakaoApi(
		@RequestParam(name = "food", required = false) String food,
		@RequestParam(name = "latitude", required = true) String latitude,
		@RequestParam(name = "longitude", required = true) String longitude,
		@RequestParam(name = "radius", required = true) String radius,
		@RequestParam(name = "round", required = true) String round) throws Exception {

		RectanglePoints rectanglePoints = pointService.getRectanglePoints(Double.parseDouble(latitude), Double.parseDouble(longitude), Double.parseDouble(radius) * 0.001); // km 기준 입니다.

//		System.out.println("=== test1 ===");
//		for (LocationPoint lp : rectanglePoints.getRectanglePoints()){
//			System.out.println("lat : " + lp.getLatitude() + ", lng : " + lp.getLongitude());
//		}
		List<RectanglePoints> listRectanglePoints = pointService.getRectanglePoints(rectanglePoints, Double.parseDouble(radius) * 0.001 / 2);

//		System.out.println("=== test ===");
//		for(RectanglePoints rp : listRectanglePoints){
//			for(LocationPoint lp : rp.getRectanglePoints()){
//				System.out.println("lat : " + lp.getLatitude() + ", lng : " + lp.getLongitude());
//			}
//		}
		// FIXME 16분할 했으므로, radius는 'radius = radius / 4;'로 탐색, 분할 개수 바뀌면 수정해야함.
		radius = String.valueOf(Double.parseDouble(radius) / 4);
		return haeyongService.kakaoApi(food, listRectanglePoints, radius, round);
	}
}
