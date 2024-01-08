package com.js.secondhandauction.core.auction.exception;

import com.js.secondhandauction.common.exception.ErrorCode;

public class DuplicateUserTickException extends AuctionException {
    public DuplicateUserTickException() {
        super(ErrorCode.DUPLICATE_USER_TICK);
    }
}
