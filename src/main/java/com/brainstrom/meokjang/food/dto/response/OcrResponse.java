package com.brainstrom.meokjang.food.dto.response;

import com.brainstrom.meokjang.food.dto.OcrFoodDto;
import lombok.Getter;

import java.util.Map;

@Getter
public class OcrResponse {

    private final Map<Integer, OcrFoodDto> list;
    private final Map<Integer, Map<String, Integer>> recommend;

    public OcrResponse(Map<Integer, OcrFoodDto> list, Map<Integer, Map<String, Integer>> recommend) {
        this.list = list;
        this.recommend = recommend;
    }
}
