package com.js.secondhandauction.core.auction.controller;

import com.js.secondhandauction.core.auction.domain.Auction;
import com.js.secondhandauction.core.auction.dto.AuctionRequest;
import com.js.secondhandauction.core.auction.dto.AuctionResponse;
import com.js.secondhandauction.core.auction.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auctions")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    @PostMapping
    public ResponseEntity<AuctionResponse> createAuction(@AuthenticationPrincipal User user, @RequestBody AuctionRequest auctionRequest) {
        final String userId = user.getUsername();
        return ResponseEntity.ok(auctionService.createAuction(userId, auctionRequest));
    }

    @GetMapping("/{itemNo}")
    public ResponseEntity<List<Auction>> getAuctions(@PathVariable("itemNo") long itemNo) {
        return ResponseEntity.ok(auctionService.getAuctions(itemNo));
    }
}
