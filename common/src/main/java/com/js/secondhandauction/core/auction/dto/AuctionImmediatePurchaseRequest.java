package com.js.secondhandauction.core.auction.dto;

import com.js.secondhandauction.common.security.service.CustomUserDetails;
import com.js.secondhandauction.core.auction.domain.Auction;

public record AuctionImmediatePurchaseRequest(
        long itemNo
) {
    public Auction toEntity(CustomUserDetails customUserDetails) {
        return Auction.builder()
                .itemNo(itemNo)
                .regId(customUserDetails.getId())
                .build();
    }

    public AuctionRequest toAuctionRequest() {
        return new AuctionRequest(itemNo, 0);
    }

}
