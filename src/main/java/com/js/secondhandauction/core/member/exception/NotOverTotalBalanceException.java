package com.js.secondhandauction.core.member.exception;

import com.js.secondhandauction.common.exception.ErrorCode;

public class NotOverTotalBalanceException extends MemberException {
    public NotOverTotalBalanceException() {
        super(ErrorCode.NOT_OVER_TOTALBALANCE);
    }
}
