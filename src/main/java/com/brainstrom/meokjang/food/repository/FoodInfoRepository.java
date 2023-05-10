package com.brainstrom.meokjang.food.repository;

import com.brainstrom.meokjang.food.domain.FoodInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FoodInfoRepository extends JpaRepository<FoodInfo, Long> {

    Optional<FoodInfo> findByInfoId(Long id);
    List<FoodInfo> findByInfoName(String name);
}
