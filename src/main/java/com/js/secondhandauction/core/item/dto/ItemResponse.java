package com.js.secondhandauction.core.item.dto;

import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.domain.State;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ItemResponse {
    private long itemNo;
    private String item;
    private int regPrice;
    private State state;

    public static ItemResponse of(Item item) {
        return ItemResponse.builder()
                .itemNo(item.getItemNo())
                .item(item.getItem())
                .regPrice(item.getRegPrice())
                .state(item.getState())
                .build();
    }
}
