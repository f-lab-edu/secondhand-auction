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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원", description = "회원 관리 API")
@RestController
public class MemberController {

    @Autowired
    MemberService memberService;

    /**
     * 회원가입
     */
    @PostMapping("/public/members")
    @Operation(summary = "회원가입", description = "회원가입을 합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 미보유", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "중복된 아이디", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "비밀번호 규칙 위반", content = @Content)
    })
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
    @Operation(summary = "회원 조회", description = "회원을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원 없음", content = @Content)
    })
    public ResponseEntity<ApiResponse<MemberGetResponse>> getMember(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable String userId) {
        return new ResponseEntity<>(ApiResponse.success(memberService.getMemberByUserId(userId)), HttpStatus.OK);
    }

    /**
     * 자기 자신 조회
     */
    @GetMapping("/members/my")
    @Operation(summary = "내 정보 조회", description = "내 정보를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "내 정보 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "로그인 이전", content = @Content)
    })
    public ResponseEntity<ApiResponse<MemberGetResponse>> getMyInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return new ResponseEntity<>(ApiResponse.success(memberService.getMemberByUserNo(customUserDetails.getId())), HttpStatus.OK);
    }


    /**
     * 회원 잔액 추가
     */
    @PatchMapping("/members/{userId}/totalBalance")
    @Operation(summary = "회원 잔액 추가", description = "회원의 잔액을 추가합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 잔액 추가 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 미보유", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원 없음", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "잔액보다 큰 금액 감소", content = @Content)
    })
    public ResponseEntity<ApiResponse<Void>> updateMemberBalance(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                 @Parameter(description = "사용자 ID", required = true)
                                                                 @PathVariable String userId,
                                                                 @RequestBody int totalBalance) {
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
    @Operation(summary = "비밀번호 변경", description = "로그인한 사용자의 비밀번호를 변경합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "비밀번호 변경 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원 없음", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "기존 비밀번호와 동일", content = @Content)
    })
    public ResponseEntity<ApiResponse<Void>> updateMemberPassword(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody String password) {
        memberService.updateMemberPassword(customUserDetails.getUsername(), password);
        return new ResponseEntity<>(ApiResponse.success(null), HttpStatus.OK);
    }

    /**
     * 회원 비밀번호 초기화
     */
    @PatchMapping("/public/members/{userId}/password")
    @Operation(summary = "비밀번호 초기화", description = "회원의 비밀번호를 초기화합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "비밀번호 초기화 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원 없음")
    })
    public ResponseEntity<ApiResponse<String>> resetMemberPassword(@PathVariable String userId) {
        String temporaryPassword = String.format("%06d", (int) (Math.random() * 1000000)) + "A!";

        memberService.updateMemberPassword(userId, temporaryPassword);
        return new ResponseEntity<>(ApiResponse.success(temporaryPassword), HttpStatus.OK);
    }

}
