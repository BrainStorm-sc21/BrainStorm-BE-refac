package com.brainstrom.meokjang.user.service;

import com.brainstrom.meokjang.user.domain.User;
import com.brainstrom.meokjang.user.dto.response.UserInfoResponse;
import com.brainstrom.meokjang.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserInfoResponse getUserInfo(Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 유저입니다."));
        return new UserInfoResponse(user.getUserId(), user.getUserName(), user.getLocation(), user.getLatitude(),
                user.getLongitude(), user.getReliability(), user.getStopUntil());
    }

    public UserInfoResponse updateUserInfo(Long userId, String userName) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 유저입니다."));
        int result = userRepository.updateUserById(userId, userName);
        if (result != 1) {
            throw new IllegalStateException("유저 정보 수정에 실패하였습니다.");
        }
        User updateUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 유저입니다."));
        return new UserInfoResponse(updateUser.getUserId(), userName, updateUser.getLocation(),
                updateUser.getLatitude(), updateUser.getLongitude(), updateUser.getReliability(), updateUser.getStopUntil());
    }

    public List<User> getUserList() {
        return userRepository.findAll();
    }
}
