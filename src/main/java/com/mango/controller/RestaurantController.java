package com.mango.controller;

import com.mango.service.RestaurantService;
import com.mango.service.responsedto.DetailResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://3.217.20.163:3000")
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
@Tag(name = "Restaurant", description = "식당 관련 API")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping("/search")
    @Operation(summary = "식당 검색")
    public ResponseEntity searchRestaurant(
        @Parameter(name = "query", description = "검색어", in = ParameterIn.QUERY) @RequestParam String query) {
        List restaurantList = restaurantService.searchRestaurant(query);
        return ResponseEntity.status(HttpStatus.OK).body(restaurantList);
    }

    @GetMapping("/detail")
    @Operation(summary = "식당 상세 정보 조회")
    public ResponseEntity getRestaurantDetail(
        @Parameter(name = "restaurantId", description = "식당 ID", in = ParameterIn.QUERY) @RequestParam long restaurantId) {
        DetailResponseDto restaurantDetail = restaurantService.getRestaurantDetail(restaurantId);

        if (restaurantDetail == null) {
            return ResponseEntity.status(500).body("no information");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantDetail);
        }
    }
}