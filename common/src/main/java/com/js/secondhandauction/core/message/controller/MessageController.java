package com.js.secondhandauction.core.message.controller;

import com.js.secondhandauction.common.response.ApiResponse;
import com.js.secondhandauction.common.security.service.CustomUserDetails;
import com.js.secondhandauction.core.message.dto.MessageResponse;
import com.js.secondhandauction.core.message.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "메시지", description = "메시지 관리 API")
@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping("/{userNo}")
    @Operation(summary = "메시지 조회", description = "메시지를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "메시지 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "로그인 이전", content = @Content)
    })
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getMessages(@PathVariable("userNo") long userNo) {
        return ResponseEntity.ok(ApiResponse.success(messageService.getMessages(userNo)));
    }

    @GetMapping("/my")
    @Operation(summary = "내 메시지 조회", description = "내 메시지를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "내 메시지 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "로그인 이전", content = @Content)
    })
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getMyMessage(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(ApiResponse.success(messageService.getMessages(customUserDetails.getId())));
    }
}
