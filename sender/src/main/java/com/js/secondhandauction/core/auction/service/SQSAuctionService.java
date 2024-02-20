package com.js.secondhandauction.core.auction.service;

import com.js.secondhandauction.core.auction.domain.Auction;
import com.js.secondhandauction.core.auction.dto.*;
import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.domain.State;
import com.js.secondhandauction.core.message.dto.MessageRequest;
import com.js.secondhandauction.sender.sqs.AmazonSQSSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
@Slf4j
public class SQSAuctionService extends AuctionService {

    @Autowired
    AmazonSQSSender amazonSQSSender;

    protected MessageRequest getMessageRequest(Auction auction, Item item) {
        List<AuctionParticipantsResponse> participantsResponseList = super.getAuctionParticipants(auction.getItemNo());

        MessageRequest messageRequest = MessageRequest.builder()
                .participants(participantsResponseList)
                .auction(auction)
                .item(item)
                .build();

        if (super.isFinalBid(item)) {
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
    protected void finishAuction(Auction auction, Item item) {
        super.finishAuction(auction, item);

        amazonSQSSender.sendInbox(getMessageRequest(auction, item));
    }

}
