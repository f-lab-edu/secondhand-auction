package com.js.secondhandauction.sender.sqs;

import com.js.secondhandauction.core.message.dto.MessageRequest;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationBrokerSender {
    @Autowired
    private SqsTemplate sqsTemplate;

    private final String queueName = "sha-message-queue";

    public void makeNotification(MessageRequest messageRequest) {
        log.info("Sending message to SQS test InBox: {}", messageRequest);
        sqsTemplate.send(to -> to
                .queue(queueName)
                .payload(messageRequest));
    }

}
