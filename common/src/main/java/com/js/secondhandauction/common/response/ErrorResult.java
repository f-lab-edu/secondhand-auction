package com.js.secondhandauction.common.response;

import com.js.secondhandauction.common.exception.ErrorCode;

public record ErrorResult(int status, String code, String message) {
    public static ErrorResult of(ErrorCode errorCode) {
        return new ErrorResult(errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage());
    }
}
