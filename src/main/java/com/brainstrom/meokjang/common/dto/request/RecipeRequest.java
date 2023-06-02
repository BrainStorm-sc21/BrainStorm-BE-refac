package com.brainstrom.meokjang.common.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@NoArgsConstructor
public class RecipeRequest {

    private ArrayList<String> foodList;

    public RecipeRequest(ArrayList<String> foodList) {
        this.foodList = foodList;
    }
}
