package com.brainstrom.meokjang.food.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OcrFoodDto {
    private String foodName;
    private Integer stock;

    public OcrFoodDto(String foodName, Integer stock){
        this.foodName = foodName;
        this.stock = stock;
    }
}
