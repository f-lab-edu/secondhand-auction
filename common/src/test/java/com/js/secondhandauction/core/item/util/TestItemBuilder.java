package com.js.secondhandauction.core.item.util;

import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.domain.State;

import java.time.LocalDateTime;

public class TestItemBuilder {
    static long REG_USER_NO = 100L;

    public static Item buildItem(long itemNo, State state, Boolean isBid) {
        return Item.builder()
                .itemNo(itemNo)
                .item("item")
                .itemDesc("itemDesc")
                .regDate(LocalDateTime.now())
                .uptDate(LocalDateTime.now())
                .regPrice(300000)
                .immediatePrice(2000000)
                .state(state)
                .regId(REG_USER_NO)
                .betTime(30)
                .isBid(isBid)
                .build();
    }

    public static long getRegUserNo() {
        return REG_USER_NO;
    }
}
