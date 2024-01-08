package com.js.secondhandauction.core.auction.dto;

import com.js.secondhandauction.core.auction.domain.Auction;

public record AuctionRequest(
    long itemNo,
    int bid
) {
    public Auction toEntity() {
        return Auction.builder()
                .itemNo(itemNo)
                .bid(bid)
                .build();
    }

}
