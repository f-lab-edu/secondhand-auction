package com.js.secondhandauction.sender.sqs;

import com.js.secondhandauction.core.message.dto.MessageRequest;
import io.awspring.cloud.sqs.operations.SendResult;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AmazonSQSSender {
    @Autowired
    private SqsTemplate sqsTemplate;

    private final String queueName = "sha-message-queue";

    public AmazonSQSSender(SqsTemplate sqsTemplate) {
        this.sqsTemplate = sqsTemplate;
    }

    public SendResult<String> sendMessage(String message) {
        log.info("Sending message to SQS : {}", message);
        return sqsTemplate.send(to -> to
                .queue(queueName)
                .payload(message));
    }

    public void sendInbox(MessageRequest messageRequest) {
        log.info("Sending message to SQS test InBox: {}", messageRequest);
        sqsTemplate.send(to -> to
                .queue(queueName)
                .payload(messageRequest));
    }

}
