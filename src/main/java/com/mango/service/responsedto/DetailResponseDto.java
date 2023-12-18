package com.mango.service.responsedto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DetailResponseDto {

    private String addressName;
    private String categoryName;
    private String phone;
    private String placeName;
    private String placeUrl;
    private String roadAddressName;
    private Double x;
    private Double y;
}
