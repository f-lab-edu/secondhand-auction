package com.js.secondhandauction.core.auction.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AuctionResponse {
    private long auctionNo;
    private long itemNo;
    private int bid = 0;

    @Builder
    public AuctionResponse(long auctionNo,
                   long itemNo,
                   int bid
    ) {
        this.auctionNo = auctionNo;
        this.itemNo = itemNo;
        this.bid = bid;
    }
}
