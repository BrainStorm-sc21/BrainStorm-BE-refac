package com.brainstrom.meokjang.admin.report.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AdminReportDetail {

    private Long reportId;
    private Long reportingUser;
    private String reportingUserName;
    private Long reportedUser;
    private String reportedUserName;
    private String reportText;
    private Boolean isHandled;
    private String handledBy;
    private LocalDateTime handledAt;
    private LocalDateTime createdAt;

    public AdminReportDetail(Long reportId, Long reportingUser, String reportingUserName,
                             Long reportedUser, String reportedUserName,
                             String reportText, Boolean isHandled, String handledBy,
                             LocalDateTime handledAt, LocalDateTime createdAt) {
        this.reportId = reportId;
        this.reportingUser = reportingUser;
        this.reportingUserName = reportingUserName;
        this.reportedUser = reportedUser;
        this.reportedUserName = reportedUserName;
        this.reportText = reportText;
        this.isHandled = isHandled;
        this.handledBy = handledBy;
        this.handledAt = handledAt;
        this.createdAt = createdAt;
    }
}
