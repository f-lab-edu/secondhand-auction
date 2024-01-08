package com.js.secondhandauction.core.item.exception;

import com.js.secondhandauction.common.exception.CustomException;
import com.js.secondhandauction.common.exception.ErrorCode;

public class ItemException extends CustomException {
    public ItemException(ErrorCode errorCode) {
        super(errorCode);
    }
}
