package com.js.secondhandauction.core.user.dto;

import com.js.secondhandauction.core.user.domain.User;

public record UserCreateRequest(
        String username,
        String nickname,
        String password
) {
    public User toEntity() {
        return User.builder()
                .username(username)
                .nickname(nickname)
                .password(password)
                .build();
    }

}
