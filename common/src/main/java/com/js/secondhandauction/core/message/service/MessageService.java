package com.js.secondhandauction.core.message.service;

import com.js.secondhandauction.core.auction.dto.AuctionParticipantsResponse;
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
    public void sendMessage(List<AuctionParticipantsResponse> participants, String message) {

        for (AuctionParticipantsResponse participant : participants) {
            messageRepository.sendMessage(participant.getUserNo(), message);
        }

    }
}
