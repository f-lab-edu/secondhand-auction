package com.js.secondhandauction.core.user.controller;

import com.js.secondhandauction.common.response.ApiResponse;
import com.js.secondhandauction.core.user.domain.User;
import com.js.secondhandauction.core.user.dto.UserCreateRequest;
import com.js.secondhandauction.core.user.dto.UserCreateResponse;
import com.js.secondhandauction.core.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 회원가입
     */
    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody UserCreateRequest userCreateRequest) {
        return new ResponseEntity<>(ApiResponse.success(userService.createUser(userCreateRequest)), HttpStatus.CREATED);
    }

    /**
     * 회원 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUser(@PathVariable String id) {
        return new ResponseEntity<>(ApiResponse.success(userService.getUser(id)), HttpStatus.OK);
    }


}
