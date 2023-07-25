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

    @Column(name = "report_from", nullable = false)
    private Long reportFrom;

    @Column(name = "report_to", nullable = false)
    private Long reportTo;

    @Column(name = "report_content", length = 300)
    private String reportContent;

    @Column(name = "is_handled", nullable = false)
    private Boolean isHandled;

    @Column(name = "handled_by")
    private Long handledBy;

    @Column(name = "handled_date")
    private LocalDateTime handledDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Report(Long reportFrom, Long reportTo, String reportContent) {
        this.reportFrom = reportFrom;
        this.reportTo = reportTo;
        this.reportContent = reportContent;
        this.isHandled = false;
    }

    public void handleReport(Admin AdminName, LocalDateTime now) {
        this.isHandled = true;
        this.handledBy = AdminName.getAdminId();
        this.handledDate = now;
    }
}
