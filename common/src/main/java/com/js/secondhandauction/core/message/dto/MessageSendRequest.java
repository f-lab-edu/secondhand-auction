package com.js.secondhandauction.core.message.dto;

import com.js.secondhandauction.core.auction.dto.AuctionParticipantsResponse;
import lombok.*;

import java.util.List;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageSendRequest {
    List<AuctionParticipantsResponse> participants;
    private String message;
}
