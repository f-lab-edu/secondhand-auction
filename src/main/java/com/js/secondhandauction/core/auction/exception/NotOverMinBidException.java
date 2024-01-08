package com.js.secondhandauction.core.auction.exception;

import com.js.secondhandauction.common.exception.ErrorCode;

public class NotOverMinBidException extends AuctionException {
    public NotOverMinBidException() {
        super(ErrorCode.NOT_OVER_MINBID);
    }
}
