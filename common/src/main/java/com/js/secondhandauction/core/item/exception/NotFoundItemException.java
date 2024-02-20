package com.js.secondhandauction.core.item.exception;

import com.js.secondhandauction.common.exception.ErrorCode;

public class NotFoundItemException extends ItemException {
    public NotFoundItemException() {
        super(ErrorCode.NOT_FOUND_ITEM);
    }
}
