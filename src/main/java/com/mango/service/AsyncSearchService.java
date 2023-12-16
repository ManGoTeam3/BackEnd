package com.mango.service;

import com.mango.entity.Restaurant;
import com.mango.repository.RestaurantRepository;
import com.mango.service.kakaoApiDto.RestaurantDocuments;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncSearchService {
    private final RestaurantRepository restaurantRepository;

    @Async //이거 db다 저장하려니 속도 성능문제가 너무 커서 비동기로 해야할 수도
    public void saveRestaurantToDB(List<RestaurantDocuments> restaurantDocuments) {

        List<Restaurant> restaurantList = restaurantDocuments.stream()
            .map(d -> d.documentsToEntity())
            .collect(Collectors.toList());

        restaurantRepository.saveAll(restaurantList);
//        restaurantDocuments.forEach(d -> restaurantRepository.save(d.documentsToEntity()));
    }
}
