package com.js.secondhandauction.core.message;

import com.js.secondhandauction.core.auction.dto.AuctionParticipantsResponse;
import com.js.secondhandauction.core.message.repository.MessageRepository;
import com.js.secondhandauction.core.message.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {
    @InjectMocks
    private MessageService messageService;

    @Mock
    private MessageRepository messageRepository;

    List<AuctionParticipantsResponse> participantList;

    @BeforeEach
    void setUp() {
        // given
        participantList = new ArrayList<>();

        for (long i = 1L; i < 20L; i++) {
            AuctionParticipantsResponse participant = AuctionParticipantsResponse.builder()
                    .userNo(i)
                    .build();
            participantList.add(participant);
        }
    }

    @Test
    @DisplayName("입찰 완료된 물품에 메시지를 생성한다")
    void testOnsaleCreateMessage() {
        // when
        messageService.sendMessage(participantList, "test");
        // then
        Mockito.verify(messageRepository, Mockito.times(participantList.size())).sendMessage(anyLong(), anyString());
    }

}
