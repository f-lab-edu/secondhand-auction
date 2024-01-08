package com.js.secondhandauction.core.user.exception;

import com.js.secondhandauction.common.exception.ErrorCode;

public class NotFoundUserException extends UserException {
    public NotFoundUserException() {
        super(ErrorCode.NOT_FOUND_USER);
    }
}
