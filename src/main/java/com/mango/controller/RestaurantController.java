package com.mango.controller;

import com.mango.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
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
    public ResponseEntity searchRestaurant(@RequestParam String query){
        List restaurantList = restaurantService.searchRestaurant(query);
        return ResponseEntity.status(HttpStatus.OK).body(restaurantList);
    }

    @GetMapping("/detail")
    @Operation(summary = "식당 상세 정보 조회")
    public String getRestaurantDetail(@RequestParam long restaurantId) {
        restaurantService.getRestaurantDetail(restaurantId);
        return null;
    }
}