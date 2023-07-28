package com.brainstrom.meokjang.common.domain;

import com.brainstrom.meokjang.admin.auth.domain.Admin;
import com.brainstrom.meokjang.user.domain.User;
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

    @ManyToOne
    @JoinColumn(name = "report_from", referencedColumnName = "user_id", nullable = false)
    private User reportFrom;

    @ManyToOne
    @JoinColumn(name = "report_to", referencedColumnName = "user_id", nullable = false)
    private User reportTo;

    @Column(name = "report_content", length = 300, nullable = false)
    private String reportContent;

    @Column(name = "is_handled", nullable = false)
    private Boolean isHandled;

    @ManyToOne
    @JoinColumn(name = "handled_by", referencedColumnName = "admin_id")
    private Admin handledBy;

    @Column(name = "handled_date")
    private LocalDateTime handledDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Report(User reportFrom, User reportTo, String reportContent) {
        this.reportFrom = reportFrom;
        this.reportTo = reportTo;
        this.reportContent = reportContent;
        this.isHandled = false;
    }

    public void handleReport(Admin admin, LocalDateTime now) {
        this.isHandled = true;
        this.handledBy = admin;
        this.handledDate = now;
    }
}
