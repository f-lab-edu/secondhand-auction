package com.js.secondhandauction.core.user.dto;

import com.js.secondhandauction.core.user.domain.User;

public record UserCreateRequest(
        String id,
        String name,
        String password
) {
    public User toEntity() {
        return User.builder()
                .id(id)
                .name(name)
                .password(password)
                .build();
    }

}
