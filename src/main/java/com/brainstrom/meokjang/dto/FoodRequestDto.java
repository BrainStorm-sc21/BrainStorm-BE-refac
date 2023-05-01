package com.brainstrom.meokjang.dto;

import com.brainstrom.meokjang.domain.Food;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class FoodRequestDto {
    private Long foodId;
    private Long userId;
    private Long infoId;
    private String foodName;
    private Integer stock;
    private Date expireDate;
    private Integer storageWay;
    private Date createdAt;

    public Food toEntity(){
        Food food = new Food();
        food.setFoodId(this.foodId);
        food.setUserId(this.userId);
        food.setInfoId(this.infoId);
        food.setFoodName(this.foodName);
        food.setStock(this.stock);
        food.setExpireDate(this.expireDate);
        food.setStorageWay(this.storageWay);
        food.setCreatedAt(this.createdAt);

        return food;
    }
}