package com.mango.service.kakaoApiDto;

import lombok.Getter;

@Getter
public class RestaurantDocuments {

    private String address_name;
    private String category_group_code;
    private String category_group_name;
    private String category_name;
    private Long distance;
    private Long id;
    private String phone;
    private String place_name;
    private String place_url;
    private String road_address_name;
    public Double x;
    public Double y;
}