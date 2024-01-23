package com.js.secondhandauction.core.member.exception;

import com.js.secondhandauction.common.exception.CustomException;
import com.js.secondhandauction.common.exception.ErrorCode;

public class MemberException extends CustomException {
    public MemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}
