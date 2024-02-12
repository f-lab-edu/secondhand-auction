package com.js.secondhandauction.core.item.controller;

import com.js.secondhandauction.common.response.ApiResponse;
import com.js.secondhandauction.common.security.service.CustomUserDetails;
import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.dto.ItemRequest;
import com.js.secondhandauction.core.item.dto.ItemResponse;
import com.js.secondhandauction.core.item.service.ItemService;
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
    public ResponseEntity<ApiResponse<ItemResponse>> createItem(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody ItemRequest itemRequest) {
        return new ResponseEntity<>(ApiResponse.success(itemService.createItem(customUserDetails, itemRequest)), HttpStatus.CREATED);
    }

    /**
     * 상품 조회
     */
    @GetMapping("/{itemNo}")
    public ResponseEntity<Item> getItem(@PathVariable long itemNo) {
        return new ResponseEntity<>(itemService.getItem(itemNo), HttpStatus.OK);
    }

    /**
     * 상품 수정
     */
    @PutMapping("/{itemNo}")
    public ResponseEntity<ApiResponse<ItemResponse>> updateItem(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable long itemNo, @RequestBody ItemRequest itemRequest) {
        return new ResponseEntity<>(ApiResponse.success(itemService.updateItem(itemNo, customUserDetails, itemRequest)), HttpStatus.OK);
    }

    /**
     * 상품 삭제
     */
    @DeleteMapping("/{itemNo}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable long itemNo) {
        itemService.deleteItem(itemNo, customUserDetails);
        return new ResponseEntity<>(ApiResponse.success(null), HttpStatus.NO_CONTENT);
    }

}
