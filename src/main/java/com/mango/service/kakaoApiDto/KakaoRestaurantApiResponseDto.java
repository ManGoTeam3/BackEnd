package com.mango.service.kakaoApiDto;

import java.util.HashMap;
import java.util.List;
import lombok.Getter;

@Getter
public class KakaoRestaurantApiResponseDto {
    private Meta meta;
    private List<RestaurantDocuments> documents;
}

