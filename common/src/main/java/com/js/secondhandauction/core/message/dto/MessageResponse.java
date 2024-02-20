package com.js.secondhandauction.core.message.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageResponse {
    private String messageContent;
}
