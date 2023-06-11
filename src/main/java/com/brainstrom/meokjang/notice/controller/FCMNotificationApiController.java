package com.brainstrom.meokjang.notice.controller;

import com.brainstrom.meokjang.notice.dto.FCMNotificationRequestDto;
import com.brainstrom.meokjang.notice.service.FCMNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notice")
public class FCMNotificationApiController {

    private final FCMNotificationService fcmNotificationService;
    @Autowired
    public FCMNotificationApiController(FCMNotificationService fcmNotificationService) {
        this.fcmNotificationService = fcmNotificationService;
    }

    @PostMapping()
    public String sendNotificationByToken(@RequestBody FCMNotificationRequestDto requestDto) {
        return fcmNotificationService.sendNotificationByToken(requestDto);
    }
}
