package com.brainstrom.meokjang;

import com.brainstrom.meokjang.user.dto.request.SignupRequest;
import com.brainstrom.meokjang.user.dto.response.UserInfoResponse;
import com.brainstrom.meokjang.user.repository.UserRepository;
import com.brainstrom.meokjang.user.service.AuthService;
import com.brainstrom.meokjang.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class UserServiceMockTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("mock을 이용한 회원정보 조회 테스트")
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

        Mockito.when(userRepository.save(Mockito.any())).thenReturn(signupRequest.toEntity());
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(signupRequest.toEntity()));

        Long savedId = authService.join(signupRequest).getUserId();

        //when
        UserInfoResponse userInfoResponse = userService.getUserInfo(savedId);

        //then
        assertNotNull(userInfoResponse);
        assertEquals(userInfoResponse.getUserName(), signupRequest.getUserName());
    }
}
