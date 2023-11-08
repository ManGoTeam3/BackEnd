package com.mango.service;

import com.mango.repository.RestaurantRepository;
import com.mango.service.dto.SearchResponseDto;
import com.mango.service.kakaoApiDto.KakaoRestaurantApiResponseDto;
import com.mango.service.kakaoApiDto.AddressDocuments;
import com.mango.service.kakaoApiDto.KakaoAddressApiResponseDto;
import com.mango.service.kakaoApiDto.RestaurantDocuments;
import com.mango.service.kakaoApiDto.XYDto;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

    private final RestaurantRepository restaurantRepository;

    @Value("${kakao.key}")
    private String key;


    public List searchRestaurant(String query) {
        List<RestaurantDocuments> tempList = new ArrayList<>();

        XYDto xyDto = kakaoApiSearchXYByAddress(query); //주소 검색어의 xy좌표 반환
        kakaoApiSearchRestaurantMax(tempList, xyDto);
        List<SearchResponseDto> resultList = restaurantDocumentsToSearchResponseDto(
            tempList); //맛집 데이터 목록에 필요한 정보만 가공 후 반환

        return resultList;
    }

    public XYDto kakaoApiSearchXYByAddress(String queryAddress) {
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

        ResponseEntity<KakaoAddressApiResponseDto> result = restTemplate.exchange(targetUrl,
            HttpMethod.GET, httpEntity, KakaoAddressApiResponseDto.class);
        AddressDocuments searchResult = result.getBody().getDocuments().get(0);
        return XYDto.builder()
            .x(searchResult.getX())
            .y(searchResult.getY())
            .build();
    }

    //x경도(longitude) y위도(latitude)
    public KakaoRestaurantApiResponseDto kakaoApiSearchRestaurantByXY(double x, double y,
        int page) {
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
            .queryParam("page", page)
            .build()
            .encode(StandardCharsets.UTF_8)
            .toUri();

        ResponseEntity<KakaoRestaurantApiResponseDto> result = restTemplate.exchange(targetUrl,
            HttpMethod.GET, httpEntity, KakaoRestaurantApiResponseDto.class);

//        saveRestaurantToDB(result.getBody().getDocuments());

        return result.getBody();
    }

    public List<RestaurantDocuments> kakaoApiSearchRestaurantMax(List<RestaurantDocuments> tempList,
        XYDto xyDto) {

        int page = 1;

        while (true) { //최대 갯수 조회. 카카오 api의 최대는 3페이지까지(45개)
            KakaoRestaurantApiResponseDto kakaoRestaurantApiResponseDto = kakaoApiSearchRestaurantByXY(
                xyDto.getX(), xyDto.getY(), page);
            tempList.addAll(kakaoRestaurantApiResponseDto.getDocuments());
            if (kakaoRestaurantApiResponseDto.getMeta().getIs_end().equals("true") || page == 3) {
                break;
            }
            page++;
        }

        return tempList;
    }

    public List<SearchResponseDto> restaurantDocumentsToSearchResponseDto(
        List<RestaurantDocuments> documents) {
        return documents.stream()
            .map(r -> SearchResponseDto.builder()
                .id(r.getId())
                .placeName(r.getPlace_name())
                .categoryName(splitCategoryName(r.getCategory_name()))
                .address_name(r.getAddress_name())
                .x(r.getX())
                .y(r.getY())
                .build())
            .collect(Collectors.toList());
    }

  /*  //추후에 하나씩 추가할 일이 있을 경우 이 메서드 restaurant 하나만 받고
    //위 kakaoapixy메서드에서 반복문으로 리스트 안 restaurant에 하나하나씩 호출
    @Async //이거 db다 저장하려니 속도 성능문제가 너무 커서 비동기로 해야할 수도
    public void saveRestaurantToDB(List<RestaurantDocuments> restaurantDocuments) {

        for (RestaurantDocuments documents : restaurantDocuments) {
            Optional<Restaurant> byId = restaurantRepository.findById(documents.getId());
            if (!byId.isPresent()) {
                restaurantRepository.save(documents.documentsToEntity());
            }
        }
    }
*/
    public String splitCategoryName(String categoryName) {
        String[] split = categoryName.split(">");
        if (split.length > 1) {
            return split[1].strip();
        } else {
            return split[0].strip();
        }
    }
}
