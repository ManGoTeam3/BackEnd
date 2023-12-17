package com.mango.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor

public class GetAllReviewDto {
    private Long reviewId;
    private Long restaurantId;
    private int score;
    private String reviewContents;
    private List<String> reviewPicUrl;
}
