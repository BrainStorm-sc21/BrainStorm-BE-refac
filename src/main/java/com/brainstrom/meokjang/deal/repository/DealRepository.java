package com.brainstrom.meokjang.deal.repository;

import com.brainstrom.meokjang.deal.domain.Deal;
import com.brainstrom.meokjang.deal.dto.response.DealInfoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DealRepository extends JpaRepository<Deal, Long> {
    @Query(value = "SELECT * FROM deal d WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(d.latitude)) * cos(radians(d.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(d.latitude)))) <= 1", nativeQuery = true)
    List<Deal> findAroundDealList(@Param("latitude") Double latitude, @Param("longitude") Double longitude);
}
