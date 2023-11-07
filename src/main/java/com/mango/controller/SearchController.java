package com.mango.controller;

import com.mango.service.SearchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("http://34.83.15.61:3000")
@RequiredArgsConstructor
@Tag(name = "search", description = "서치 좋아요 관련 API")
public class SearchController{

    private final SearchService searchService;

    @GetMapping("/api/search")
    public ResponseEntity searchRestaurant(@RequestParam String query){
        System.out.println("working done");
        List restaurantList = searchService.searchRestaurant(query);
        return ResponseEntity.status(HttpStatus.OK).body(restaurantList);
    }
}
