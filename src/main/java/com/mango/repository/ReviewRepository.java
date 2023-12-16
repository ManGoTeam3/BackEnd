package com.mango.repository;

import com.mango.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findAllByRestaurantId(Long restaurantId);

    //Optional<Review> findById(Long reviewId);

    //Optional<Review> findByRestaurantId(Long restaurantId);
}
