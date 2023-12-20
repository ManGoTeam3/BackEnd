package com.mango.service;

import com.mango.entity.Restaurant;
import com.mango.repository.RestaurantRepository;
import com.mango.service.kakaoApiDto.KakaoApiResponseDto;
import com.mango.service.kakaoApiDto.RestaurantDocuments;
import com.mango.service.responsedto.DetailResponseDto;
import com.mango.service.responsedto.SearchResponseDto;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final KakaoApiService kakaoApiService;
    private final AsyncRestaurantService asyncRestaurantService;

    public DetailResponseDto getRestaurantDetail(long restaurantId) {
        Restaurant findRestaurant = restaurantRepository.findById(restaurantId).orElse(null);
        RestaurantDocuments detailDocuments = null;

        if (findRestaurant == null) {
            return null;
        } else {
            List<RestaurantDocuments> documents = kakaoApiService.kakaoApiSearchRestaurantByXY(
                findRestaurant.getX(), findRestaurant.getY()).getDocuments();

            for (RestaurantDocuments d : documents) {
                if (d.getId() == restaurantId) {
                    detailDocuments = d;
                }
            }
        }

        if (detailDocuments == null) {
            return null;
        } else {
            return DetailResponseDto.builder()
                .addressName(detailDocuments.getAddress_name())
                .categoryName(detailDocuments.getCategory_name())
                .phone(detailDocuments.getPhone())
                .placeName(detailDocuments.getPlace_name())
                .placeUrl(detailDocuments.getPlace_url())
                .roadAddressName(detailDocuments.getRoad_address_name())
                .score(findRestaurant.getScore())
                .x(detailDocuments.getX())
                .y(detailDocuments.getY()).build();
        }

    }

    public List searchRestaurant(String query) {
        List<RestaurantDocuments> tempList = new ArrayList<>();


        searchRestaurantMax(tempList, query);
        asyncRestaurantService.saveRestaurantToDB(tempList);

        List<SearchResponseDto> resultList = restaurantDocumentsToSearchResponseDto(
            tempList); //맛집 데이터 목록에 필요한 정보만 가공 후 반환

        return resultList;
    }

    public void searchRestaurantMax(List<RestaurantDocuments> tempList, String query) {

        int page = 1;

        while (true) { //최대 갯수 조회. 카카오 api의 최대는 3페이지까지(45개)
            KakaoApiResponseDto kakaoRestaurantApiResponseDto = kakaoApiService.kakaoApiSearchRestaurant(
                query, page);
            tempList.addAll(kakaoRestaurantApiResponseDto.getDocuments());
            if (kakaoRestaurantApiResponseDto.getMeta().getIs_end().equals("true") || page == 3) {
                break;
            }
            page++;
        }
    }

    public String splitCategoryName(String categoryName) {
        String[] split = categoryName.split(">");
        if (split.length > 1) {
            return split[1].strip();
        } else {
            return split[0].strip();
        }
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
//                .score(getScoreIfExists(r.getId()))
                .build())
            .collect(Collectors.toList());
    }

    public double getScoreIfExists(long restaurantId){
        Restaurant temp = restaurantRepository.findById(restaurantId).orElse(null);
        if(temp == null) {
            return 0.0;
        } else {
            return temp.getScore();
        }
    }
}
