package com.js.secondhandauction.core.message;

import com.js.secondhandauction.core.auction.domain.Auction;
import com.js.secondhandauction.core.auction.dto.AuctionParticipantsResponse;
import com.js.secondhandauction.core.auction.service.AuctionService;
import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.domain.State;
import com.js.secondhandauction.core.message.dto.MessageRequest;
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

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {
    @InjectMocks
    private MessageService messageService;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private AuctionService auctionService;

    private Auction auction;

    private Item item;

    private final long PARTICIPANT_NO = 1L;

    private final long ITEM_NO = 100L;

    List<AuctionParticipantsResponse> participantList;

    @BeforeEach
    void setUp() {
        // given
        participantList = new ArrayList<>();

        for (long i = 1L; i < 20L; i++) {
            AuctionParticipantsResponse participant = AuctionParticipantsResponse.builder()
                    .regId(i)
                    .build();
            participantList.add(participant);
        }

        auction = Auction.builder()
                .bid(100000)
                .regId(PARTICIPANT_NO)
                .build();

        item = Item.builder()
                .itemNo(ITEM_NO)
                .item("아이템")
                .build();
    }

    @Test
    @DisplayName("입찰 완료된 물품에 메시지를 생성한다")
    void testOnsaleCreateMessage() {
        // given
        MessageRequest onsaleMessageRequest = MessageRequest.builder()
                .participants(participantList)
                .auction(auction)
                .item(item)
                .state(State.ONSALE)
                .build();


        // when
        messageService.sendMessage(onsaleMessageRequest);
        // then
        Mockito.verify(messageRepository, Mockito.times(participantList.size() - 1)).sendMessage(anyLong(), anyString());
    }

    @Test
    @DisplayName("입찰 완료된 물품에 메시지를 생성한다")
    void testSoldoutCreateMessage() {
        // given
        MessageRequest soldoutMessageRequest = MessageRequest.builder()
                .participants(participantList)
                .auction(auction)
                .item(item)
                .state(State.SOLDOUT)
                .build();

        // when
        messageService.sendMessage(soldoutMessageRequest);
        // then
        Mockito.verify(messageRepository, Mockito.times(participantList.size())).sendMessage(anyLong(), anyString());
    }
}
