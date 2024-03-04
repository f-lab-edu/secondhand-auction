package com.js.secondhandauction.core.member.controller;

import com.js.secondhandauction.common.exception.ErrorCode;
import com.js.secondhandauction.common.response.ApiResponse;
import com.js.secondhandauction.common.security.service.CustomUserDetails;
import com.js.secondhandauction.core.member.domain.Role;
import com.js.secondhandauction.core.member.dto.MemberCreateRequest;
import com.js.secondhandauction.core.member.dto.MemberCreateResponse;
import com.js.secondhandauction.core.member.dto.MemberGetResponse;
import com.js.secondhandauction.core.member.exception.MemberException;
import com.js.secondhandauction.core.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class MemberController {

    @Autowired
    MemberService memberService;

    /**
     * 회원가입
     */
    @PostMapping("/public/members")
    public ResponseEntity<ApiResponse<MemberCreateResponse>> createMember(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody MemberCreateRequest memberCreateRequest) {
        if (memberCreateRequest.role() != Role.USER) {
            if (customUserDetails == null) {
                throw new MemberException(ErrorCode.ACCESS_DENIED);
            }

            if (customUserDetails.getRole() != Role.ADMIN) {
                throw new MemberException(ErrorCode.ACCESS_DENIED);
            }
        }

        return new ResponseEntity<>(ApiResponse.success(memberService.createMember(memberCreateRequest)), HttpStatus.CREATED);
    }

    /**
     * 회원 조회
     */
    @GetMapping("/public/members/{userId}")
    public ResponseEntity<ApiResponse<MemberGetResponse>> getMember(@PathVariable String userId) {
        return new ResponseEntity<>(ApiResponse.success(memberService.getMemberByUserId(userId)), HttpStatus.OK);
    }

    /**
     * 자기 자신 조회
     */
    @GetMapping("/members/me")
    public ResponseEntity<ApiResponse<MemberGetResponse>> getMyInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return new ResponseEntity<>(ApiResponse.success(memberService.getMemberByUserNo(customUserDetails.getId())), HttpStatus.OK);
    }


    /**
     * 회원 잔액 추가
     */
    @PatchMapping("/members/{userId}/totalBalance")
    public ResponseEntity<ApiResponse<Void>> updateMemberBalance(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable String userId, @RequestBody int totalBalance) {
        if (customUserDetails.getRole() != Role.ADMIN) {
            throw new MemberException(ErrorCode.ACCESS_DENIED);
        }

        memberService.updateMemberTotalBalanceByUserId(userId, totalBalance);
        return new ResponseEntity<>(ApiResponse.success(null), HttpStatus.OK);
    }

    /**
     * 회원 비밀번호 변경
     */
    @PatchMapping("/members/password")
    public ResponseEntity<ApiResponse<Void>> updateMemberPassword(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody String password) {
        memberService.updateMemberPassword(customUserDetails.getUsername(), password);
        return new ResponseEntity<>(ApiResponse.success(null), HttpStatus.OK);
    }

    /**
     * 회원 비밀번호 초기화
     */
    @PatchMapping("/public/members/{userId}/password")
    public ResponseEntity<ApiResponse<String>> resetMemberPassword(@PathVariable String userId) {
        String temporaryPassword = String.format("%06d", (int) (Math.random() * 1000000)) + "A!";

        memberService.updateMemberPassword(userId, temporaryPassword);
        return new ResponseEntity<>(ApiResponse.success(temporaryPassword), HttpStatus.OK);
    }

}
