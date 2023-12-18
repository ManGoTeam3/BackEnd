package com.mango.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Builder
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "Review")
public class Review {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Restaurant_Id")
    private Restaurant restaurant;

    @Column(name = "Score")
    private int score;

    @Column(name = "Review_Contents")
    private String reviewContents;


    @Column(name = "user_name")
    private String username;

    @Column(name="created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewPicture> reviewPictures;


}

