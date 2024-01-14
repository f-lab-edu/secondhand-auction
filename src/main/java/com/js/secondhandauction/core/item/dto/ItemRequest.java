package com.js.secondhandauction.core.item.dto;

import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.domain.State;

public record ItemRequest(
        String item,
        int regPrice
) {
    public Item toEntity() {
        return Item.builder()
                .item(item)
                .regPrice(regPrice)
                .build();
    }

}
