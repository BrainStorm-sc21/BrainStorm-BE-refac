package com.brainstrom.meokjang.deal.repository;

import com.brainstrom.meokjang.deal.domain.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DealRepository extends JpaRepository<Deal, Long> {

    @Query(value = "SELECT * FROM Deal d WHERE (6371000 * acos(cos(radians(:latitude)) * cos(radians(d.latitude)) * cos(radians(d.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(d.latitude)))) <= 1000", nativeQuery = true)
    List<Deal> findAroundDealList(@Param("latitude") Double latitude, @Param("longitude") Double longitude);

    List<Deal> findByUserId(Long userId);
}
