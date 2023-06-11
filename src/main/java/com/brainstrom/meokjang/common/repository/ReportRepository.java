package com.brainstrom.meokjang.common.repository;

import com.brainstrom.meokjang.common.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
