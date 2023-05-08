package com.brainstrom.meokjang;

import com.brainstrom.meokjang.user.dto.request.LoginRequest;
import com.brainstrom.meokjang.user.dto.request.SignupRequest;
import com.brainstrom.meokjang.user.repository.UserRepository;
import com.brainstrom.meokjang.user.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class AuthServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthService authService;

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
    }

    @Test
    void join() {
        //given
        SignupRequest signupRequest = SignupRequest.builder()
                .userName("test")
                .phoneNumber("01012345678")
                .location("test")
                .latitude(0.0)
                .longitude(0.0)
                .gender(1)
                .reliability((float)50)
                .build();

        //when
        Long savedId = authService.join(signupRequest).getUserId();

        //then
        assertEquals(userRepository.findByPhoneNumber("01012345678"), userRepository.findById(savedId));
    }

    @Test
    void login1() {
        //given
        SignupRequest signupRequest = SignupRequest.builder()
                .userName("test")
                .phoneNumber("01012345678")
                .location("test")
                .latitude(0.0)
                .longitude(0.0)
                .gender(1)
                .reliability((float)50)
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
    void login2() throws Exception {
        //given
        SignupRequest signupRequest = SignupRequest.builder()
                .userName("test")
                .snsType("naver")
                .snsKey("test")
                .location("test")
                .latitude(0.0)
                .longitude(0.0)
                .gender(1)
                .reliability((float)50)
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
