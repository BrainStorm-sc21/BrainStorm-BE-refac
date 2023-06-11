package com.brainstrom.meokjang.user.controller;

import com.brainstrom.meokjang.common.dto.response.ApiResponse;
import com.brainstrom.meokjang.user.dto.request.LoginRequest;
import com.brainstrom.meokjang.user.dto.request.SignupRequest;
import com.brainstrom.meokjang.user.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/users/create")
    public ResponseEntity<ApiResponse> createUser(@RequestBody SignupRequest dto, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(400, "회원가입 실패", result.getAllErrors()));
        } // 이 부분 GlobalExceptionHandler로 빼기
        try {
            ApiResponse res = new ApiResponse(200, "회원가입 성공", authService.join(dto));
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(400, "회원가입 실패", e.getMessage()));
        }
    }

    @PostMapping("/users/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest dto, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(400, "로그인 실패", result.getAllErrors()));
        } // 이 부분 GlobalExceptionHandler로 빼기
        try {
            ApiResponse res = new ApiResponse(200, "로그인 성공", authService.login(dto));
            return ResponseEntity.ok(res);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(401, "로그인 실패", e.getMessage()));
        }
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {

        try {
            authService.withdraw(userId);
            ApiResponse res = new ApiResponse(200, "회원탈퇴 성공", null);
            return ResponseEntity.ok(res);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(401, "회원탈퇴 실패", e.getMessage()));
        }
    }
}
