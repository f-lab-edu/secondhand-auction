package com.js.secondhandauction.core.item.domain;

import com.js.secondhandauction.core.item.dto.ItemResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Item {
    private long itemNo;
    private String item;
    private LocalDateTime regDate;
    private LocalDateTime uptDate;
    private int regPrice;
    private State state;
    private String regId;
    private int betTime;

    @Builder
    public Item(long itemNo,
                String item,
                LocalDateTime regDate,
                LocalDateTime uptDate,
                int regPrice,
                State state,
                String regId,
                int betTime
    ) {
        this.itemNo = itemNo;
        this.item = item;
        this.regDate = regDate;
        this.uptDate = uptDate;
        this.regPrice = regPrice;
        this.state = state;
        this.regId = regId;
        this.betTime = betTime;
    }

    public ItemResponse toResponse() {
        return ItemResponse.builder()
                .itemNo(itemNo)
                .item(item)
                .regPrice(regPrice)
                .state(state)
                .build();
    }
}
