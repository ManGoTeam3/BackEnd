package com.mango.service.kakaoApiDto;

import lombok.Getter;


@Getter
public class Meta {

    private int total_count;
    private int pageable_count;
    private String is_end;
    private Object same_name;
}
