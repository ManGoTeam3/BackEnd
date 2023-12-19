package com.mango.service.kakaoApiDto;

import java.util.List;
import lombok.Getter;

@Getter
public class KakaoApiResponseDto {
    private Meta meta;
    private List<RestaurantDocuments> documents;
}

