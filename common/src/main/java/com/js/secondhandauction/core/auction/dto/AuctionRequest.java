package com.js.secondhandauction.core.auction.dto;

import com.js.secondhandauction.common.security.service.CustomUserDetails;
import com.js.secondhandauction.core.auction.domain.Auction;

public record AuctionRequest(
        long itemNo,
        int bid
) {
    public Auction toEntity(CustomUserDetails customUserDetails) {
        return Auction.builder()
                .itemNo(itemNo)
                .bid(bid)
                .regId(customUserDetails.getId())
                .build();
    }

}
