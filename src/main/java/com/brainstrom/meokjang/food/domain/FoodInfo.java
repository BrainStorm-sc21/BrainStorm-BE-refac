package com.brainstrom.meokjang.food.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(schema = "FOOD_INFO")
@Getter
@NoArgsConstructor
public class FoodInfo {

    @Id @Column(name = "food_info_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodInfoId;

    @Column(name = "food_info_name", length = 30, nullable = false)
    private String foodInfoName;

    @Column(name = "storage_way", nullable = false)
    private Byte storageWay;

    @Column(name = "storage_day", nullable = false)
    private Integer storageDay;

    public FoodInfo(Long foodInfoId, String foodInfoName, Byte storageWay, Integer storageDay) {
        this.foodInfoId = foodInfoId;
        this.foodInfoName = foodInfoName;
        this.storageWay = storageWay;
        this.storageDay = storageDay;
    }
}
