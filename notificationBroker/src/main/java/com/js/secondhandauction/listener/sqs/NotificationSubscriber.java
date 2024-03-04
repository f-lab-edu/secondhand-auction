package com.js.secondhandauction.listener.sqs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.js.secondhandauction.core.auction.dto.AuctionParticipantsResponse;
import com.js.secondhandauction.core.auction.service.AuctionService;
import com.js.secondhandauction.core.item.domain.State;
import com.js.secondhandauction.core.message.dto.MessageRequest;
import com.js.secondhandauction.core.message.dto.MessageSendRequest;
import com.js.secondhandauction.core.message.service.MessageService;
import com.js.secondhandauction.sender.sqs.NotificationSender;
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
import java.util.stream.IntStream;

@Component
@Slf4j
public class NotificationSubscriber {

    @Autowired
    private MessageService messageService;

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private NotificationSender notificationSender;

    private final int MAX_INSERT_COUNT = 100;

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
    public void notificationRequestSubscriber(MessageRequest messageRequest) throws JsonProcessingException {
        log.info("Listening to SQS InBox: " + messageRequest);

        List<AuctionParticipantsResponse> participants = auctionService.getAuctionParticipants(messageRequest.getAuction().getItemNo());

        String message = "";
        if (State.ONSALE == messageRequest.getState()) {
            message = "경매에 참여하신 [" + messageRequest.getItem().getItem() + "]에 새로운 입찰자가 등장했습니다! 현재 가격은 [" + messageRequest.getAuction().getBid() + "]원 입니다.";

            participants = participants.stream()
                    .filter(participant -> participant.getUserNo() != messageRequest.getAuction().getRegId())
                    .toList();

        } else if (State.SOLDOUT == messageRequest.getState()) {
            message = "아이템 [" + messageRequest.getItem().getItem() + "] 경매가 종료되었습니다! 최종 가격은 [" + messageRequest.getAuction().getBid() + "]원 입니다.";
        }

        //message.replace("{itemNo}", String.valueOf(messageRequest.getItem().getItemNo()));
        //message.replace("{auctionNo}", String.valueOf(messageRequest.getAuction().getAuctionNo()));


        // 리스트를 MAX_INSERT_COUNT 크기로 분할하여 처리
        final List<AuctionParticipantsResponse> finalParticipants = participants;
        final String finalMessage = message;

        IntStream.range(0, (participants.size() + MAX_INSERT_COUNT - 1) / MAX_INSERT_COUNT)
                .mapToObj(i -> finalParticipants.subList(i * MAX_INSERT_COUNT, Math.min((i + 1) * MAX_INSERT_COUNT, finalParticipants.size())))
                .forEach(partition -> {
                    messageService.sendMessage(partition, finalMessage);
                });

        MessageSendRequest messageSendRequest = MessageSendRequest.builder()
                .participants(finalParticipants)
                .message(finalMessage)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(messageSendRequest);

        notificationSender.sendNotification(jsonString);


    }
}
