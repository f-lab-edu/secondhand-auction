package com.js.secondhandauction.core.member.dto;

import com.js.secondhandauction.core.member.domain.Member;
import com.js.secondhandauction.core.member.domain.Role;

public record MemberCreateRequest(
        String userId,
        String nickname,
        String password,
        Role role
) {
    public Member toEntity() {
        return Member.builder()
                .userId(userId)
                .nickname(nickname)
                .password(password)
                .role(role)
                .build();
    }

}
