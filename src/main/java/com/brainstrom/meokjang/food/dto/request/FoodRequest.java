package com.brainstrom.meokjang.food.dto.request;

import com.brainstrom.meokjang.food.domain.Food;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class FoodRequest {
    private final Long userId;
    private final String foodName;
    private final Integer stock;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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

    public FoodRequest(Long userId, String foodName, Integer stock, String expireDate, String storageWay) {
        this.userId = userId;
        this.foodName = foodName;
        this.stock = stock;
        this.expireDate = LocalDate.parse(expireDate);
        this.storageWay = storageWay;
    }
}