package com.mango.service.dto;

import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
public class SearchResponseDto {

    private Long id;
    private String placeName;
    private String categoryName;
    private String imageUrl;
}
