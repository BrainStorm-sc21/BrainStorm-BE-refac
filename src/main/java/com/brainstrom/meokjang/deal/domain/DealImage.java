package com.brainstrom.meokjang.deal.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DEAL_IMAGE")
@Getter
@NoArgsConstructor
public class DealImage {

    @Id @Column(name = "deal_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dealImageId;

    @ManyToOne
    @JoinColumn(name = "deal_id", referencedColumnName = "deal_id", nullable = false)
    private Deal deal;

    @Column(name = "image") @Lob
    private byte[] image;
}
