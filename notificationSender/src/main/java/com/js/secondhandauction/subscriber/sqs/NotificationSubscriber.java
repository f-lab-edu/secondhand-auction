package com.js.secondhandauction.subscriber.sqs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.js.secondhandauction.websocket.ReactiveWebSocketHandler;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.listener.acknowledgement.handler.AcknowledgementMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.time.Duration;

@Component
@Slf4j
public class NotificationSubscriber {

    @Autowired
    private ReactiveWebSocketHandler webSocketHandler;

    @Bean
    SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory(SqsAsyncClient sqsAsyncClient) {
        return SqsMessageListenerContainerFactory
                .builder()
                .configure(options -> options
                        .acknowledgementMode(AcknowledgementMode.ALWAYS)
                        .acknowledgementInterval(Duration.ofSeconds(3))
                        //한번에 수신할 수 있는 메시지의 최대 수
                        .acknowledgementThreshold(1)
                        //SQS 에서 받아온 메시지가 다른 워커(소비자)에 보이지 않는 시간
                        .messageVisibility(Duration.ofSeconds(30))
                )
                .sqsAsyncClient(sqsAsyncClient)
                .build();
    }

    @SqsListener(queueNames = "sha-message-send-queue")
    public void consumeNotification(String info) {
        log.info("Listening to SQS InBox: " + info);
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(info);
            JsonNode participantsNode = root.path("participants");
            log.info("participantsNode: " + participantsNode);
            String messageText = root.path("message").asText();
            log.info("messageText: " + messageText);

            for (JsonNode jsonNode : participantsNode) {
                String userNo = jsonNode.path("userNo").asText();
                log.info("userNo: " + userNo);
                webSocketHandler.getSink(userNo).emitNext(messageText, Sinks.EmitFailureHandler.FAIL_FAST);
            }
        } catch (JsonProcessingException e) {
            log.error(String.valueOf(e));
        }

    }
}
