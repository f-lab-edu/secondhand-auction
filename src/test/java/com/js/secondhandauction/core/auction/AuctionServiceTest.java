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
import com.js.secondhandauction.core.member.domain.Member;
import com.js.secondhandauction.core.member.dto.MemberGetResponse;
import com.js.secondhandauction.core.member.service.MemberService;
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
    private ItemService itemService;
    @Mock
    private MemberService memberService;
    private final long ITEM_REG_ID = 100L;
    private final long MEMBER_ID = 1L;
    private final long MEMBER_ID2 = 2L;
    private final long NO_TICK_ITEM_NO = 2L;
    private final long TICK_ITEM_NO = 3L;
    private final long SOLDOUT_ITEM_NO = 4L;
    private AuctionRequest auctionRequest;
    private MemberGetResponse member;
    private MemberGetResponse member2;
    private Item NO_TICK_ITEM;
    private Item TICK_ITEM;
    private Item SOLDOUT_ITEM;


    @BeforeEach
    public void setup() {
        member = MemberGetResponse.builder()
                .uniqId(MEMBER_ID)
                .userId("Test User")
                .nickname("Test Name")
                .totalBalance(10000000)
                .build();

        member2 = MemberGetResponse.builder()
                .uniqId(MEMBER_ID2)
                .userId("Test User")
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

        Mockito.when(memberService.getMemberByUniqId(MEMBER_ID)).thenReturn(member);

        Mockito.when(memberService.getMemberByUniqId(MEMBER_ID2)).thenReturn(member2);

        //정상 입찰
        Mockito.when(auctionRepository.getCountTick(NO_TICK_ITEM_NO)).thenReturn(0);

        AuctionResponse createdAuction = auctionService.createAuction(MEMBER_ID, auctionRequest);
        assertNotNull(createdAuction);
        assertThat(createdAuction.getBid()).isEqualTo(500000);

        AuctionRequest soldAuctionRequest = new AuctionRequest(SOLDOUT_ITEM_NO, 500000);

        //판매 된 물건 입찰 실패
        Assertions.assertThrows(AlreadySoldoutException.class,
                () -> auctionService.createAuction(MEMBER_ID, soldAuctionRequest));

        //중복 입찰 실패
        Mockito.when(auctionRepository.getCountTick(TICK_ITEM_NO)).thenReturn(1);

        Auction lastTick = Auction.builder()
                .bid(400000)
                .regId(MEMBER_ID)
                .itemNo(TICK_ITEM_NO)
                .build();

        Mockito.when(auctionRepository.getLastTick(TICK_ITEM_NO)).thenReturn(lastTick);

        AuctionRequest dupAuctionRequest = new AuctionRequest(TICK_ITEM_NO, 500000);

        Assertions.assertThrows(DuplicateUserTickException.class,
                () -> auctionService.createAuction(MEMBER_ID, dupAuctionRequest));

        //최소 베팅 금액
        AuctionRequest newAuctionRequest = new AuctionRequest(TICK_ITEM_NO, 410000);

        Assertions.assertThrows(NotOverMinBidException.class,
                () -> auctionService.createAuction(MEMBER_ID2, newAuctionRequest));
    }

}
