package com.js.secondhandauction.core.member.util;

import com.js.secondhandauction.common.security.service.CustomUserDetails;
import com.js.secondhandauction.core.member.domain.Role;

public class TestMemberBuilder {
    public static CustomUserDetails buildUserDetails(long userNo) {
        return CustomUserDetails.builder()
                .userNo(userNo)
                .userId("userId")
                .role(Role.USER)
                .build();
    }
}
