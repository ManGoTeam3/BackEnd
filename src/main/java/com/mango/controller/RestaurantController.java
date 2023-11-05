package com.mango.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "Restaurant", description = "식당 관련 API")
public class RestaurantController {
    @PostMapping("/api/restaurant/register")
    public String enrollRestaurant() {
        return null;
    }

    @GetMapping("api/restaurant/detail")
    public String getRestaurantDetail() {
        return null;
    }
}

