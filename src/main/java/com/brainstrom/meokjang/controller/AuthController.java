package com.brainstrom.meokjang.controller;

import com.brainstrom.meokjang.domain.User;
import com.brainstrom.meokjang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users/create")
    public String create(@RequestBody UserForm userForm) {
        User user = new User(userForm);
        userService.join(user);

        return "redirect:/";
    }

    @PostMapping("/users/login")
    public String login() {
        return "redirect:/";
    }
}
