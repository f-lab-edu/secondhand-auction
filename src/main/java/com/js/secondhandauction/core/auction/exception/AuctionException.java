package com.js.secondhandauction.core.auction.exception;

import com.js.secondhandauction.common.exception.CustomException;
import com.js.secondhandauction.common.exception.ErrorCode;

public class AuctionException extends CustomException {
    public AuctionException(ErrorCode errorCode) {
        super(errorCode);
    }
}
