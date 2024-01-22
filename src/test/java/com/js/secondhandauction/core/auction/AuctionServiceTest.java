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
import com.js.secondhandauction.core.item.exception.AlreadySoldoutException;
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


    private final long ITEM_REG_ID = 100L;
    private final long USER_ID = 1L;

    private final long USER_ID2 = 2L;

    private final long NO_TICK_ITEM_NO = 2L;

    private final long TICK_ITEM_NO = 3L;

    private final long SOLDOUT_ITEM_NO = 4L;

    private Auction auction;

    private AuctionRequest auctionRequest;

    private User user;

    private User user2;

    private Item NO_TICK_ITEM;

    private Item TICK_ITEM;

    private Item SOLDOUT_ITEM;


    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(USER_ID)
                .username("Test User")
                .nickname("Test Name")
                .totalBalance(10000000)
                .build();

        user2 = User.builder()
                .id(USER_ID2)
                .username("Test User")
                .nickname("Test Name")
                .totalBalance(10000000)
                .build();

        NO_TICK_ITEM = Item.builder()
                .itemNo(ITEM_REG_ID)
                .item("Test Item")
                .state(State.ONSALE)
                .betTime(5)
                .regPrice(300000)
                .build();

        TICK_ITEM = Item.builder()
                .itemNo(ITEM_REG_ID)
                .item("Test Item")
                .state(State.ONSALE)
                .betTime(5)
                .regPrice(300000)
                .build();

        SOLDOUT_ITEM = Item.builder()
                .itemNo(ITEM_REG_ID)
                .item("Test Item")
                .state(State.SOLDOUT)
                .betTime(5)
                .regPrice(300000)
                .build();

        Mockito.when(itemService.getItem(NO_TICK_ITEM_NO)).thenReturn(NO_TICK_ITEM);
        Mockito.when(itemService.getItem(TICK_ITEM_NO)).thenReturn(TICK_ITEM);
        Mockito.when(itemService.getItem(SOLDOUT_ITEM_NO)).thenReturn(SOLDOUT_ITEM);

    }

    @Test
    @DisplayName("경매를 생성한다")
    void testCreateAuction() {

        auctionRequest = new AuctionRequest(NO_TICK_ITEM_NO, 500000);

        //정상 입찰
        Mockito.when(auctionRepository.getCountTick(NO_TICK_ITEM_NO)).thenReturn(0);

        AuctionResponse createdAuction = auctionService.createAuction(user, auctionRequest);
        assertNotNull(createdAuction);
        assertThat(createdAuction.getBid()).isEqualTo(500000);

        AuctionRequest soldAuctionRequest = new AuctionRequest(SOLDOUT_ITEM_NO, 500000);

        //판매 된 물건 입찰 실패
        Assertions.assertThrows(AlreadySoldoutException.class,
                () -> auctionService.createAuction(user, soldAuctionRequest));

        //중복 입찰 실패
        Mockito.when(auctionRepository.getCountTick(TICK_ITEM_NO)).thenReturn(1);

        Auction lastTick = Auction.builder()
                .bid(400000)
                .regId(USER_ID)
                .itemNo(TICK_ITEM_NO)
                .build();

        Mockito.when(auctionRepository.getLastTick(TICK_ITEM_NO)).thenReturn(lastTick);

        AuctionRequest dupAuctionRequest = new AuctionRequest(TICK_ITEM_NO, 500000);

        Assertions.assertThrows(DuplicateUserTickException.class,
                () -> auctionService.createAuction(user, dupAuctionRequest));

        //최소 베팅 금액
        AuctionRequest newAuctionRequest = new AuctionRequest(TICK_ITEM_NO, 410000);

        Assertions.assertThrows(NotOverMinBidException.class,
                () -> auctionService.createAuction(user2, newAuctionRequest));
    }

}
