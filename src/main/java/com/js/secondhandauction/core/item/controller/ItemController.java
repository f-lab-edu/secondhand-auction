package com.js.secondhandauction.core.item.controller;

import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.dto.ItemRequest;
import com.js.secondhandauction.core.item.dto.ItemResponse;
import com.js.secondhandauction.core.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    private ItemService itemService;

    //TODO
    final String userId = "1";

    /**
     * 상품 등록
     */
    @PostMapping
    public ResponseEntity<ItemResponse> createItem(@RequestBody ItemRequest itemRequest) {
        return ResponseEntity.ok(itemService.createItem(userId, itemRequest));
    }

    /**
     * 상품 조회
     */
    @GetMapping("/{itemNo}")
    public ResponseEntity<Item> getItem(@PathVariable long itemNo) {
        return ResponseEntity.ok(itemService.getItem(itemNo));
    }

    /**
     * 상품 수정
     */
    @PutMapping("/{itemNo}")
    public ResponseEntity<ItemResponse> updateItem(@PathVariable long itemNo, @RequestBody ItemRequest itemRequest) {
        return ResponseEntity.ok(itemService.updateItem(itemNo, userId, itemRequest));
    }

}
