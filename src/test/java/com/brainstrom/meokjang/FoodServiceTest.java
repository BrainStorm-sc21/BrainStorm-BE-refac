package com.brainstrom.meokjang;

import com.brainstrom.meokjang.food.domain.Food;
import com.brainstrom.meokjang.food.dto.response.FoodResponse;
import com.brainstrom.meokjang.food.repository.FoodRepository;
import com.brainstrom.meokjang.food.service.FoodService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
public class FoodServiceTest {
    FoodRepository foodRepoMock = mock(FoodRepository.class);
    FoodService foodService = new FoodService(foodRepoMock);

    @Test
    public void testGetList() {
        Long userId = 1L;
        List<Food> foodList = new ArrayList<>();
        foodList.add(new Food(1L, "food1", 10, "2023-05-01", "냉장"));
        foodList.add(new Food(1L, "food2", 20, "2023-05-02", "냉동"));
        List<FoodResponse> savedFood = foodRepoMock.saveAll(foodList)
                .stream().map(FoodResponse::new).toList();
        List<FoodResponse> result = foodService.getList(userId);
        assertEquals(savedFood, result);
    }
//
//    @Test
//    public void testSaveFood() {
//        Food food = new Food(1L, "food1", 10, "2023-05-01", "냉장");
//        FoodResponse savedFood = foodService.save(food.toFoodRequest());
//        FoodResponse result = foodRepoMock.findById(food.getFoodId())
//                .map(FoodResponse::new)
//                .orElseThrow(() -> new IllegalArgumentException("해당 음식이 없습니다."));
//        assertEquals(savedFood, result);
//    }

//    @Test
//    public void testDelete() {
//        Long foodId = 1L;
//        doNothing().when(foodRepoMock).deleteById(foodId);
//
//        assertDoesNotThrow(() -> foodService.delete(foodId));
//
//        verify(foodRepoMock, times(1)).deleteById(foodId);
//    }
//
//    @Test
//    public void testUpdate() {
//        Long foodId = 1L;
//        Food foodToUpdate = new Food(1L, "food1", 10, "2023-05-01", "냉장");
//        Food foodEntity = new Food(1L, "food1", 10, "2023-05-01", "냉장");
//        when(foodRepoMock.getById(foodId)).thenReturn(foodEntity);
//
//        Food result = foodService.update(foodId, foodToUpdate);
//
//        assertEquals(foodEntity.getFoodName(), result.getFoodName());
//        assertEquals(foodToUpdate.getFoodName(), foodEntity.getFoodName());
//        assertEquals(foodToUpdate.getStock(), foodEntity.getStock());
//        assertEquals(foodToUpdate.getExpireDate(), foodEntity.getExpireDate());
//        assertEquals(foodToUpdate.getStorageWay(), foodEntity.getStorageWay());
//    }
//
//    @Test
//    public void testGet() {
//        Long foodId = 1L;
//        Food food = new Food(1L, "food1", 10, "2023-05-01", "냉장");
//        when(foodRepoMock.findById(foodId)).thenReturn(Optional.of(food));
//
//        FoodResponse result = foodService.find(foodId);
//
//        assertEquals(food, result);
//    }
}