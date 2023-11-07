package com.mango.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("http://34.83.15.61:3000")
@RequiredArgsConstructor
@Tag(name = "Restaurant", description = "식당 관련 API")
public class RestaurantController {
    @PostMapping("/api/restaurant/register")
    @Operation(summary = "식당 등록")
    public String enrollRestaurant() {
        return null;
    }

    @GetMapping("api/restaurant/detail")
    @Operation(summary = "식당 상세 정보 조회")
    public String getRestaurantDetail() {
        return null;
    }
}

