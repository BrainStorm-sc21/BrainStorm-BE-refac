package com.brainstrom.meokjang.review.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(schema = "REVIEW")
@Getter
@NoArgsConstructor
public class Review {

    @Id @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewId;

    @Column(name = "review_from", nullable = false)
    private Integer reviewFrom;

    @Column(name = "review_to", nullable = false)
    private Integer reviewTo;

    @Column(name = "deal_id", nullable = false)
    private Integer dealId;

    @Column(name = "rating", nullable = false)
    private Float rating;

    @Column(name = "review_content", length = 1000)
    private String reviewContent;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public Review(Integer reviewFrom, Integer reviewTo, Integer dealId, Float rating, String reviewContent) {
        this.reviewFrom = reviewFrom;
        this.reviewTo = reviewTo;
        this.dealId = dealId;
        this.rating = rating;
        this.reviewContent = reviewContent;
    }
}
