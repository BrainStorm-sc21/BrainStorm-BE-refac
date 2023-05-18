package com.brainstrom.meokjang.food.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class FoodListRequest {
    private Long userId;
    private List<FoodDto> foodList;

    public FoodListRequest(Long userId, List<FoodDto> foodList) {
        this.userId = userId;
        this.foodList = foodList;
    }
}
