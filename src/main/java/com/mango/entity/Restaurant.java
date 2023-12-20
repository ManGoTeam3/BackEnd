package com.mango.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Restaurant")
public class Restaurant {

    @Id
    private Long id;

    @Column
    private Double x;

    @Column
    private Double y;

    @Column
    private Double score;
}
