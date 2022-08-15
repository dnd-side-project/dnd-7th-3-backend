package com.dnd.mountclim.domain.jeongkyun;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/worldCup")
public class WorldCupController {

    private final worldCupService worldCupService;

    @GetMapping
    @ApiOperation(value = "월드컵 데이터 조회", notes = "카카오 API 를 통해 월드컵 데이터를 가져온다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "latitude", value = "위도", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "longitude", value = "경도", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "radius", value = "거리", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "category", value = "음식 종류", required = false, dataType = "string", paramType = "query")
    })
    public ResponseEntity<?> worldCupSearch(
            @RequestParam(name = "latitude", required = true) String latitude,
            @RequestParam(name = "longitude", required = true) String longitude,
            @RequestParam(name = "radius", required = true) String radius,
            @RequestParam(name = "category", required = false) String category) {

        return worldCupService.worldCupSearch(latitude, longitude, radius, category);
    }
}
