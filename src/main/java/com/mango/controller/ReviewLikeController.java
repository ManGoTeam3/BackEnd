package com.mango.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("http://34.83.15.61:3000")
@RequiredArgsConstructor
@Tag(name = "ReviewLike", description = "리뷰 좋아요 관련 API")
public class ReviewLikeController {

}
