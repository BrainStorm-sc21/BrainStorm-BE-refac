package com.brainstrom.meokjang.deal.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(schema = "DealImage")
@Getter
@NoArgsConstructor
public class DealImage {

    @Id @Column(name = "di_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer diId;

    @Column(name = "deal_id", nullable = false)
    private Integer dealId;

    @Column(name = "image") @Lob
    private byte[] image;
}
