package com.brainstrom.meokjang.review.domain;

import com.brainstrom.meokjang.deal.domain.Deal;
import com.brainstrom.meokjang.user.domain.User;
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
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "review_from", referencedColumnName = "user_id", nullable = false)
    private User reviewFrom;

    @ManyToOne
    @JoinColumn(name = "review_to", referencedColumnName = "user_id", nullable = false)
    private User reviewTo;

    @ManyToOne
    @JoinColumn(name = "deal_id", referencedColumnName = "deal_id", nullable = false)
    private Deal deal;

    @Column(name = "rating", nullable = false)
    private Float rating;

    @Column(name = "review_content", length = 200)
    private String reviewContent;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public Review(User reviewFrom, User reviewTo, Deal deal, Float rating, String reviewContent) {
        this.reviewFrom = reviewFrom;
        this.reviewTo = reviewTo;
        this.deal = deal;
        this.rating = rating;
        this.reviewContent = reviewContent;
    }
}
