package com.mango.service;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.mango.dto.GetAllReviewDto;
import com.mango.dto.ReviewDto;
import com.mango.entity.Restaurant;
import com.mango.entity.Review;
import com.mango.entity.ReviewPicture;
import com.mango.repository.RestaurantRepository;
import com.mango.repository.ReviewPictureRepository;
import com.mango.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewPictureRepository reviewPictureRepository;
    private final RestaurantRepository restaurantRepository;
    private String baseurl = "https://storage.googleapis.com/durian-storage/";
    private Storage storage;

    // 버킷이름 따로 properties에 저장했음
    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    public ResponseEntity enrollReview(Long restaurantId ,ReviewDto reviewDto) throws IOException {
        //restaurantId로 restaurant 찾기
        long reviewId = 0L;
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new IllegalArgumentException("해당 식당이 없습니다.")
        );

        String fileName = UUID.randomUUID().toString(); // 파일명 중복 방지
        // MultipartFile이 null인 경우 예외처리
        if (reviewDto.getReviewPic() == null) {
            throw new IllegalArgumentException("리뷰 이미지를 업로드해주세요.");
        }
        String ext = reviewDto.getReviewPic().getContentType();
        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, fileName)
                        .setContentType(ext)
                        .build(),
                reviewDto.getReviewPic().getInputStream()
        );

        Review review = Review.builder()
                .score(reviewDto.isScore())
                .restaurant(restaurant)
                .reviewContents(reviewDto.getReviewContents())
                .build();

        try {
            reviewId = reviewRepository.save(review).getId();

        } catch (Exception e) {
            throw new RuntimeException("리뷰 등록에 실패했습니다.");
        }
        Review reviewResult = reviewRepository.findById(reviewId).orElseThrow(
                () -> new IllegalArgumentException("해당 식당의 리뷰가 없습니다.")
        );

        ReviewPicture reviewPicture = ReviewPicture.builder()
                .reviewPicUrl(baseurl+fileName)
                .review(reviewResult)
                .build();

        try {
            reviewPictureRepository.save(reviewPicture);
        } catch (Exception e) {
            throw new RuntimeException("리뷰 등록에 실패했습니다.");
        }

        return ResponseEntity.ok().body("리뷰 등록에 성공했습니다.");

    }

    public ResponseEntity getReview(Long restaurantId) {
        //restaurantId로 restaurant 존재하는지 확인
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new IllegalArgumentException("해당 식당이 없습니다.")
        );
        //restaurantId로 review 가져오기
        List<Review> reviewList = (List<Review>) reviewRepository.findById(restaurantId).orElseThrow(
                () -> new IllegalStateException("리뷰를 찾을 수 없습니다."));


        //reviewList -> GetAllReviewDto로 변환하기
        List<GetAllReviewDto> getAllReviewDtoList = reviewList.stream()
                .flatMap(review -> review.getReviewPictures().stream()
                        .map(reviewPicture -> GetAllReviewDto.builder()
                                .reviewId(review.getId())
                                .restaurantId(review.getRestaurant().getId())
                                .score(review.getScore())
                                .reviewContents(review.getReviewContents())
                                .reviewPicUrl(Collections.singletonList(reviewPicture.getReviewPicUrl()))
                                .build()

                        )
                )
                .collect(Collectors.toList());

        return ResponseEntity.ok(getAllReviewDtoList);

    }

    public ResponseEntity getReviewDetail(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new IllegalStateException("리뷰가 존재하지 않습니다.")
        );

        //review -> GetAllReviewDtoList
        GetAllReviewDto getDetailReview = GetAllReviewDto.builder()
                .reviewId(review.getId())
                .restaurantId(review.getRestaurant().getId())
                .score(review.getScore())
                .reviewContents(review.getReviewContents())
                .reviewPicUrl(review.getReviewPictures().stream()
                        .map(ReviewPicture::getReviewPicUrl)
                        .collect(Collectors.toList())
                ).build();

        return ResponseEntity.ok(getDetailReview);
    }

    public ResponseEntity deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new IllegalStateException("리뷰가 존재하지 않습니다.")
        );

        List<ReviewPicture> reviewPictures = (List<ReviewPicture>) reviewPictureRepository.findById(reviewId).orElseThrow(
                () -> new IllegalStateException("리뷰 사진이 존재하지 않습니다.")
        );

        //삭제
        reviewPictureRepository.deleteAll(reviewPictures);
        reviewRepository.delete(review);
        return ResponseEntity.ok().body("리뷰가 삭제되었습니다.");
    }

}
