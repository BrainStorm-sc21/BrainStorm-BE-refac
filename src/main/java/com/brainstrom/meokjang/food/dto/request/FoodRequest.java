package com.brainstrom.meokjang.food.dto.request;

import com.brainstrom.meokjang.food.domain.Food;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class FoodRequest {
    private final Long userId;
    private final String foodName;
    private final Integer stock;
    private final LocalDate expireDate;
    private final String storageWay;

    public Food toEntity(){
        Food food = new Food();
        food.setUserId(this.userId);
        food.setFoodName(this.foodName);
        food.setStock(this.stock);
        food.setExpireDate(this.expireDate);
        food.setStorageWay(this.storageWay);

        return food;
    }

    public FoodRequest(Long userId, String foodName, Integer stock, LocalDate expireDate, String storageWay) {
        this.userId = userId;
        this.foodName = foodName;
        this.stock = stock;
        this.expireDate = expireDate;
        this.storageWay = storageWay;
    }
}