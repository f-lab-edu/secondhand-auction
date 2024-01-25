package com.js.secondhandauction.core.member.exception;

import com.js.secondhandauction.common.exception.ErrorCode;

public class NotFoundMemberException extends MemberException {
    public NotFoundMemberException() {
        super(ErrorCode.NOT_FOUND_USER);
    }
}
