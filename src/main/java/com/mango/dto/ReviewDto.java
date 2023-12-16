package com.mango.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {
    private boolean score;
    private String reviewContents;
    private MultipartFile reviewPic;

}
