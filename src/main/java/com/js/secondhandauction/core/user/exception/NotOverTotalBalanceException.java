package com.js.secondhandauction.core.user.exception;

import com.js.secondhandauction.common.exception.ErrorCode;

public class NotOverTotalBalanceException extends UserException {
    public NotOverTotalBalanceException() {
        super(ErrorCode.NOT_OVER_TOTALBALANCE);
    }
}
