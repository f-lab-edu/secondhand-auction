package com.js.secondhandauction.common.security;

import com.js.secondhandauction.common.exception.ErrorCode;
import com.js.secondhandauction.common.security.service.CustomUserDetails;
import com.js.secondhandauction.core.member.domain.Member;
import com.js.secondhandauction.core.member.exception.MemberException;
import com.js.secondhandauction.core.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

        Optional<Member> member = memberRepository.findByUserId(principal.getUsername());

        if (!passwordEncoder.matches(authentication.getCredentials().toString(), member.get().getPassword())) {
            throw new MemberException(ErrorCode.INVALID_PASSWORD);
        }

        return new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}