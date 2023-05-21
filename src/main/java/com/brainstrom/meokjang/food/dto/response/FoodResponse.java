package com.brainstrom.meokjang.food.dto.response;

import com.brainstrom.meokjang.food.domain.Food;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class FoodResponse {
    private final Long foodId;
    private final String foodName;

    private final Double stock;

    private final LocalDate expireDate;

    private final String storageWay;

    public FoodResponse(Food food) {
        this.foodId = food.getFoodId();
        this.foodName = food.getFoodName();
        this.stock = food.getStock();
        this.expireDate = food.getExpireDate();
        this.storageWay = food.getStorageWay();
    }
}
