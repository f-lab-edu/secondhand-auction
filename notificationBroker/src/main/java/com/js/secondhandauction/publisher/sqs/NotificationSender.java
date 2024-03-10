package com.js.secondhandauction.publisher.sqs;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationSender {
    @Autowired
    private SqsTemplate sqsTemplate;

    private final String queueName = "sha-message-send-queue";

    public void publishNotficationInfo(String message) {
        log.info("Sending message to SQS test InBox: {}", message);
        sqsTemplate.send(to -> to
                .queue(queueName)
                .payload(message));
    }
}
