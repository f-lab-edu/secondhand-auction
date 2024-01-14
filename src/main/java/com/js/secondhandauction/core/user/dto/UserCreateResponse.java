package com.js.secondhandauction.core.user.dto;

import com.js.secondhandauction.core.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCreateResponse {
    private String username;
    private String nickname;

    public static UserCreateResponse of(User user) {
        return UserCreateResponse.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .build();
    }
}
