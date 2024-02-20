package com.js.secondhandauction.core.message.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
public class Message {
    private long messageNo;
    private long userNo;
    private long messageContent;
    private LocalDateTime regDate;
}
