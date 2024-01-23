package com.js.secondhandauction.core.member.exception;

import com.js.secondhandauction.common.exception.ErrorCode;

public class CannotTotalBalanceMinusException extends MemberException {
    public CannotTotalBalanceMinusException() {
        super(ErrorCode.CANNOT_TOTALBALANCE_MINUS);
    }
}
