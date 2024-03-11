package com.js.secondhandauction.core.auction.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuctionParticipantsResponse {
    private long userNo;
}
