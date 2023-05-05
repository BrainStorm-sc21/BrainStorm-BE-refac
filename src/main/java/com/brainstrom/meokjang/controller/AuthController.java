package com.brainstrom.meokjang.controller;

import com.brainstrom.meokjang.domain.User;
import com.brainstrom.meokjang.dto.UserDto;
import com.brainstrom.meokjang.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final UserService userService;

    @PostMapping("/users/create")
    public String create(@RequestBody UserDto info) {
        User user = new User();
        user.setUserName(info.getUserName());
        user.setPhoneNumber(info.getPhoneNumber());
        user.setSnsType(info.getSnsType());
        user.setSnsKey(info.getSnsKey());
        user.setLocation(info.getLocation());
        user.setLatitude(info.getLatitude());
        user.setLongitude(info.getLongitude());
        user.setGender(info.getGender());
        user.setReliability((float)50);
        userService.join(user);

        return "redirect:/";
    }

    @PostMapping("/users/login")
    public String login() {
        return "redirect:/";
    }
}
