package com.dnd.secondgo.domain.controller;

import com.dnd.secondgo.domain.dto.RectanglePoints;
import com.dnd.secondgo.domain.service.PointService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dnd.secondgo.domain.dto.KakaoResponseDto;
import com.dnd.secondgo.domain.service.KakaoService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/worldCup")
public class WorldCupController {

	private final KakaoService kakaoService;

	private final PointService pointService;
	
	@GetMapping
	@ApiOperation(value = "카카오 API 연동", notes = "카카오 API 를 통해 음식점 데이터를 가져온다.")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "food", value = "음식 종류", required = false, dataType = "string", paramType = "query", example = "국밥,감자탕|바|기타"),
		@ApiImplicitParam(name = "latitude", value = "위도", required = true, dataType = "string", paramType = "query", example = "37.49233331210365"),
		@ApiImplicitParam(name = "longitude", value = "경도", required = true, dataType = "string", paramType = "query", example = "126.72401065553541"),
		@ApiImplicitParam(name = "radius", value = "거리", required = true, dataType = "string", paramType = "query", example = "1000"),
		@ApiImplicitParam(name = "round", value = "대진표 갯수", required = true, dataType = "string", paramType = "query", example = "16")
	})
	public ResponseEntity<KakaoResponseDto> getWorldCupInfo(
		@RequestParam(name = "food", required = false) String food,
		@RequestParam(name = "latitude", required = true) String latitude,
		@RequestParam(name = "longitude", required = true) String longitude,
		@RequestParam(name = "radius", required = true) String radius,
		@RequestParam(name = "round", required = true) String round) throws Exception {

		RectanglePoints rectanglePoints = pointService.getRectanglePoints(Double.parseDouble(latitude), Double.parseDouble(longitude), Double.parseDouble(radius) * 0.001); // km 기준 입니다.
		List<RectanglePoints> listRectanglePoints = pointService.getRectanglePoints(rectanglePoints, Double.parseDouble(radius) * 0.001 / 2);

		// ***** 음식종류 처리 예시 *****
		// List<String> foodList = Arrays.asList(food.split("\\|"));
		// ***********************
		return kakaoService.getWorldCupInfo(food, listRectanglePoints, latitude, longitude, radius, round);
	}
}
