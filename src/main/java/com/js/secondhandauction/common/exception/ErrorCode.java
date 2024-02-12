package com.js.secondhandauction.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    //COMMON
    ACCESS_DENIED(403, "C001", "권한이 없습니다."),
    INVALID_PASSWORD(401, "C002", "잘못된 비밀번호입니다."),
    UNAUTHENTICATED(401, "C003", "로그인 이후 이용해주세요."),

    //MEMEBER
    ALREADY_EXIST_MEMBER(400, "M001", "이미 존재하는 회원입니다."),
    NOT_FOUND_MEMBER(404, "M002", "존재하지 않는 회원입니다."),
    CANNOT_TOTALBALANCE_MINUS(422, "M003", "가진 금액이 마이너스가 될 수 없습니다."),
    SAME_PASSWORD(422, "M004", "새로운 비밀번호는 기존 비밀번호와 같을 수 없습니다."),
    PASSWORD_RULE(422, "M005", "비밀번호는 8자 이상, 20자 이하의 영문, 숫자, 특수문자 조합이어야 합니다."),

    //ITEM
    NOT_FOUND_ITEM(404, "I001", "존재하지 않는 아이템입니다."),
    CANNOT_CHANGE_ITEM(422, "I003", "아이템을 수정할 수 없습니다."),
    EDIT_ONLY_REGID(422, "I004", "등록자만 변경할 수 있습니다."),
    CANNOT_BID_EXPIRED(400, "A006", "경매가 종료된 상품입니다."),

    //AUCTION
    DUPLICATE_MEMBER_TICK(400, "A001", "같은 회원이 반복입찰할 수 없습니다."),
    NOT_OVER_MINBID(400, "A002", "최소 입찰금액을 넘지 못했습니다."),
    CANNOT_BID_MYSELF(400, "A003", "자신의 상품에는 입찰할 수 없습니다."),
    CANNOT_BETTING_OVER_MAXTIMES(400, "A004", "최대 입찰 횟수를 초과하였습니다."),
    CAN_BID_ONLY_ONSALE(400, "A005", "판매중인 상품만 입찰할 수 있습니다."),
    CANNOT_OVER_IMMEDIATEPRICE(400, "A007", "즉시 구매가보다 높은 금액으로 입찰할 수 없습니다.");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;

    }

    public String createErrorResponse() {
        return "{ \"message\": \"FAIL\", \"data\": { \"status\": " + status +
                ", \"code\": \"" + code + "\", \"message\": \"" + message + "\" } }";
    }
}
