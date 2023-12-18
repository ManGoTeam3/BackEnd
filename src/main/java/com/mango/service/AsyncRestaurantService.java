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
public class AsyncRestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Async
    public void saveRestaurantToDB(List<RestaurantDocuments> restaurantDocuments) {

        List<Restaurant> restaurantList = restaurantDocuments.stream()
            .map(d -> d.documentsToEntity())
            .collect(Collectors.toList());

        restaurantRepository.saveAll(restaurantList);
    }

}
