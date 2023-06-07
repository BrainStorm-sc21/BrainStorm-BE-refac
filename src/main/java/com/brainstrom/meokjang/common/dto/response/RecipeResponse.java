package com.brainstrom.meokjang.common.dto.response;

import lombok.Getter;

@Getter
public class RecipeResponse {

    private String recipe;
    public RecipeResponse(String recipe) {
        this.recipe = recipe;
    }
}
