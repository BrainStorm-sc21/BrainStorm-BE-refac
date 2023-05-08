package com.brainstrom.meokjang.service;

import com.brainstrom.meokjang.domain.Food;
import com.brainstrom.meokjang.dto.FoodList;
import com.brainstrom.meokjang.repository.FoodRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FoodService {
    private FoodRepository foodRepo;

    @Autowired
    public FoodService(FoodRepository foodRepo) {
        this.foodRepo = foodRepo;
    }

    public FoodList getList(Long userId) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Food> list = foodRepo.findAllByUserId(userId);

        FoodList foodList = new FoodList();
        foodList.setCount(list.size());

        try {
            foodList.setFoodList(objectMapper.readValue(objectMapper.writeValueAsString(list), List.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return foodList;
    }

    public Food save(Food food) {
        return foodRepo.save(food);
    }

    public void delete(Long foodId) {
        foodRepo.deleteById(foodId);
    }

    public Food update(Long foodId, Food food) {
        Food foodEntity = foodRepo.getById(foodId);
        foodEntity.setFoodName(food.getFoodName());
        foodEntity.setStock(food.getStock());
        foodEntity.setExpireDate(food.getExpireDate());
        foodEntity.setStorageWay(food.getStorageWay());
        return foodEntity;
    }

    public Food get(Long foodId) {
        return foodRepo.getById(foodId);
    }


}