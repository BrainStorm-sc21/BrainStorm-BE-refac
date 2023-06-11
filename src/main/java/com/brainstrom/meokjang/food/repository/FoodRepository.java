package com.brainstrom.meokjang.food.repository;

import com.brainstrom.meokjang.food.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FoodRepository extends JpaRepository<Food, Long>{

    Optional<Food> findById(Long foodId);
    List<Food> findAllByUserId(Long userId);
    List<Food> findAllByUserIdOrderByExpireDateAsc(Long userId);
    Food save(Food food);
    void deleteById(Long foodId);
}
