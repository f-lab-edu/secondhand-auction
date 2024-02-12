package com.js.secondhandauction.core.auction.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuctionLastBidResponse {
    private long itemNo;
    private int bid;
    private long regId;
}
