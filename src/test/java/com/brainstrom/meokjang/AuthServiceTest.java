package com.brainstrom.meokjang;

import com.brainstrom.meokjang.user.dto.request.LoginRequest;
import com.brainstrom.meokjang.user.dto.request.SignupRequest;
import com.brainstrom.meokjang.user.repository.UserRepository;
import com.brainstrom.meokjang.user.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AuthServiceTest {

    private final UserRepository userRepository;
    private final AuthService authService;

    @Autowired
    public AuthServiceTest(UserRepository userRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 테스트")
    void join() {
        //given
        SignupRequest signupRequest = SignupRequest.builder()
                .userName("test")
                .phoneNumber("01012345678")
                .location("test")
                .latitude(0.0)
                .longitude(0.0)
                .gender(1)
                .build();

        //when
        Long savedId = authService.join(signupRequest).getUserId();
        //then
        assertNotNull(savedId);
        assertEquals(userRepository.findByPhoneNumber("01012345678"), userRepository.findByUserId(savedId));
    }

    @Test
    @DisplayName("로그인 테스트 - 전화번호")
    void login1() {
        //given
        SignupRequest signupRequest = SignupRequest.builder()
                .userName("test")
                .phoneNumber("01012345678")
                .location("test")
                .latitude(0.0)
                .longitude(0.0)
                .gender(1)
                .build();
        LoginRequest loginRequest = LoginRequest.builder()
                .phoneNumber("01012345678")
                .build();

        //when
        Long savedId = authService.join(signupRequest).getUserId();
        Long loginId = authService.login(loginRequest).getUserId();

        //then
        assertEquals(savedId, loginId);
    }

    @Test
    @DisplayName("로그인 테스트 - SNS")
    void login2() {
        //given
        SignupRequest signupRequest = SignupRequest.builder()
                .userName("test")
                .snsType("naver")
                .snsKey("test")
                .location("test")
                .latitude(0.0)
                .longitude(0.0)
                .gender(1)
                .build();
        LoginRequest loginRequest = LoginRequest.builder()
                .snsType("naver")
                .snsKey("test")
                .build();

        //when
        Long savedId = authService.join(signupRequest).getUserId();
        Long loginId = authService.login(loginRequest).getUserId();

        //then
        assertEquals(savedId, loginId);
    }
}
