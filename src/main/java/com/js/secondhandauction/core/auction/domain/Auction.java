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
public class Auction {
    private long auctionNo;
    private long itemNo;
    private int bid = 0;
    private String regId;
    private LocalDateTime regDate;

    @Builder
    public Auction(long auctionNo,
                   long itemNo,
                   int bid,
                   String regId,
                   LocalDateTime regDate
    ) {
        this.auctionNo = auctionNo;
        this.itemNo = itemNo;
        this.bid = bid;
        this.regId = regId;
        this.regDate = regDate;
    }

    public AuctionResponse toResponse() {
        return AuctionResponse.builder()
                .auctionNo(auctionNo)
                .itemNo(itemNo)
                .bid(bid)
                .build();
    }
}
