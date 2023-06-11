package com.brainstrom.meokjang.common.domain;

import com.brainstrom.meokjang.admin.auth.domain.Admin;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(schema = "REPORT")
@Getter
@NoArgsConstructor
public class Report {

    @Id @Column(name = "report_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @Column(name = "reporting_user", nullable = false)
    private Long reportingUser;

    @Column(name = "reported_user", nullable = false)
    private Long reportedUser;

    @Column(name = "report_text", length = 300)
    private String reportText;

    @Column(name = "is_handled", nullable = false)
    private Boolean isHandled;

    @Column(name = "handled_by")
    private Long handledBy;

    @Column(name = "handled_at")
    private LocalDateTime handledAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Report(Long reportingUser, Long reportedUser, String reportText) {
        this.reportingUser = reportingUser;
        this.reportedUser = reportedUser;
        this.reportText = reportText;
        this.isHandled = false;
    }

    public void handleReport(Admin AdminName, LocalDateTime now) {
        this.isHandled = true;
        this.handledBy = AdminName.getAdminId();
        this.handledAt = now;
    }
}
