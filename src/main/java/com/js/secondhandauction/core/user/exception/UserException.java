package com.js.secondhandauction.core.user.exception;

import com.js.secondhandauction.common.exception.CustomException;
import com.js.secondhandauction.common.exception.ErrorCode;

public class UserException extends CustomException {
    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
