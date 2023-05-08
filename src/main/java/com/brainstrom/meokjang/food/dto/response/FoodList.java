package com.brainstrom.meokjang.food.dto.response;

import com.brainstrom.meokjang.food.domain.Food;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@ToString
public class FoodList {
    private Integer count;
    // 식품 정보를 담는 리스트
    private List<Map<String, Object>> foodList;

    public FoodList(List<Food> foods) {
        this.count =0;
        this.foodList =new ArrayList<Map<String, Object>>();
    }
}

