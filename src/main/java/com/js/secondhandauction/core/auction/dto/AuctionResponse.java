package com.js.secondhandauction.core.auction.dto;

import com.js.secondhandauction.core.auction.domain.Auction;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AuctionResponse {
    private long auctionNo;
    private long itemNo;
    private int bid = 0;

    public static AuctionResponse of(Auction auction) {
        return AuctionResponse.builder()
                .auctionNo(auction.getAuctionNo())
                .itemNo(auction.getItemNo())
                .bid(auction.getBid())
                .build();
    }
}
