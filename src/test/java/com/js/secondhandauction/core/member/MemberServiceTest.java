package com.js.secondhandauction.core.member;

import com.js.secondhandauction.common.exception.ErrorCode;
import com.js.secondhandauction.core.member.domain.Member;
import com.js.secondhandauction.core.member.domain.Role;
import com.js.secondhandauction.core.member.dto.MemberCreateRequest;
import com.js.secondhandauction.core.member.dto.MemberCreateResponse;
import com.js.secondhandauction.core.member.dto.MemberGetResponse;
import com.js.secondhandauction.core.member.exception.MemberException;
import com.js.secondhandauction.core.member.exception.NotFoundMemberException;
import com.js.secondhandauction.core.member.repository.MemberRepository;
import com.js.secondhandauction.core.member.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Member member;

    private MemberCreateRequest memberCreateRequest;

    final String CORRECT_PASSWORD = "P@SSW0RD";

    final String CORRECT_PASSWORD2 = "p@ssw0rd";

    final String INCORRECT_PASSWORD = "12345678";

    @BeforeEach
    void setup() {
        member = Member.builder()
                .userId("Test User")
                .nickname("Test Name")
                .password(passwordEncoder.encode(CORRECT_PASSWORD))
                .role(Role.USER)
                .build();

    }


    @Test
    @DisplayName("멤버를 생성한다")
    void testCreateMember() {
        memberCreateRequest = new MemberCreateRequest("Test User", "Test Name", CORRECT_PASSWORD, Role.USER);

        MemberCreateResponse createdMember = memberService.createMember(memberCreateRequest);

        assertNotNull(createdMember);
        Assertions.assertThat("Test User").isEqualTo(createdMember.getUserId());
        Assertions.assertThat("Test Name").isEqualTo(createdMember.getNickname());

        //ID가 중복되어 회원가입에 실패
        when(memberRepository.findByUserId("Test User")).thenReturn(Optional.ofNullable(member));

        assertThrows(MemberException.class,
                () -> memberService.createMember(memberCreateRequest), ErrorCode.ALREADY_EXIST_MEMBER.getMessage());

        //비밀번호가 조건에 맞지않아 회원가입에 실패
        memberCreateRequest = new MemberCreateRequest("Test User2", "Test Name", INCORRECT_PASSWORD, Role.USER);

        assertThrows(MemberException.class,
                () -> memberService.createMember(memberCreateRequest), ErrorCode.PASSWORD_RULE.getMessage());
    }

    @Test
    @DisplayName("멤버를 조회한다")
    void testGetMember() {
        //정상 조회
        when(memberRepository.findByUserId("10")).thenReturn(Optional.ofNullable(member));

        MemberGetResponse getMember = memberService.getMemberByUserId("10");

        Assertions.assertThat("Test User").isEqualTo(getMember.getUserId());
        Mockito.verify(memberRepository, times(1)).findByUserId(anyString());

        //조회 실패
        when(memberRepository.findByUserId("11")).thenReturn(Optional.empty());

        assertThrows(NotFoundMemberException.class,
                () -> memberService.getMemberByUserId("11"));
    }

    @Test
    @DisplayName("유저의 자금을 변경한다")
    void testUpdateTotalBalance() {
        //정상 자금 변경
        when(memberRepository.findByUserNo(10L)).thenReturn(Optional.ofNullable(member));

        memberService.updateMemberTotalBalanceByUserNo(10L, 500000);

        Mockito.verify(memberRepository, times(1)).updateTotalBalance(anyString(), anyInt());

        //사용자 자금보다 더 큰 금액을 빼려 해 변경에 실패
        when(memberRepository.findByUserNo(10L)).thenReturn(Optional.ofNullable(member));

        assertThrows(MemberException.class,
                () -> memberService.updateMemberTotalBalanceByUserNo(10L, -20000000), ErrorCode.CANNOT_TOTALBALANCE_MINUS.getMessage());

    }

    @Test
    @DisplayName("유저의 비밀번호를 변경한다")
    void testUpdatePassword() {
        //정상 비밀번호 변경
        when(memberRepository.findByUserId("10")).thenReturn(Optional.ofNullable(member));

        memberService.updateMemberPassword("10", CORRECT_PASSWORD2);

        //사용자가 존재하지 않아 비밀번호 변경에 실패
        when(memberRepository.findByUserId("11")).thenReturn(Optional.empty());

        assertThrows(NotFoundMemberException.class,
                () -> memberService.updateMemberPassword("11", CORRECT_PASSWORD2));

        //비밀번호가 조건에 맞지않아 비밀번호 변경에 실패
        when(memberRepository.findByUserId("10")).thenReturn(Optional.ofNullable(member));

        assertThrows(MemberException.class,
                () -> memberService.updateMemberPassword("10", INCORRECT_PASSWORD), ErrorCode.PASSWORD_RULE.getMessage());


        //비밀번호가 이전과 일치해 비밀번호 변경에 실패
        when(memberRepository.findByUserId("10")).thenReturn(Optional.ofNullable(member));

        assertThrows(MemberException.class,
                () -> memberService.updateMemberPassword("10", INCORRECT_PASSWORD), ErrorCode.SAME_PASSWORD.getMessage());


    }
}
