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
@Async
public class AsyncRestaurantService {

    private final RestaurantRepository restaurantRepository;

    public void saveRestaurantToDB(List<RestaurantDocuments> restaurantDocuments) {
        List<Restaurant> all = restaurantRepository.findAll();
        List<Long> allId = all.stream()
            .map(restaurant -> restaurant.getId())
            .collect(Collectors.toList());

        List<Restaurant> restaurantList = restaurantDocuments.stream()
            .filter(d -> !isExistId(allId, d.getId()))
            .map(d -> d.documentsToEntity())
            .collect(Collectors.toList());

        restaurantRepository.saveAll(restaurantList);
    }

    public boolean isExistId(List<Long> checkList, Long checkId){

        if(checkList.contains(checkId)) {
            return true;
        } else {
            return false;
        }
    }

}
