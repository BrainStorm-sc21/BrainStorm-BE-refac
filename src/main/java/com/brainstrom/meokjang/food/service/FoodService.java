package com.brainstrom.meokjang.food.service;

import com.brainstrom.meokjang.food.domain.Food;
import com.brainstrom.meokjang.food.dto.OcrFoodDto;
import com.brainstrom.meokjang.food.dto.request.FoodRequest;
import com.brainstrom.meokjang.food.dto.response.FoodResponse;
import com.brainstrom.meokjang.food.dto.response.OcrResponse;
import com.brainstrom.meokjang.food.dto.request.OcrRequest;
import com.brainstrom.meokjang.food.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Food food = foodRequest.toEntity();
        return new FoodResponse(foodRepo.save(food));
    }

    public List<FoodResponse> saveList(List<FoodRequest> foodRequestList) {
        List<Food> foodList = foodRequestList.stream().map(foodRequest->foodRequest.toEntity()).toList();
        List<Food> result = foodRepo.saveAll(foodList);
        return result.stream().map(FoodResponse::new).toList();
    }

    public void delete(Long foodId) {
        foodRepo.deleteById(foodId);
    }

    public FoodResponse update(Long foodId, FoodRequest foodRequest) {
        Food foodEntity = foodRepo.findById(foodId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 음식이 없습니다."));
        foodEntity.setFoodName(foodRequest.getFoodName());
        foodEntity.setStock(foodRequest.getStock());
        foodEntity.setExpireDate(foodRequest.getExpireDate());
        foodEntity.setStorageWay(foodRequest.getStorageWay());
        return new FoodResponse(foodRepo.save(foodEntity));
    }

    public FoodResponse get(Long foodId) {
        Food foodEntity = foodRepo.findById(foodId)
                .orElseThrow(() -> new IllegalArgumentException("해당 음식이 없습니다."));
        return new FoodResponse(foodEntity);
    }


    public List<OcrResponse> recommend(OcrRequest ocrRequest) {
        Map<Integer, OcrFoodDto> ocrResult = null;
        try {
            ocrResult = recommendService.doOcr(ocrRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 추천 로직 구현

        return null;
    }
}
