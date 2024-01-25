package com.js.secondhandauction.core.auth.dto;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String userId;
    private String password;
}

