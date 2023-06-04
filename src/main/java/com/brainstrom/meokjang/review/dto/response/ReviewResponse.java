package com.brainstrom.meokjang.review.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewResponse {

    private Long reviewId;
    private Long reviewFrom;
    private Long reviewTo;
    private Long dealId;
    private Float rating;
    private String reviewContent;
    private LocalDateTime createdAt;

    public ReviewResponse(Long reviewId, Long reviewFrom, Long reviewTo, Long dealId, Float rating, String reviewContent, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.reviewFrom = reviewFrom;
        this.reviewTo = reviewTo;
        this.dealId = dealId;
        this.rating = rating;
        this.reviewContent = reviewContent;
        this.createdAt = createdAt;
    }
}
