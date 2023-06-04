package com.brainstrom.meokjang;

import com.brainstrom.meokjang.user.domain.User;
import com.brainstrom.meokjang.user.dto.request.SignupRequest;
import com.brainstrom.meokjang.user.dto.response.UserInfoResponse;
import com.brainstrom.meokjang.user.repository.UserRepository;
import com.brainstrom.meokjang.user.service.AuthService;
import com.brainstrom.meokjang.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class UserServiceTest {

    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public UserServiceTest(UserRepository userRepository, UserService userService, AuthService authService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.authService = authService;
    }

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 정보 조회 테스트")
    void getUserInfo() {
        //given
        SignupRequest signupRequest = SignupRequest.builder()
                .userName("test")
                .phoneNumber("01012345678")
                .location("test")
                .latitude(0.0)
                .longitude(0.0)
                .gender(1)
                .build();

        Long savedId = authService.join(signupRequest).getUserId();

        //when
        UserInfoResponse userInfoResponse = userService.getUserInfo(savedId);

        //then
        assertNotNull(userInfoResponse);
        assertEquals(savedId, userInfoResponse.getUserId());
    }

    @Test
    @DisplayName("회원 정보 수정 테스트")
    void updateUserInfo() {
        //given
        SignupRequest signupRequest = SignupRequest.builder()
                .userName("test")
                .phoneNumber("01012345678")
                .location("test")
                .latitude(0.0)
                .longitude(0.0)
                .gender(1)
                .build();

        Long savedId = authService.join(signupRequest).getUserId();

        //when
        UserInfoResponse userInfoResponse = userService.updateUserInfo(savedId, "test2");
        User user = userRepository.findByUserId(savedId)
                .orElse(null);

        //then
        assertNotNull(userInfoResponse);
        assertEquals(user.getUserName(), "test2");
    }
}
