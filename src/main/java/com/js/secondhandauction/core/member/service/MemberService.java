package com.js.secondhandauction.core.member.service;

import com.js.secondhandauction.common.exception.ErrorCode;
import com.js.secondhandauction.core.member.domain.Member;
import com.js.secondhandauction.core.member.dto.MemberCreateRequest;
import com.js.secondhandauction.core.member.dto.MemberCreateResponse;
import com.js.secondhandauction.core.member.dto.MemberGetResponse;
import com.js.secondhandauction.core.member.exception.CannotTotalBalanceMinusException;
import com.js.secondhandauction.core.member.exception.NotFoundMemberException;
import com.js.secondhandauction.core.member.exception.MemberException;
import com.js.secondhandauction.core.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    final MemberRepository memberRepository;

    final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    public MemberCreateResponse createMember(MemberCreateRequest memberCreateRequest) {
        checkUserIdValidity(memberCreateRequest);

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
     * 회원 조회
     */
    @Cacheable(value = "member", key = "#userId")
    public MemberGetResponse getMemberByUserId(String userId) {
        Member member = memberRepository.findByUserId(userId).orElseThrow(NotFoundMemberException::new);
        return MemberGetResponse.of(member);
    }

    /**
     * 회원 조회
     */
    public MemberGetResponse getMemberByUniqId(long id) {
        Member member = memberRepository.findByUniqId(id).orElseThrow(NotFoundMemberException::new);
        return MemberGetResponse.of(member);
    }


    /**
     * 회원 가진금액 더하기 Username 으로
     */
    public void updateMemberTotalBalanceByUserId(String userId, int totalBalance) {
        if (memberRepository.findByUserId(userId).orElseThrow(NotFoundMemberException::new).getTotalBalance() + totalBalance > 0) {
            memberRepository.updateTotalBalance(userId, totalBalance);
        } else {
            throw new CannotTotalBalanceMinusException();
        }
    }

    /**
     * 회원 가진금액 더하기 id로
     */
    public void updateMemberTotalBalanceByUniqId(long id, int totalBalance) {
        Member user = memberRepository.findByUniqId(id).orElseThrow(NotFoundMemberException::new);

        if (user.getTotalBalance() + totalBalance > 0) {
            memberRepository.updateTotalBalance(user.getUserId(), totalBalance);
        } else {
            throw new CannotTotalBalanceMinusException();
        }
    }

    /**
     * 회원 중복 체크
     */
    private void checkUserIdValidity(MemberCreateRequest memberCreateRequest) {
        if (memberRepository.findByUserId(memberCreateRequest.userId()).isPresent()) {
            throw new MemberException(ErrorCode.ALREADY_EXIST_USER);
        }
    }
}
