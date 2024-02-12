package com.js.secondhandauction.core.auction.controller;

import com.js.secondhandauction.common.response.ApiResponse;
import com.js.secondhandauction.common.security.service.CustomUserDetails;
import com.js.secondhandauction.core.auction.domain.Auction;
import com.js.secondhandauction.core.auction.dto.AuctionImmediatePurchaseRequest;
import com.js.secondhandauction.core.auction.dto.AuctionRequest;
import com.js.secondhandauction.core.auction.dto.AuctionResponse;
import com.js.secondhandauction.core.auction.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auctions")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    @PostMapping
    public ResponseEntity<ApiResponse<AuctionResponse>> createAuction(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody AuctionRequest auctionRequest) {
        return new ResponseEntity<>(ApiResponse.success(auctionService.createAuction(customUserDetails, auctionRequest)), HttpStatus.CREATED);
    }

    @PostMapping("/immediate-purchase")
    public ResponseEntity<ApiResponse<AuctionResponse>> immediatePurchaseItem(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody AuctionImmediatePurchaseRequest auctionImmediatePurchaseRequest) {
        return new ResponseEntity<>(ApiResponse.success(auctionService.createImmediateAuction(customUserDetails, auctionImmediatePurchaseRequest)), HttpStatus.CREATED);
    }

    @GetMapping("/{itemNo}")
    public ResponseEntity<ApiResponse<List<Auction>>> getAuctions(@PathVariable("itemNo") long itemNo) {
        return new ResponseEntity<>(ApiResponse.success(auctionService.getAuctions(itemNo)), HttpStatus.OK);
    }


}
