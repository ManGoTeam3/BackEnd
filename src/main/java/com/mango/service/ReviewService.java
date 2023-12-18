package com.mango.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Component
@Transactional
public class ReviewService {

    @Autowired
    private final AmazonS3 amazonS3Client;

    private final ReviewRepository reviewRepository;
    private final ReviewPictureRepository reviewPictureRepository;
    private final RestaurantRepository restaurantRepository;
    //private String baseurl = "https://storage.googleapis.com/durian-storage/";
    //private final Storage storage;


    @Value("${spring.cloud.aws.bucket}")
    private String bucket;

    public ResponseEntity enrollReview(Long restaurantId , ReviewDto reviewDto, String userName) throws IOException {
        String dirName = "durian";
        String S3Link;
        //restaurantId로 restaurant 찾기
        long reviewId = 0L;
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new IllegalArgumentException("해당 식당이 없습니다.")
        );

        // MultipartFile이 null인 경우 예외처리
        if (reviewDto.getReviewPic() == null) {
            throw new IllegalArgumentException("리뷰 이미지를 업로드해주세요.");
        }
        // MultipartFile을 전달받아 File로 전환한 후 S3에 업로드
        File uploadFile = convert(reviewDto.getReviewPic())
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));

        //업로드
        S3Link = upload(uploadFile, dirName);


        /*
        String fileName = UUID.randomUUID().toString(); // 파일명 중복 방지
        String ext = reviewDto.getReviewPic().getContentType();

        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, fileName)
                        .setContentType(ext)
                        .build(),
                reviewDto.getReviewPic().getInputStream()
        );
*/
        Review review = Review.builder()
                .score(reviewDto.getScore())
                .restaurant(restaurant)
                .reviewContents(reviewDto.getReviewContents())
                .username(userName)
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
                .reviewPicUrl(S3Link)
                .review(reviewResult)
                .build();

        try {
            reviewPictureRepository.save(reviewPicture);
        } catch (Exception e) {
            throw new RuntimeException("리뷰 등록에 실패했습니다.");
        }


        return ResponseEntity.ok().contentType(new MediaType("text", "plain", StandardCharsets.UTF_8)).body("리뷰 등록 성공");


    }

    public ResponseEntity getReview(Long restaurantId) {
        //restaurantId로 restaurant 존재하는지 확인
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new IllegalArgumentException("해당 식당이 없습니다.")
        );
        //restaurantId로 review 존재하는지 확인
        List<Review> reviewList = reviewRepository.findAllByRestaurantId(restaurantId);
        if (!reviewList.isEmpty()) {
            //restaurantId로 review 가져오기
            //reviewList -> GetAllReviewDto로 변환하기
            List<GetAllReviewDto> getAllReviewDtoList = reviewList.stream()
                    .flatMap(review -> review.getReviewPictures().stream()
                            .map(reviewPicture -> GetAllReviewDto.builder()
                                    .reviewId(review.getId())
                                    .restaurantId(review.getRestaurant().getId())
                                    .score(review.getScore())
                                    .reviewContents(review.getReviewContents())
                                    .reviewPicUrl(Collections.singletonList(reviewPicture.getReviewPicUrl()))
                                    .username(review.getUsername())
                                    .createdAt(String.valueOf(review.getCreatedAt()))
                                    .build()

                            )
                    )
                    .collect(Collectors.toList());

            return ResponseEntity.ok(getAllReviewDtoList);
        } else {

            return ResponseEntity.ok().body("해당 식당의 리뷰가 없습니다.");
        }
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
                .createdAt(String.valueOf(review.getCreatedAt()))
                .username(review.getUsername())
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

        Optional<ReviewPicture> reviewPicture = reviewPictureRepository.findByReviewId(reviewId);

        if (reviewPicture.isPresent()){
            List<ReviewPicture> reviewPictures = reviewPictureRepository.findAllByReviewId(reviewId);

            //삭제
            reviewPictureRepository.deleteAll(reviewPictures);
            reviewRepository.delete(review);
            return ResponseEntity.ok().contentType(new MediaType("text", "plain", StandardCharsets.UTF_8)).body("리뷰 삭제가 완료되었습니다.");
        }else{
            //존재하지 않음
            return ResponseEntity.ok().contentType(new MediaType("text", "plain", StandardCharsets.UTF_8)).body("리뷰가 존재하지 않습니다.");
        }

    }
    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile);  // convert()함수로 인해서 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)

        return uploadImageUrl;      // 업로드된 파일의 S3 URL 주소 반환
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)	// PublicRead 권한으로 업로드 됨
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if(targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        }else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }

}
