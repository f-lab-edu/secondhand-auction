package com.js.secondhandauction.core.member.dto;

import com.js.secondhandauction.core.member.domain.Member;
import com.js.secondhandauction.core.member.domain.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberCreateResponse {
    private String userId;
    private String nickname;
    private Role role;

    public static MemberCreateResponse of(Member member) {
        return MemberCreateResponse.builder()
                .userId(member.getUserId())
                .nickname(member.getNickname())
                .role(member.getRole())
                .build();
    }
}
