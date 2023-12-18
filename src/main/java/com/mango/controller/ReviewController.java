package com.mango.controller;

import com.mango.dto.ReviewDto;
import com.mango.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin("http://34.83.15.61:3000")
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
@Tag(name = "Review", description = "리뷰 관련 API")
public class ReviewController {

    private final ReviewService reviewService;
    @PostMapping(value = "/review/{restaurantId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "리뷰 등록",
            description = "form-data 로 보내야 함. " +
                    "ReviewDto : {'score' : 'true or false', 'reviewContents':'리뷰내용'} " +
                    "reviewPic : (이미지파일)")
    public ResponseEntity enrollReview(@PathVariable Long restaurantId, @RequestPart(required = false, value = "reviewPic") MultipartFile image,@RequestPart("ReviewDto") ReviewDto reviewDto, Authentication auth) throws IOException {
        String userName = auth.getName();
        reviewDto.setReviewPic(image);
        return reviewService.enrollReview(restaurantId,reviewDto,userName);
    }
    @GetMapping("/review/{restaurantId}")
    @Operation(summary = "리뷰 전체 조회",
            description = "리뷰 조회")
    public ResponseEntity getReview(@PathVariable Long restaurantId) {
        return reviewService.getReview(restaurantId);
    }

    @GetMapping("/reviewDetail/{reviewId}")
    @Operation(summary = "리뷰 상세 조회",
            description = "리뷰 상세 조회")
    public ResponseEntity getReviewDetail(@PathVariable Long reviewId) {
        return reviewService.getReviewDetail(reviewId);
    }
    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity deleteReview(@PathVariable Long reviewId) {
        return reviewService.deleteReview(reviewId);
    }
}
