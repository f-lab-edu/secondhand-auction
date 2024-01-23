package com.js.secondhandauction.common.security.service;


import com.js.secondhandauction.core.member.domain.Member;
import com.js.secondhandauction.core.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return CustomUserDetails.of(memberRepository.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException(userId)));
    }
}
