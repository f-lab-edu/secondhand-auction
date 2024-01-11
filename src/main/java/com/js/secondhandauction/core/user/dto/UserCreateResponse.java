package com.js.secondhandauction.core.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserCreateResponse {
    private String id;
    private String name;

    @Builder
    public UserCreateResponse(String id,
                              String name
    ) {
        this.id = id;
        this.name = name;
    }
}
