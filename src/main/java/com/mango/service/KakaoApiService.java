package com.mango.service;

import com.mango.service.kakaoApiDto.KakaoApiResponseDto;
import java.net.URI;
import java.nio.charset.StandardCharsets;
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
public class KakaoApiService {

    private final AsyncRestaurantService restaurantService;

    @Value("${kakao.key}")
    private String key;


    public KakaoApiResponseDto kakaoApiSearchRestaurant(String query, int page) {
        String url = "https://dapi.kakao.com/v2/local/search/keyword.json";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "KakaoAK " + key);
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        URI targetUrl = UriComponentsBuilder.fromUriString(url)
            .queryParam("category_group_code", "FD6").queryParam("query", query)
            .queryParam("radius", 1000).queryParam("page", page).build()
            .encode(StandardCharsets.UTF_8).toUri();

        ResponseEntity<KakaoApiResponseDto> result = restTemplate.exchange(targetUrl,
            HttpMethod.GET, httpEntity, KakaoApiResponseDto.class);

        //비동기 작업
        restaurantService.saveRestaurantToDB(result.getBody().getDocuments());

        return result.getBody();
    }

    public KakaoApiResponseDto kakaoApiSearchRestaurantByXY(double x, double y) {
        String url = "https://dapi.kakao.com/v2/local/search/category.json";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "KakaoAK " + key);
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        URI targetUrl = UriComponentsBuilder.fromUriString(url)
            .queryParam("category_group_code", "FD6").queryParam("x", x).queryParam("y", y)
            .queryParam("radius", 0.1).build().encode(StandardCharsets.UTF_8).toUri();

        ResponseEntity<KakaoApiResponseDto> result = restTemplate.exchange(targetUrl,
            HttpMethod.GET, httpEntity, KakaoApiResponseDto.class);

        return result.getBody();
    }
}