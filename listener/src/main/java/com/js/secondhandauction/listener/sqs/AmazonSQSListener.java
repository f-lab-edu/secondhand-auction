package com.js.secondhandauction.listener.sqs;

import com.js.secondhandauction.core.auction.dto.AuctionParticipantsResponse;
import com.js.secondhandauction.core.item.domain.State;
import com.js.secondhandauction.core.message.dto.MessageRequest;
import com.js.secondhandauction.core.message.service.MessageService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.listener.acknowledgement.handler.AcknowledgementMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.time.Duration;
import java.util.List;

@Component
@Slf4j
public class AmazonSQSListener {

    @Autowired
    private MessageService messageService;

    @Bean
    SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory(SqsAsyncClient sqsAsyncClient) {
        return SqsMessageListenerContainerFactory
                .builder()
                .configure(options -> options
                        .acknowledgementMode(AcknowledgementMode.ALWAYS)
                        .acknowledgementInterval(Duration.ofSeconds(3))
                        //한번에 수신할 수 있는 메시지의 최대 수
                        .acknowledgementThreshold(5)
                        //SQS 에서 받아온 메시지가 다른 워커(소비자)에 보이지 않는 시간
                        .messageVisibility(Duration.ofSeconds(10))
                )
                .sqsAsyncClient(sqsAsyncClient)
                .build();
    }

    @SqsListener(queueNames = "sha-message-queue")
    public void listenInbox(MessageRequest messageRequest) {
        log.info("Listening to SQS InBox: " + messageRequest);

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

        //message.replace("{itemNo}", String.valueOf(messageRequest.getItem().getItemNo()))
        //message.replace("{auctionNo}", String.valueOf(messageRequest.getAuction().getAuctionNo()));

        messageService.sendMessage(participants, message);


    }
}
