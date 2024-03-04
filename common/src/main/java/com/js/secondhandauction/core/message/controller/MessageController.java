package com.js.secondhandauction.core.message.controller;

import com.js.secondhandauction.common.response.ApiResponse;
import com.js.secondhandauction.common.security.service.CustomUserDetails;
import com.js.secondhandauction.core.message.dto.MessageResponse;
import com.js.secondhandauction.core.message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping("/{userNo}")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getMessages(@PathVariable("userNo") long userNo) {
        return ResponseEntity.ok(ApiResponse.success(messageService.getMessages(userNo)));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getMyMessage(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(ApiResponse.success(messageService.getMessages(customUserDetails.getId())));
    }
}
