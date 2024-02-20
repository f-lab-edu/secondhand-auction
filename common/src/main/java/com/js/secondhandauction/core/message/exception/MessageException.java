package com.js.secondhandauction.core.message.exception;

import com.js.secondhandauction.common.exception.CustomException;
import com.js.secondhandauction.common.exception.ErrorCode;

public class MessageException extends CustomException {
    public MessageException(ErrorCode errorCode) {
        super(errorCode);
    }
}
