package com.brainstrom.meokjang;

import com.brainstrom.meokjang.food.domain.Food;
import com.brainstrom.meokjang.food.dto.request.FoodDto;
import com.brainstrom.meokjang.food.dto.request.FoodRequest;
import com.brainstrom.meokjang.food.dto.response.FoodResponse;
import com.brainstrom.meokjang.food.repository.FoodRepository;
import com.brainstrom.meokjang.food.service.FoodService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FoodServiceTest {
    FoodRepository foodRepoMock = mock(FoodRepository.class);
    FoodService foodService = new FoodService(foodRepoMock, null,null);

    @Test
    public void testGetList() {
        List<Food> foods = new ArrayList<>();
        foods.add(new Food(1L, "food1", 10D, "2023-05-01", "냉장"));
        foods.add(new Food(1L, "food2", 20D, "2023-05-02", "냉장"));
        when(foodRepoMock.findAllByUserId(1L)).thenReturn(foods);

        List<FoodResponse> result = foodService.getList(1L);
        assertEquals(foods.size(), result.size());
        assertEquals(foods.get(0).getFoodName(), result.get(0).getFoodName());
        assertEquals(foods.get(0).getStock(), result.get(0).getStock());
        assertEquals(foods.get(0).getExpireDate(), result.get(0).getExpireDate());
        assertEquals(foods.get(0).getStorageWay(), result.get(0).getStorageWay());
        assertEquals(foods.get(1).getFoodName(), result.get(1).getFoodName());
        assertEquals(foods.get(1).getStock(), result.get(1).getStock());
        assertEquals(foods.get(1).getExpireDate(), result.get(1).getExpireDate());
        assertEquals(foods.get(1).getStorageWay(), result.get(1).getStorageWay());
    }

    @Test
    public void testSaveFood() {
        //saveFoodTest

        FoodRequest foodRequest = new FoodRequest(1L, new FoodDto("food1", 10D, "2023-05-01", "냉장"));
        Food foodEntity = new Food(1L,"food1", 10D, "2023-05-01", "냉장");
        when(foodRepoMock.save(Mockito.any(Food.class))).thenReturn(foodEntity);

        FoodResponse result = foodService.save(foodRequest);
        assertEquals(foodEntity.getFoodName(), result.getFoodName());
        assertEquals(foodEntity.getStock(), result.getStock());
        assertEquals(foodEntity.getExpireDate(), result.getExpireDate());
        assertEquals(foodEntity.getStorageWay(), result.getStorageWay());
    }

    @Test
    public void testDelete() {
        Long foodId = 1L;
        doNothing().when(foodRepoMock).deleteById(foodId);

        assertDoesNotThrow(() -> foodService.delete(foodId));

        verify(foodRepoMock, times(1)).deleteById(foodId);
    }

    @Test
    public void testUpdate() {
        Long foodId = 1L;
        FoodRequest foodRequest = new FoodRequest(1L, new FoodDto("food2", 5D, "2023-05-01", "냉장"));
        Food foodEntity = new Food(1L, "food1", 10D, "2023-05-01", "냉장");
        when(foodRepoMock.findById(foodId)).thenReturn(Optional.of(foodEntity));
        when(foodRepoMock.save(Mockito.any(Food.class))).thenReturn(foodEntity);

        FoodResponse result = foodService.update(foodId, foodRequest);

        assertEquals(foodRequest.getFood().getFoodName(), result.getFoodName());
        assertEquals(foodRequest.getFood().getStock(), result.getStock());
        assertEquals(foodRequest.getFood().getExpireDate(), result.getExpireDate());
        assertEquals(foodRequest.getFood().getStorageWay(), result.getStorageWay());
    }

    @Test
    public void testGet() {
        Long foodId = 1L;
        Food food = new Food(1L, "food1", 10D, "2023-05-01", "냉장");
        when(foodRepoMock.findById(foodId)).thenReturn(Optional.of(food));

        FoodResponse result = foodService.get(foodId);

        assertEquals(food.getFoodName(), result.getFoodName());
        assertEquals(food.getStock(), result.getStock());
        assertEquals(food.getExpireDate(), result.getExpireDate());
        assertEquals(food.getStorageWay(), result.getStorageWay());
    }
}