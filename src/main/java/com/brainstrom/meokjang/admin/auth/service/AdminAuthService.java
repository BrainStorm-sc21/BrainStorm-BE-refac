package com.brainstrom.meokjang.admin.auth.service;

import com.brainstrom.meokjang.admin.auth.domain.Admin;
import com.brainstrom.meokjang.admin.auth.dto.AdminLoginForm;
import com.brainstrom.meokjang.admin.auth.repository.AdminRepository;
import com.brainstrom.meokjang.deal.repository.DealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AdminAuthService {

    private final AdminRepository adminRepository;
    private final DealRepository dealRepository;
    private final Integer DEAL_STATUS_COUNT = 2;

    @Autowired
    public AdminAuthService(AdminRepository adminRepository, DealRepository dealRepository) {
        this.adminRepository = adminRepository;
        this.dealRepository = dealRepository;
    }

    public boolean adminLogin(AdminLoginForm adminLoginForm) {
        System.out.println("adminName: " + adminLoginForm.getName());
        System.out.println("adminPw: " + adminLoginForm.getPw());
        Admin admin = adminRepository.findByAdminName(adminLoginForm.getName())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자 계정입니다."));
        return admin.getAdminPw().equals(adminLoginForm.getPw());
    }

    public List<Integer> getDealCountList() {
        List<Integer> dealCountList = new ArrayList<>();
        for (int i = 0; i <= DEAL_STATUS_COUNT; i++) {
            dealCountList.add(dealRepository.countByDealType(i));
        }
        System.out.println("dealCountList: " + dealCountList);
        return dealCountList;
    }
}
