package com.js.secondhandauction.core.auction;

import com.js.secondhandauction.core.auction.domain.Auction;
import com.js.secondhandauction.core.auction.dto.AuctionRequest;
import com.js.secondhandauction.core.auction.dto.AuctionResponse;
import com.js.secondhandauction.core.auction.exception.DuplicateUserTickException;
import com.js.secondhandauction.core.auction.exception.NotOverMinBidException;
import com.js.secondhandauction.core.auction.repository.AuctionRepository;
import com.js.secondhandauction.core.auction.service.AuctionService;
import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.domain.State;
import com.js.secondhandauction.core.item.service.ItemService;
import com.js.secondhandauction.core.user.domain.User;
import com.js.secondhandauction.core.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(MockitoExtension.class)
public class AuctionServiceTest {

    @InjectMocks
    private AuctionService auctionService;
    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private UserService userService;

    @Mock
    private ItemService itemService;


    private final String USER_ID = "1";

    private final long ITEM_ID = 2L;

    private Auction auction;

    private AuctionRequest auctionRequest;


    @BeforeEach
    public void setup() {
        User user = User.builder()
                .id(USER_ID)
                .name("Test User")
                .build();

        Item item = Item.builder()
                .itemNo(ITEM_ID)
                .item("Test Item")
                .state(State.ONSALE)
                .betTime(5)
                .regPrice(300000)
                .build();

        auction = Auction.builder()
                .bid(500000)
                .itemNo(ITEM_ID)
                .regId(USER_ID)
                .build();

        auctionRequest = new AuctionRequest(ITEM_ID, 500000);

        Mockito.when(userService.getUser(Mockito.anyString())).thenReturn(user);
        Mockito.when(itemService.getItem(Mockito.anyLong())).thenReturn(item);
    }

    @Test
    @DisplayName("입찰을 한다.")
    void testCreateAuction() {

        Mockito.when(auctionRepository.getCountTick(Mockito.anyLong())).thenReturn(0);

        AuctionResponse createdAuction = auctionService.createAuction(USER_ID, auctionRequest);
        assertNotNull(createdAuction);
        assertThat(createdAuction.getBid()).isEqualTo(500000);


    }

    @Test
    @DisplayName("한 유저가 어떤아이템에 중복 입찰을 해 입찰에 실패한다.")
    void testCreateAuctionThrowDuplicateUserTickException() {

        Mockito.when(auctionRepository.getCountTick(Mockito.anyLong())).thenReturn(1);

        Auction lastTick = Auction.builder()
                .bid(400000)
                .regId(USER_ID)
                .itemNo(ITEM_ID)
                .build();


        Mockito.when(auctionRepository.getLastTick(Mockito.anyLong())).thenReturn(lastTick);

        Assertions.assertThrows(DuplicateUserTickException.class,
                () -> auctionService.createAuction(USER_ID, auctionRequest));

    }

    @Test
    @DisplayName("최소 베팅금액을 넘지 못해 입찰에 실패한다.")
    void testCreateAuctionThrowNotOverMinBidException() {

        Mockito.when(auctionRepository.getCountTick(Mockito.anyLong())).thenReturn(1);

        Auction lastTick = Auction.builder()
                .bid(400000)
                .regId("100")
                .itemNo(ITEM_ID)
                .build();

        Mockito.when(auctionRepository.getLastTick(Mockito.anyLong())).thenReturn(lastTick);

        auction.setBid(410000);

        Assertions.assertThrows(NotOverMinBidException.class,
                () -> auctionService.createAuction(USER_ID, auctionRequest));

    }


}
