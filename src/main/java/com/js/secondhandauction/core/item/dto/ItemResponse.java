package com.js.secondhandauction.core.item.dto;

import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.domain.State;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemResponse {
    private long itemNo;
    private String item;
    private String itemDesc;
    private int regPrice;
    private int immediatePrice;
    private State state;
    private Boolean isBid;

    public static ItemResponse of(Item item) {
        return ItemResponse.builder()
                .itemNo(item.getItemNo())
                .item(item.getItem())
                .itemDesc(item.getItemDesc())
                .regPrice(item.getRegPrice())
                .immediatePrice(item.getImmediatePrice())
                .state(item.getState())
                .isBid(item.getIsBid())
                .build();
    }
}
