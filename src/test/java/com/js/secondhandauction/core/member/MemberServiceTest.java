package com.js.secondhandauction.core.member;

import com.js.secondhandauction.core.member.domain.Member;
import com.js.secondhandauction.core.member.domain.Role;
import com.js.secondhandauction.core.member.dto.MemberCreateRequest;
import com.js.secondhandauction.core.member.dto.MemberCreateResponse;
import com.js.secondhandauction.core.member.dto.MemberGetResponse;
import com.js.secondhandauction.core.member.exception.CannotTotalBalanceMinusException;
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
import static org.mockito.ArgumentMatchers.any;
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

    @BeforeEach
    void setup() {
        member = Member.builder()
                .userId("Test User")
                .nickname("Test Name")
                .build();

        memberCreateRequest = new MemberCreateRequest("Test User", "Test Name", "pw", Role.USER);

    }


    @Test
    @DisplayName("멤버를 생성한다")
    void testCreateMember() {
        MemberCreateResponse createdMember = memberService.createMember(memberCreateRequest);

        assertNotNull(createdMember);
        Assertions.assertThat("Test User").isEqualTo(createdMember.getUserId());
        Assertions.assertThat("Test Name").isEqualTo(createdMember.getNickname());
        Mockito.verify(memberRepository, times(1)).create(any(Member.class));
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
        when(memberRepository.findByUniqId(10L)).thenReturn(Optional.ofNullable(member));

        memberService.updateMemberTotalBalanceByUniqId(10L, 500000);

        Mockito.verify(memberRepository, times(1)).updateTotalBalance(anyString(), anyInt());

        //사용자 자금보다 더 큰 금액을 빼려 해 변경에 실패
        when(memberRepository.findByUniqId(10L)).thenReturn(Optional.ofNullable(member));

        assertThrows(CannotTotalBalanceMinusException.class,
                () -> memberService.updateMemberTotalBalanceByUniqId(10L, -20000000));

    }

}
