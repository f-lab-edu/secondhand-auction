package com.js.secondhandauction.core.member.controller;

import com.js.secondhandauction.common.response.ApiResponse;
import com.js.secondhandauction.core.member.dto.MemberCreateRequest;
import com.js.secondhandauction.core.member.dto.MemberCreateResponse;
import com.js.secondhandauction.core.member.dto.MemberGetResponse;
import com.js.secondhandauction.core.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    MemberService memberService;

    /**
     * 회원가입
     */
    @PostMapping
    public ResponseEntity<ApiResponse<MemberCreateResponse>> createMember(@RequestBody MemberCreateRequest memberCreateRequest) {
        return new ResponseEntity<>(ApiResponse.success(memberService.createMember(memberCreateRequest)), HttpStatus.CREATED);
    }

    /**
     * 회원 조회
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<MemberGetResponse>> getMember(@PathVariable String userId) {
        return new ResponseEntity<>(ApiResponse.success(memberService.getMemberByUserId(userId)), HttpStatus.OK);
    }


}
