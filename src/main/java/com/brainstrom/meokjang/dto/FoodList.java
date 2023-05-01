package com.brainstrom.meokjang.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class FoodList {
    private Integer count;
    private List<Map<String, Object>> foodList;
}
