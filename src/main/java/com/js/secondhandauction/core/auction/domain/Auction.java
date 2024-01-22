package com.js.secondhandauction.core.auction.domain;

import com.js.secondhandauction.core.auction.dto.AuctionResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
public class Auction {
    private long auctionNo;
    private long itemNo;
    private int bid;
    private long regId;
    private LocalDateTime regDate;
}
