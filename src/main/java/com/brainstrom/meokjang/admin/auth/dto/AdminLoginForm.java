package com.brainstrom.meokjang.admin.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminLoginForm {

    private String name;
    private String pw;

    public AdminLoginForm(String name, String pw) {
        this.name = name;
        this.pw = pw;
    }
}
