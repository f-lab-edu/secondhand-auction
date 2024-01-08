package com.js.secondhandauction.core.user.exception;

import com.js.secondhandauction.common.exception.ErrorCode;

public class CannotTotalBalanceMinusException extends UserException {
    public CannotTotalBalanceMinusException() {
        super(ErrorCode.CANNOT_TOTALBALANCE_MINUS);
    }
}
