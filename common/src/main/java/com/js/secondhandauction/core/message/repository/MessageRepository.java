package com.js.secondhandauction.core.message.repository;

import com.js.secondhandauction.core.message.dto.MessageResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageRepository {
    List<MessageResponse> getMessages(long userNo);

    void sendMessage(long userNo, String messageContent);
}
