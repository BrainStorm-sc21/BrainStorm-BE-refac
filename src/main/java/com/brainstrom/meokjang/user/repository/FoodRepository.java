package com.brainstrom.meokjang.repository;

import com.brainstrom.meokjang.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long>{

    Food getById(Long aLong);
    List<Food> findAllByUserId(Long userId);
    Food save(Food food);
    void deleteById(Long foodId);
}
