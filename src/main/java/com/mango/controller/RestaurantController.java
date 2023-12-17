package com.mango.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://3.217.20.163:3000")
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
@Tag(name = "Restaurant", description = "식당 관련 API")
public class RestaurantController {
    @PostMapping("/register")
    @Operation(summary = "식당 등록")
    public String enrollRestaurant() {
        return null;
    }

    @GetMapping("/detail")
    @Operation(summary = "식당 상세 정보 조회")
    public String getRestaurantDetail() {
        return null;
    }
}

