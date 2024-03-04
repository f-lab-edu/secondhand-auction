package com.js.secondhandauction.core.member.service;

import com.js.secondhandauction.common.exception.ErrorCode;
import com.js.secondhandauction.core.member.domain.Member;
import com.js.secondhandauction.core.member.dto.MemberCreateRequest;
import com.js.secondhandauction.core.member.dto.MemberCreateResponse;
import com.js.secondhandauction.core.member.dto.MemberGetResponse;
import com.js.secondhandauction.core.member.exception.MemberException;
import com.js.secondhandauction.core.member.exception.NotFoundMemberException;
import com.js.secondhandauction.core.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MemberService {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    public MemberCreateResponse createMember(MemberCreateRequest memberCreateRequest) {
        checkUserIdValidity(memberCreateRequest);

        checkPasswordValidity(memberCreateRequest.password());

        Member member = Member.builder()
                .userId(memberCreateRequest.userId())
                .nickname(memberCreateRequest.nickname())
                .password(passwordEncoder.encode(memberCreateRequest.password()))
                .role(memberCreateRequest.role())
                .build();

        memberRepository.create(member);

        return MemberCreateResponse.of(member);
    }

    /**
     * 회원 조회 userid 로
     */
    @Cacheable(key = "#userId", value = "MEMBER_USERID")
    public MemberGetResponse getMemberByUserId(String userId) {
        Member member = memberRepository.findByUserId(userId).orElseThrow(NotFoundMemberException::new);
        return MemberGetResponse.of(member);
    }

    /**
     * 회원 조회 userNo로
     */
    @Cacheable(key = "#id", value = "MEMBER_USERNO")
    public MemberGetResponse getMemberByUserNo(long id) {
        Member member = memberRepository.findByUserNo(id).orElseThrow(NotFoundMemberException::new);
        return MemberGetResponse.of(member);
    }

    /**
     * 로그인을 위한 회원 조회 userid 로
     */
    @Cacheable(key = "#userId", value = "MEMBER_LOGIN")
    public Member getMemberForLogin(String userId) {
        return memberRepository.findByUserId(userId).orElseThrow(NotFoundMemberException::new);
    }


    /**
     * 회원 가진금액 더하기 UserId 로
     */
    public void updateMemberTotalBalanceByUserId(String userId, int totalBalance) {
        Member member = memberRepository.findByUserId(userId).orElseThrow(NotFoundMemberException::new);

        evictCache(member);

        if (member.getTotalBalance() + totalBalance > 0) {
            memberRepository.updateTotalBalance(userId, totalBalance);
        } else {
            throw new MemberException(ErrorCode.CANNOT_TOTALBALANCE_MINUS);
        }
    }

    /**
     * 회원 가진금액 더하기 userNo로
     */
    public void updateMemberTotalBalanceByUserNo(long id, int totalBalance) {
        Member member = memberRepository.findByUserNo(id).orElseThrow(NotFoundMemberException::new);

        evictCache(member);

        if (member.getTotalBalance() + totalBalance > 0) {
            memberRepository.updateTotalBalance(member.getUserId(), totalBalance);
        } else {
            throw new MemberException(ErrorCode.CANNOT_TOTALBALANCE_MINUS);
        }
    }

    /**
     * 회원 비밀번호 변경
     */
    public void updateMemberPassword(String userId, String password) {
        Member member = memberRepository.findByUserId(userId).orElseThrow(NotFoundMemberException::new);

        evictCache(member);

        checkPasswordValidity(password);

        if (passwordEncoder.matches(password, member.getPassword())) {
            throw new MemberException(ErrorCode.SAME_PASSWORD);
        }

        memberRepository.updatePassword(userId, passwordEncoder.encode(password));
    }

    /**
     * 회원 중복 체크
     */
    private void checkUserIdValidity(MemberCreateRequest memberCreateRequest) {
        if (memberRepository.findByUserId(memberCreateRequest.userId()).isPresent()) {
            throw new MemberException(ErrorCode.ALREADY_EXIST_MEMBER);
        }
    }

    private void checkPasswordValidity(String password) {
        String regex = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*()]).{8,20}$";
        if (!password.matches(regex)) {
            throw new MemberException(ErrorCode.PASSWORD_RULE);
        }
    }

    @Caching(
            evict = {
                    @CacheEvict(key = "#member.userId", value = "MEMBER_USERID"),
                    @CacheEvict(key = "#member.userNo", value = "MEMBER_USERNO")
            }
    )
    public void evictCache(Member member) {
    }
}
