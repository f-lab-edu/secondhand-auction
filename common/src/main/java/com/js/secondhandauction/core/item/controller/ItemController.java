package com.js.secondhandauction.core.item.controller;

import com.js.secondhandauction.common.response.ApiResponse;
import com.js.secondhandauction.common.security.service.CustomUserDetails;
import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.dto.ItemRequest;
import com.js.secondhandauction.core.item.dto.ItemResponse;
import com.js.secondhandauction.core.item.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "상품", description = "상품 관리 API")
@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    private ItemService itemService;

    /**
     * 상품 등록
     */
    @PostMapping
    @Operation(summary = "상품 등록", description = "상품을 등록합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "상품 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 미보유", content = @Content),
    })
    public ResponseEntity<ApiResponse<ItemResponse>> createItem(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody ItemRequest itemRequest) {
        return new ResponseEntity<>(ApiResponse.success(itemService.createItem(customUserDetails, itemRequest)), HttpStatus.CREATED);
    }

    /**
     * 상품 조회
     */
    @GetMapping("/{itemNo}")
    @Operation(summary = "상품 조회", description = "상품을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "상품 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "상품 없음", content = @Content),
    })
    public ResponseEntity<Item> getItem(@PathVariable long itemNo) {
        return new ResponseEntity<>(itemService.getItem(itemNo), HttpStatus.OK);
    }

    /**
     * 상품 수정
     */
    @PutMapping("/{itemNo}")
    @Operation(summary = "상품 수정", description = "상품을 수정합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "상품 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 미보유", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "상품 없음", content = @Content),
    })
    public ResponseEntity<ApiResponse<ItemResponse>> updateItem(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable long itemNo, @RequestBody ItemRequest itemRequest) {
        return new ResponseEntity<>(ApiResponse.success(itemService.updateItem(itemNo, customUserDetails, itemRequest)), HttpStatus.OK);
    }

    /**
     * 상품 삭제
     */
    @DeleteMapping("/{itemNo}")
    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "상품 삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 미보유", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "상품 없음", content = @Content),
    })
    public ResponseEntity<ApiResponse<Void>> deleteItem(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable long itemNo) {
        itemService.deleteItem(itemNo, customUserDetails);
        return new ResponseEntity<>(ApiResponse.success(null), HttpStatus.NO_CONTENT);
    }

}
