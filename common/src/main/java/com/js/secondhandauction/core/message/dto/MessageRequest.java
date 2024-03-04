package com.js.secondhandauction.core.message.dto;

import com.js.secondhandauction.core.auction.domain.Auction;
import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.domain.State;
import lombok.*;


@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageRequest {
    Auction auction;
    Item item;
    State state;
}
