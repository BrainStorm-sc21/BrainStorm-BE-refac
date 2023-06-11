package com.brainstrom.meokjang.admin.report.service;

import com.brainstrom.meokjang.admin.auth.domain.Admin;
import com.brainstrom.meokjang.admin.auth.dto.HandleReportForm;
import com.brainstrom.meokjang.admin.auth.repository.AdminRepository;
import com.brainstrom.meokjang.admin.report.dto.AdminReport;
import com.brainstrom.meokjang.admin.report.dto.AdminReportDetail;
import com.brainstrom.meokjang.common.domain.Report;
import com.brainstrom.meokjang.common.repository.ReportRepository;
import com.brainstrom.meokjang.user.domain.User;
import com.brainstrom.meokjang.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AdminReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    @Autowired
    public AdminReportService(ReportRepository reportRepository, UserRepository userRepository,
                              AdminRepository adminRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    public List<AdminReport> getReportList() {
        List<AdminReport> reportList = new ArrayList<>();
        reportRepository.findAll().forEach(report -> {
            User reportingUser = userRepository.findById(report.getReportingUser())
                    .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
            User reportedUser = userRepository.findById(report.getReportedUser())
                    .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
            AdminReport adminReport = AdminReport.builder()
                    .reportId(report.getReportId())
                    .reportingUserName(reportingUser.getUserName())
                    .reportedUserName(reportedUser.getUserName())
                    .reportText(report.getReportText())
                    .isHandled(report.getIsHandled())
                    .createdAt(report.getCreatedAt())
                    .build();
            reportList.add(adminReport);
        });
        return reportList;
    }

    public AdminReportDetail getReportDetail(Long reportId, String adminName) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("해당 신고가 없습니다."));
        User reportingUser = userRepository.findById(report.getReportingUser())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
        User reportedUser = userRepository.findById(report.getReportedUser())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
        Optional<Admin> handledBy = report.getHandledBy() != null ? adminRepository.findById(report.getHandledBy()) : null;
        return AdminReportDetail.builder()
                .reportId(report.getReportId())
                .reportingUser(reportingUser.getUserId())
                .reportingUserName(reportingUser.getUserName())
                .reportedUser(reportedUser.getUserId())
                .reportedUserName(reportedUser.getUserName())
                .reportText(report.getReportText())
                .isHandled(report.getIsHandled())
                .handledBy(handledBy != null ? handledBy.get().getAdminName() : null)
                .handledAt(report.getHandledAt())
                .createdAt(report.getCreatedAt())
                .currAdmin(adminName)
                .build();
    }

    public void handleReport(HandleReportForm handleReportForm) {
        Long reportId = handleReportForm.getReportId();
        System.out.println("reportId = " + reportId);
        String adminName = handleReportForm.getAdminName();
        System.out.println("adminName = " + adminName);
        int suspensionDays = handleReportForm.getSuspensionDays();
        System.out.println("suspensionDays = " + suspensionDays);
        if (suspensionDays < 0)
            throw new IllegalArgumentException("정지일은 0일 이상이어야 합니다.");

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("해당 신고가 없습니다."));
        Admin admin = adminRepository.findByAdminName(adminName)
                .orElseThrow(() -> new IllegalArgumentException("해당 관리자가 없습니다."));
        report.handleReport(admin, LocalDateTime.now());
        reportRepository.save(report);
        reportRepository.flush();
        if (suspensionDays > 0) {
            User reportedUser = userRepository.findById(reportRepository.findById(reportId).get().getReportedUser())
                    .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
            reportedUser.suspend(suspensionDays);
            userRepository.save(reportedUser);
            userRepository.flush();
        }
    }

    public boolean validate(String canAccessAdminPage) {
        Admin admin = adminRepository.findByAdminName(canAccessAdminPage)
                .orElseThrow(() -> new IllegalArgumentException("해당 관리자가 없습니다."));
        return admin != null;
    }
}
