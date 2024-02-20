package com.js.secondhandauction.core.auth.service;

import com.js.secondhandauction.common.security.service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    AuthenticationProvider AuthenticationProvider;

    @Autowired
    UserDetailsService userDetailsService;

    public Authentication login(String userId, String password) {
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(userId);

        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, password);

        // 인증 정보 반환
        return AuthenticationProvider.authenticate(auth);
    }


}
