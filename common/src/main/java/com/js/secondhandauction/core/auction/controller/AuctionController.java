package com.js.secondhandauction.core.auction.controller;

import com.js.secondhandauction.common.response.ApiResponse;
import com.js.secondhandauction.common.security.service.CustomUserDetails;
import com.js.secondhandauction.core.auction.domain.Auction;
import com.js.secondhandauction.core.auction.dto.AuctionWithoutBidRequest;
import com.js.secondhandauction.core.auction.dto.AuctionRequest;
import com.js.secondhandauction.core.auction.dto.AuctionResponse;
import com.js.secondhandauction.core.auction.service.AuctionRedissonService;
import com.js.secondhandauction.core.auction.service.AuctionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "경매", description = "경매 관리 API")
@RestController
@RequestMapping("/auctions")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private AuctionRedissonService auctionRedissonService;

    @PostMapping
    @Operation(summary = "경매 등록", description = "경매를 등록합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "경매 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 미보유", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "상품 없음", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "금액 미달", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "기타사항", content = @Content),
    })
    public ResponseEntity<ApiResponse<AuctionResponse>> createAuction(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody AuctionRequest auctionRequest) {
        return new ResponseEntity<>(ApiResponse.success(auctionRedissonService.createAuctionWithLock(auctionRequest.toEntity(customUserDetails), false)), HttpStatus.CREATED);
    }

    @PostMapping("/min-bid")
    @Operation(summary = "최소 입찰", description = "최소 입찰을 합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "최소 입찰 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 미보유", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "상품 없음", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "금액 미달", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "기타사항", content = @Content),
    })
    public ResponseEntity<ApiResponse<AuctionResponse>> minBid(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody AuctionWithoutBidRequest auctionWithoutBidRequest) {
        return new ResponseEntity<>(ApiResponse.success(auctionRedissonService.createAuctionWithLock(auctionWithoutBidRequest.toAuctionRequest().toEntity(customUserDetails), false)), HttpStatus.CREATED);
    }

    @PostMapping("/immediate-purchase")
    @Operation(summary = "즉시 구매", description = "즉시 구매를 합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "즉시 구매 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 미보유", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "상품 없음", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "금액 미달", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "기타사항", content = @Content),
    })
    public ResponseEntity<ApiResponse<AuctionResponse>> immediatePurchaseItem(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody AuctionWithoutBidRequest auctionWithoutBidRequest) {
        return new ResponseEntity<>(ApiResponse.success(auctionRedissonService.createAuctionWithLock(auctionWithoutBidRequest.toAuctionRequest().toEntity(customUserDetails), true)), HttpStatus.CREATED);
    }

    @GetMapping("/{itemNo}")
    @Operation(summary = "경매 조회", description = "경매를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "경매 조회 성공"),
    })
    public ResponseEntity<ApiResponse<List<Auction>>> getAuctions(@PathVariable("itemNo") long itemNo) {
        return new ResponseEntity<>(ApiResponse.success(auctionService.getAuctions(itemNo)), HttpStatus.OK);
    }


}
