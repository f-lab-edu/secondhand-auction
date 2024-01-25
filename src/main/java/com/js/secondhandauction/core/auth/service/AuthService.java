package com.js.secondhandauction.core.auth.service;

import com.js.secondhandauction.common.exception.ErrorCode;
import com.js.secondhandauction.common.security.service.CustomUserDetails;
import com.js.secondhandauction.core.member.exception.MemberException;
import com.js.secondhandauction.core.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    AuthenticationManagerBuilder managerBuilder;

    public Authentication login(String userId, String password) {
        CustomUserDetails userDetails = CustomUserDetails.of(memberRepository.findByUserId(userId).orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER)));

        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, password);

        try {
            // 인증 정보 반환
            return managerBuilder.getObject().authenticate(auth);
        } catch (AuthenticationException e) {
            throw new MemberException(ErrorCode.INVALID_PASSWORD);
        }
    }


}
