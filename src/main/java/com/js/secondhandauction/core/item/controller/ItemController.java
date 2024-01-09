package com.js.secondhandauction.core.item.controller;

import com.js.secondhandauction.common.response.ApiResponse;
import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.dto.ItemRequest;
import com.js.secondhandauction.core.item.dto.ItemResponse;
import com.js.secondhandauction.core.item.service.ItemService;
import com.js.secondhandauction.core.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    private ItemService itemService;

    /**
     * 상품 등록
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ItemResponse>> createItem(@AuthenticationPrincipal User user, @RequestBody ItemRequest itemRequest) {
        final String userId = user.getUsername();
        return new ResponseEntity<>(ApiResponse.success(itemService.createItem(userId, itemRequest)), HttpStatus.CREATED);
    }

    /**
     * 상품 조회
     */
    @GetMapping("/{itemNo}")
    public ResponseEntity<ApiResponse<Item>> getItem(@PathVariable long itemNo) {
        return new ResponseEntity<>(ApiResponse.success(itemService.getItem(itemNo)), HttpStatus.OK);
    }

    /**
     * 상품 수정
     */
    @PutMapping("/{itemNo}")
    public ResponseEntity<ApiResponse<ItemResponse>> updateItem(@AuthenticationPrincipal User user, @PathVariable long itemNo, @RequestBody ItemRequest itemRequest) {
        final String userId = user.getUsername();
        return new ResponseEntity<>(ApiResponse.success(itemService.updateItem(itemNo, userId, itemRequest)), HttpStatus.OK);
    }

}
