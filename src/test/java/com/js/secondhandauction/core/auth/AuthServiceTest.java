package com.js.secondhandauction.core.auth;

import com.js.secondhandauction.common.exception.ErrorCode;
import com.js.secondhandauction.core.auth.service.AuthService;
import com.js.secondhandauction.core.member.domain.Role;
import com.js.secondhandauction.core.member.dto.MemberCreateRequest;
import com.js.secondhandauction.core.member.exception.MemberException;
import com.js.secondhandauction.core.member.repository.MemberRepository;
import com.js.secondhandauction.core.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class AuthServiceTest {
    @Autowired
    private AuthService authService;

    @Autowired
    private MemberService memberService;


    @Autowired
    MemberRepository memberRepository;

    final String TEST_ID = "test";
    final String TEST_PW = "p@ssw0rd";

    @BeforeEach
    void setup() {
        memberService.createMember(new MemberCreateRequest(TEST_ID, "test", TEST_PW, Role.USER));
    }

    @Test
    @DisplayName("로그인")
    void login() {
        //when
        Authentication authentication = authService.login(TEST_ID, TEST_PW);

        //then
        assertTrue(authentication.isAuthenticated());

        //given
        final String NOT_USERID = "test2";
        final String NOT_USERPW = "test2";

        //then
        assertThrows(MemberException.class, () -> authService.login(NOT_USERID, TEST_PW), ErrorCode.NOT_FOUND_MEMBER.getMessage());

        final String password2 = "test2";

        //then
        assertThrows(MemberException.class, () -> authService.login(TEST_ID, NOT_USERPW), ErrorCode.INVALID_PASSWORD.getMessage());
    }

}
