package com.js.secondhandauction.core.auth;

import com.js.secondhandauction.common.security.CustomAuthenticationProvider;
import com.js.secondhandauction.common.security.service.CustomUserDetails;
import com.js.secondhandauction.common.security.service.CustomUserDetailsService;
import com.js.secondhandauction.core.auth.service.AuthService;
import com.js.secondhandauction.core.member.domain.Member;
import com.js.secondhandauction.core.member.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;


@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @InjectMocks
    private AuthService authService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private CustomAuthenticationProvider customAuthenticationProvider;

    private CustomUserDetails userDetails;
    final String TEST_ID = "test";
    final String TEST_PW = "p@ssw0rd";

    @BeforeEach
    void setup() {
        Member member = Member.builder()
                .userId(TEST_ID)
                .nickname("Test Name")
                .password(passwordEncoder.encode(TEST_PW))
                .role(Role.USER)
                .build();

        userDetails = CustomUserDetails.of(member);

    }

    @Test
    @DisplayName("로그인")
    void login() {
        //when
        Mockito.when(customUserDetailsService.loadUserByUsername(TEST_ID)).thenReturn(userDetails);
        Mockito.when(customAuthenticationProvider.authenticate(Mockito.any(Authentication.class))).thenReturn(Mockito.mock(Authentication.class));

        authService.login(TEST_ID, TEST_PW);
    }

}
