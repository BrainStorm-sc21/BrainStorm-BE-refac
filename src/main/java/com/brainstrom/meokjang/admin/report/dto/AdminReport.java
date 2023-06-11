package com.brainstrom.meokjang.admin.report.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AdminReport {

    private final Long reportId;
    private final String reportingUserName;
    private final String reportedUserName;
    private final String reportText;
    private final Boolean isHandled;
    private final LocalDateTime createdAt;

    public AdminReport(Long reportId, String reportingUserName, String reportedUserName,
                       String reportText, Boolean isHandled, LocalDateTime createdAt) {
        this.reportId = reportId;
        this.reportingUserName = reportingUserName;
        this.reportedUserName = reportedUserName;
        this.reportText = reportText;
        this.isHandled = isHandled;
        this.createdAt = createdAt;
    }
}
