package com.js.secondhandauction.core.user.controller;

import com.js.secondhandauction.core.user.domain.User;
import com.js.secondhandauction.core.user.dto.UserCreateRequest;
import com.js.secondhandauction.core.user.dto.UserCreateResponse;
import com.js.secondhandauction.core.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<UserCreateResponse> createUser(@RequestBody UserCreateRequest userCreateRequest) {
        return ResponseEntity.ok(userService.createUser(userCreateRequest));
    }

    /**
     * 회원 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUser(id));
    }


}
