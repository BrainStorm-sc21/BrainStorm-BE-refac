package com.brainstrom.meokjang.user.dto.request;

import com.brainstrom.meokjang.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupRequest {
    private String userName;
    private String phoneNumber;
    private String snsType;
    private String snsKey;
    private String location;
    private Double latitude;
    private Double longitude;
    private Integer gender;
    private String firebaseToken;

    public User toEntity() {
        return User.builder()
                .userName(userName)
                .phoneNumber(phoneNumber)
                .snsType(snsType)
                .snsKey(snsKey)
                .location(location)
                .latitude(latitude)
                .longitude(longitude)
                .gender(gender)
                .reliability(50F)
                .firebaseToken(firebaseToken)
                .build();
    }
}
