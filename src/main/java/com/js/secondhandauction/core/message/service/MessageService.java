package com.js.secondhandauction.core.message.service;

import com.js.secondhandauction.core.auction.dto.AuctionParticipantsResponse;
import com.js.secondhandauction.core.item.domain.State;
import com.js.secondhandauction.core.message.dto.MessageRequest;
import com.js.secondhandauction.core.message.dto.MessageResponse;
import com.js.secondhandauction.core.message.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class MessageService {
    @Autowired
    MessageRepository messageRepository;

    public List<MessageResponse> getMessages(long userNo) {
        return messageRepository.getMessages(userNo);
    }

    @Transactional
    public void sendMessage(MessageRequest messageRequest) {
        List<AuctionParticipantsResponse> participants = messageRequest.getParticipants();
        String message = "";
        if (State.ONSALE == messageRequest.getState()) {
            message = "경매에 참여하신 [" + messageRequest.getItem().getItem() + "]에 새로운 입찰자가 등장했습니다! 현재 가격은 [" + messageRequest.getAuction().getBid() + "]원 입니다.";

            participants = participants.stream()
                    .filter(participant -> participant.getRegId() != messageRequest.getAuction().getRegId())
                    .toList();

        } else if (State.SOLDOUT == messageRequest.getState()) {
            message = "아이템 [" + messageRequest.getItem().getItem() + "] 경매가 종료되었습니다! 최종 가격은 [" + messageRequest.getAuction().getBid() + "]원 입니다.";
        }

        for (AuctionParticipantsResponse participant : participants) {
            messageRepository.sendMessage(participant.getRegId(), message);
            //.replace("{itemNo}", String.valueOf(messageRequest.getItem().getItemNo()))
            //.replace("{auctionNo}", String.valueOf(messageRequest.getAuction().getAuctionNo()));
        }

    }
}
