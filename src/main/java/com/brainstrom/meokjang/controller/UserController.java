package com.brainstrom.meokjang.controller;

import com.brainstrom.meokjang.domain.User;
import com.brainstrom.meokjang.dto.LoginRequestDTO;
import com.brainstrom.meokjang.dto.SignupRequestDTO;
import com.brainstrom.meokjang.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users/create")
    public ResponseEntity<Object> create(@RequestBody SignupRequestDTO dto, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }
        try {
            User user = dto.toUser();
            userService.join(user);
            userService.validateDuplicateUser(dto.toUser());
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/users/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequestDTO dto, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }
        try {
            User user = userService.login(dto);
            return ResponseEntity.ok(user);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
