package com.mango.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("http://3.217.20.163:3000")
@RequiredArgsConstructor
@Tag(name = "ReviewLike", description = "리뷰 좋아요 관련 API")
public class ReviewLikeController {

}
