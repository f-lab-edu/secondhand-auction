package com.js.secondhandauction.publisher.sqs;

import com.js.secondhandauction.core.message.dto.MessageRequest;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessagePublisher {
    @Autowired
    private SqsTemplate sqsTemplate;

    private final String queueName = "sha-message-queue";

    public void publishMessage(MessageRequest messageRequest) {
        log.info("Sending message to SQS test InBox: {}", messageRequest);
        sqsTemplate.send(to -> to
                .queue(queueName)
                .payload(messageRequest));
    }

}
