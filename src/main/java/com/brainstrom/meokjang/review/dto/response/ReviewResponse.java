package com.brainstrom.meokjang.review.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewResponse {

    private final Long reviewId;
    private final Long reviewFrom;
    private final String reviewFromName;
    private final Float reviewFromReliability;
    private final Long reviewTo;
    private final String reviewToName;
    private final Float reviewToReliability;
    private final Long dealId;
    private final Float rating;
    private final String reviewContent;
    private final LocalDateTime createdAt;

    @Builder
    public ReviewResponse(Long reviewId, Long reviewFrom, String reviewFromName, Float reviewFromReliability,
                          Long reviewTo, String reviewToName, Float reviewToReliability,
                          Long dealId, Float rating, String reviewContent, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.reviewFrom = reviewFrom;
        this.reviewFromName = reviewFromName;
        this.reviewFromReliability = reviewFromReliability;
        this.reviewTo = reviewTo;
        this.reviewToName = reviewToName;
        this.reviewToReliability = reviewToReliability;
        this.dealId = dealId;
        this.rating = rating;
        this.reviewContent = reviewContent;
        this.createdAt = createdAt;
    }
}
