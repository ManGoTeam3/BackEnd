package com.mango.repository;

import com.mango.entity.ReviewPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface ReviewPictureRepository extends JpaRepository<ReviewPicture,Long>{

    Optional<ReviewPicture> findByReviewId(Long reviewId);
    List<ReviewPicture> findAllByReviewId(Long reviewId);
}
