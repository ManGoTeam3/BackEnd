package com.mango.service.responsedto;

import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
public class SearchResponseDto {

    private Long id;
    private String placeName;
    private String categoryName;
    private String imageUrl;
    private String address_name;
    private Double x;
    private Double y;
    private Double score;
}
