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
@Builder
public class Item {
    private long itemNo;
    private String item;
    private LocalDateTime regDate;
    private LocalDateTime uptDate;
    private int regPrice;
    private State state;
    private long regId;
    private int betTime;
}
