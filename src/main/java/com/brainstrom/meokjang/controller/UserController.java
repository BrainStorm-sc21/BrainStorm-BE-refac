package com.brainstrom.meokjang.controller;

import com.brainstrom.meokjang.domain.User;
import com.brainstrom.meokjang.dto.LoginRequestDTO;
import com.brainstrom.meokjang.dto.SignupRequestDTO;
import com.brainstrom.meokjang.dto.UserDTO;
import com.brainstrom.meokjang.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users/create")
    public String create(@RequestBody SignupRequestDTO dto, BindingResult result) {

        if (result.hasErrors()) {
            throw new IllegalStateException("잘못된 입력입니다.");
        }
        try {
            User user = dto.toUser();
            userService.join(user);
            userService.validateDuplicateUser(dto.toUser());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
        return "redirect:/";
    }

    @PostMapping("/users/login")
    public String login(@RequestBody LoginRequestDTO dto, BindingResult result) {

        if (result.hasErrors()) {
            throw new IllegalStateException("잘못된 입력입니다.");
        }
        userService.login(dto);
        return "redirect:/";
    }
}
