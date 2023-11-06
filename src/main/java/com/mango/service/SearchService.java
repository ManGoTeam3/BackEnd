package com.mango.service;

import com.mango.service.dto.SearchResponseDto;
import com.mango.service.kakaoApiDto.KakaoRestaurantApiResponseDto;
import com.mango.service.kakaoApiDto.AddressDocuments;
import com.mango.service.kakaoApiDto.KakaoAddressApiResponseDto;
import com.mango.service.kakaoApiDto.RestaurantDocuments;
import com.mango.service.kakaoApiDto.XYDto;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class SearchService {

    @Value("${kakao.key}")
    private String key;


    public List searchRestaurant(String query){
        XYDto xyDto = kakaoApiSearchXYByAddress(query); //주소 검색어의 xy좌표 반환
        List<RestaurantDocuments> restaurantDocuments = kakaoApiSearchRestaurantByXY(xyDto.getX(), xyDto.getY()); //xy좌표를 기준으로 반경 1km 맛집리스트(15개) 반환
        List<SearchResponseDto> resultList = restaurantDocumentsToSearchResponseDto(restaurantDocuments); //맛집 데이터 목록에 필요한 정보만 가공 후 반환

        return resultList;
    }

    public XYDto kakaoApiSearchXYByAddress(String queryAddress){
        String url = "https://dapi.kakao.com/v2/local/search/address.json";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "KakaoAK " + key);
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        URI targetUrl = UriComponentsBuilder
            .fromUriString(url)
            .queryParam("query", queryAddress)
            .queryParam("analyze_type", "similar")
            .build()
            .encode(StandardCharsets.UTF_8)
            .toUri();

        ResponseEntity<KakaoAddressApiResponseDto> result = restTemplate.exchange(targetUrl, HttpMethod.GET, httpEntity, KakaoAddressApiResponseDto.class);
        AddressDocuments searchResult = result.getBody().getDocuments().get(0);
        return XYDto.builder()
            .x(searchResult.getX())
            .y(searchResult.getY())
            .build();
    }

    //x경도(longitude) y위도(latitude)
    public List<RestaurantDocuments> kakaoApiSearchRestaurantByXY(double x, double y){
        String url = "https://dapi.kakao.com/v2/local/search/category.json";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "KakaoAK " + key);
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        URI targetUrl = UriComponentsBuilder
            .fromUriString(url)
            .queryParam("category_group_code", "FD6")
            .queryParam("x", x)
            .queryParam("y", y)
            .queryParam("radius", 1000)
            .build()
            .encode(StandardCharsets.UTF_8)
            .toUri();

        ResponseEntity<KakaoRestaurantApiResponseDto> result = restTemplate.exchange(targetUrl, HttpMethod.GET, httpEntity, KakaoRestaurantApiResponseDto.class);

        return result.getBody().getDocuments();
    }

    public List<SearchResponseDto> restaurantDocumentsToSearchResponseDto(List<RestaurantDocuments> documents){
        return documents.stream()
            .map(r -> SearchResponseDto.builder()
                .id(r.getId())
                .placeName(r.getPlace_name())
                .categoryName(splitCategoryName(r.getCategory_name()))
                .build())
            .collect(Collectors.toList());
    }

    public String splitCategoryName(String categoryName){
        String[] split = categoryName.split(">");
        return split[1].strip();
    }
}
