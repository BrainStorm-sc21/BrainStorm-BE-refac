package com.brainstrom.meokjang.admin.report.service;

import com.brainstrom.meokjang.admin.auth.domain.Admin;
import com.brainstrom.meokjang.admin.auth.repository.AdminRepository;
import com.brainstrom.meokjang.admin.report.dto.AdminReport;
import com.brainstrom.meokjang.admin.report.dto.AdminReportDetail;
import com.brainstrom.meokjang.common.repository.ReportRepository;
import com.brainstrom.meokjang.user.domain.User;
import com.brainstrom.meokjang.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.LocalTime.now;

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

    public AdminReportDetail getReportDetail(Long reportId) {
        return reportRepository.findById(reportId)
                .map(report -> {
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
                            .build();
                })
                .orElseThrow(() -> new IllegalArgumentException("해당 신고가 없습니다."));
    }

    public void handleReport(Long reportId, int suspensionDays) {
//        reportRepository.findById(reportId)
//                .map(report -> reportRepository.save(report.handleReport(adminRepository.findById().get(), now())))
//                .orElseThrow(() -> new IllegalArgumentException("해당 신고가 없습니다."));
//        User reportedUser = userRepository.findById(reportRepository.findById(reportId).get().getReportedUser())
//                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
//        reportedUser.setIsSuspended(true);
//        reportedUser.setSuspendedUntil(suspensionDays);
    }

    public boolean validate(String canAccessAdminPage) {
        Admin admin = adminRepository.findByAdminName(canAccessAdminPage)
                .orElseThrow(() -> new IllegalArgumentException("해당 관리자가 없습니다."));
        return admin != null;
    }
}
