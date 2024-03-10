package com.js.secondhandauction.core.auction.service;

import com.js.secondhandauction.core.auction.domain.Auction;
import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.domain.State;
import com.js.secondhandauction.core.message.dto.MessageRequest;
import com.js.secondhandauction.publisher.sqs.MessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@Slf4j
public class AuctionMessagePublisherService extends AuctionService {

    @Autowired
    MessagePublisher messagePublisher;

    protected MessageRequest getMessageRequest(Auction auction, Item item, boolean isImmediatePurchase, int bidCount) {
        MessageRequest messageRequest = MessageRequest.builder()
                .auction(auction)
                .item(item)
                .build();

        if (super.isFinalBid(item, isImmediatePurchase, bidCount)) {
            log.info("경매 종료 메시지 전송");
            messageRequest.setState(State.SOLDOUT);
        } else {
            log.info("경매 진행 메시지 전송");
            messageRequest.setState(State.ONSALE);
        }

        return messageRequest;
    }

    /**
     * 경매 종료
     */
    @Override
    protected void finishAuction(Auction auction, Item item, boolean isImmediatePurchase, int bidCount) {
        super.finishAuction(auction, item, isImmediatePurchase, bidCount);

        messagePublisher.publishMessage(getMessageRequest(auction, item, isImmediatePurchase, bidCount));
    }

}
