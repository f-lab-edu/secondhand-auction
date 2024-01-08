package com.js.secondhandauction.core.item.exception;

import com.js.secondhandauction.common.exception.ErrorCode;

public class AlreadySoldoutException extends ItemException {
    public AlreadySoldoutException() {
        super(ErrorCode.ALREADY_SOLDOUT);
    }
}
