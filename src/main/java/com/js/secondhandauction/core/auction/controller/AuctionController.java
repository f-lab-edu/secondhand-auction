package com.js.secondhandauction.core.auction.controller;

import com.js.secondhandauction.core.auction.domain.Auction;
import com.js.secondhandauction.core.auction.dto.AuctionRequest;
import com.js.secondhandauction.core.auction.dto.AuctionResponse;
import com.js.secondhandauction.core.auction.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auctions")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    //TODO
    final String userId = "1";

    @PostMapping
    public ResponseEntity<AuctionResponse> createAuction(@RequestBody AuctionRequest auctionRequest) {
        return ResponseEntity.ok(auctionService.createAuction(userId , auctionRequest));
    }

    @GetMapping("/{itemNo}")
    public ResponseEntity<List<Auction>> getAuctions(@PathVariable("itemNo") long itemNo) {
        return ResponseEntity.ok(auctionService.getAuctions(itemNo));
    }
}
