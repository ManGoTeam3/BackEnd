package com.mango.service.kakaoApiDto;

import com.mango.entity.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private Double x;
    private Double y;

    public Restaurant documentsToEntity() {
        return Restaurant.builder()
            .id(this.id)
            .x(this.x)
            .y(this.y)
            .build();
    }
}