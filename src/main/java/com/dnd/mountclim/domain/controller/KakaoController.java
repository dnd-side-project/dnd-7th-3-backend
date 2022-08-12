package com.dnd.mountclim.domain.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dnd.mountclim.domain.dto.KakaoResponseDto;
import com.dnd.mountclim.domain.service.KakaoService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kakao")
public class KakaoController {

	private final KakaoService haeyongService;
	
	@PostMapping("/kakao-api")
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
		return haeyongService.kakaoApi(food, latitude, longitude, radius, round);
	}
}
