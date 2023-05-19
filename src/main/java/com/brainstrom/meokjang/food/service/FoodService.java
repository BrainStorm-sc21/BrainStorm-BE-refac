package com.brainstrom.meokjang.food.service;

import com.brainstrom.meokjang.food.domain.Food;
import com.brainstrom.meokjang.food.dto.OcrFoodDto;
import com.brainstrom.meokjang.food.dto.request.FoodListRequest;
import com.brainstrom.meokjang.food.dto.request.FoodDto;
import com.brainstrom.meokjang.food.dto.request.FoodRequest;
import com.brainstrom.meokjang.food.dto.response.FoodResponse;
import com.brainstrom.meokjang.food.dto.response.OcrResponse;
import com.brainstrom.meokjang.food.dto.request.OcrRequest;
import com.brainstrom.meokjang.food.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class FoodService {
    private FoodRepository foodRepo;

    @Autowired
    public FoodService(FoodRepository foodRepo) {
        this.foodRepo = foodRepo;
    }

    @Autowired
    private RecommendService recommendService;

    public List<FoodResponse> getList(Long userId) {
        List<Food> foods = foodRepo.findAllByUserId(userId);
        return foods.stream().map(FoodResponse::new).toList();
    }

    public FoodResponse save(FoodRequest foodRequest) {
        Food food = foodRequest.getFood().toEntity();
        food.setUserId(foodRequest.getUserId());
        return new FoodResponse(foodRepo.save(food));
    }

    public List<FoodResponse> saveList(FoodListRequest foodRequest) {
        List<Food> foods = foodRequest.getFoodList().stream().map(FoodDto::toEntity).toList();
        foods.forEach(food -> food.setUserId(foodRequest.getUserId()));
        List<Food> result = foodRepo.saveAll(foods);
        return result.stream().map(FoodResponse::new).toList();
    }

    public void delete(Long foodId) {
        foodRepo.deleteById(foodId);
    }

    public FoodResponse update(Long foodId, FoodRequest foodRequest) {
        try {
            Food foodEntity = foodRepo.findById(foodId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 음식이 없습니다."));
            if (!foodRequest.getUserId().equals(foodEntity.getUserId())) {
                throw new IllegalArgumentException("해당 음식의 소유자가 아닙니다.");
            }
            FoodDto foodDto = foodRequest.getFood();
            foodEntity.setFoodName(foodDto.getFoodName());
            foodEntity.setStock(foodDto.getStock());
            foodEntity.setExpireDate(foodDto.getExpireDate());
            foodEntity.setStorageWay(foodDto.getStorageWay());
            return new FoodResponse(foodRepo.save(foodEntity));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public FoodResponse get(Long foodId) {
        try {
            Food foodEntity = foodRepo.findById(foodId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 음식이 없습니다."));
            return new FoodResponse(foodEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public OcrResponse recommend(OcrRequest ocrRequest) {
        Map<Integer, OcrFoodDto> ocrResult = null;
        try {
            ocrResult = recommendService.doOcr(ocrRequest);
            if (ocrResult == null)
                throw new IllegalArgumentException("OCR 결과가 없습니다.");
            else
                return recommendService.recommend(ocrResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 추천 로직 구현
        System.out.println(ocrResult);
        return null;
    }
}
