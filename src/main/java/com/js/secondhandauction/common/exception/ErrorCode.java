package com.js.secondhandauction.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {


    //USER
    NOT_FOUND_USER("존재하지 않는 유저입니다."),
    NOT_OVER_TOTALBALANCE("가진 금액보다 더 큰돈을 베팅할 수 없습니다."),
    CANNOT_TOTALBALANCE_MINUS("가진 금액이 마이너스가 될 수 없습니다."),

    //ITEM
    NOT_FOUND_ITEM("존재하지 않는 아이템입니다."),
    ALREADY_SOLDOUT("이미 판매된 아이템입니다."),
    CANNOT_UPDATE_SOLDOUT_ITEM("판매된 아이템은 수정할 수 없습니다."),

    //AUCTION
    DUPLICATE_USER_TICK("같은 유저가 반복입찰할 수 없습니다."),
    NOT_OVER_MINBID("최소 입찰금액을 넘지 못했습니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
