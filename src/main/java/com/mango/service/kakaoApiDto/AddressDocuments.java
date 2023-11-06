package com.mango.service.kakaoApiDto;

import java.util.HashMap;
import lombok.Getter;

@Getter
public class AddressDocuments {

    private HashMap<String, Object> address;
    private String address_type;
    public Double x;
    public Double y;
    private String address_name;
    private HashMap<String, Object> road_address;
}