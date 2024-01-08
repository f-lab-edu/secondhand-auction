package com.js.secondhandauction.core.item.dto;

import com.js.secondhandauction.core.item.domain.State;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ItemResponse {
    private long itemNo;
    private String item;
    private int regPrice;
    private State state;

    @Builder
    public ItemResponse(long itemNo, String item, int regPrice, State state) {
        this.itemNo = itemNo;
        this.item = item;
        this.regPrice = regPrice;
        this.state = state;
    }
}
