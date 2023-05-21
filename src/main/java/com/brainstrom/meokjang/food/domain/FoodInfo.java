package com.brainstrom.meokjang.food.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(schema = "FOOD_INFO")
@Getter
@NoArgsConstructor
public class FoodInfo {

    @Id @Column(name = "info_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long infoId;

    @Column(name = "info_name", length = 20, nullable = false)
    private String infoName;

    @Column(name = "storage_way", nullable = false)
    private String storageWay;

    @Column(name = "storage_day", nullable = false)
    private Integer storageDay;

    public FoodInfo(Long infoId, String infoName, String storageWay, Integer storageDay) {
        this.infoId = infoId;
        this.infoName = infoName;
        this.storageWay = storageWay;
        this.storageDay = storageDay;
    }
}
