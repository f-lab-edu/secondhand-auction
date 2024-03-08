package com.js.secondhandauction.core.auction;

import com.js.secondhandauction.common.exception.ErrorCode;
import com.js.secondhandauction.common.security.service.CustomUserDetails;
import com.js.secondhandauction.core.auction.dto.AuctionLastBidResponse;
import com.js.secondhandauction.core.auction.dto.AuctionRequest;
import com.js.secondhandauction.core.auction.dto.AuctionResponse;
import com.js.secondhandauction.core.auction.exception.AuctionException;
import com.js.secondhandauction.core.auction.repository.AuctionRepository;
import com.js.secondhandauction.core.auction.service.AuctionService;
import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.domain.State;
import com.js.secondhandauction.core.item.exception.ItemException;
import com.js.secondhandauction.core.item.service.ItemService;
import com.js.secondhandauction.core.item.util.TestItemBuilder;
import com.js.secondhandauction.core.member.service.MemberService;
import com.js.secondhandauction.core.member.util.TestMemberBuilder;
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


    private final long USER_NO = 1L;
    private final long USER_NO2 = 2L;
    private final long NO_BID_ITEM_NO = 2L;
    private final long BID_ITEM_NO = 3L;
    private final long END_BID_ITEM_NO = 4L;
    private final long SOLDOUT_ITEM_NO = 5L;
    private final long EXPIRED_ITEM_NO = 6L;
    private AuctionRequest auctionRequest;
    private CustomUserDetails USERDETAILS_USER_NO;
    private CustomUserDetails USERDETAILS_USER_NO2;
    private CustomUserDetails USERDETAILS_REG_USER_NO;
    private Item NO_BID_ITEM;
    private Item BID_ITEM;
    private Item END_BID_ITEM;
    private Item SOLDOUT_ITEM;
    private Item EXPIRED_ITEM;
    private AuctionLastBidResponse BID_ITEM_RESPONSE;
    private AuctionLastBidResponse END_BID_ITEM_RESPONSE;
    private AuctionLastBidResponse SOLDOUT_ITEM_RESPONSE;


    @BeforeEach
    public void setup() {
        USERDETAILS_USER_NO = TestMemberBuilder.buildUserDetails(USER_NO);

        USERDETAILS_USER_NO2 = TestMemberBuilder.buildUserDetails(USER_NO2);

        USERDETAILS_REG_USER_NO = TestMemberBuilder.buildUserDetails(TestItemBuilder.getRegUserNo());

        NO_BID_ITEM = TestItemBuilder.buildItem(NO_BID_ITEM_NO, State.ONSALE, false);

        BID_ITEM = TestItemBuilder.buildItem(BID_ITEM_NO, State.ONSALE, true);

        END_BID_ITEM = TestItemBuilder.buildItem(END_BID_ITEM_NO, State.ONSALE, true);

        SOLDOUT_ITEM = TestItemBuilder.buildItem(SOLDOUT_ITEM_NO, State.SOLDOUT, true);

        EXPIRED_ITEM = TestItemBuilder.buildItem(EXPIRED_ITEM_NO, State.ONSALE, true);

        BID_ITEM_RESPONSE = AuctionLastBidResponse.builder()
                .bid(350000)
                .regId(USER_NO)
                .itemNo(BID_ITEM_NO)
                .build();

        END_BID_ITEM_RESPONSE = AuctionLastBidResponse.builder()
                .bid(350000)
                .regId(USER_NO)
                .itemNo(END_BID_ITEM_NO)
                .build();

        SOLDOUT_ITEM_RESPONSE = AuctionLastBidResponse.builder()
                .bid(350000)
                .regId(USER_NO)
                .itemNo(SOLDOUT_ITEM_NO)
                .build();

        Mockito.when(itemService.getItem(NO_BID_ITEM_NO)).thenReturn(NO_BID_ITEM);
        Mockito.when(itemService.getItem(BID_ITEM_NO)).thenReturn(BID_ITEM);
        Mockito.when(itemService.getItem(END_BID_ITEM_NO)).thenReturn(END_BID_ITEM);
        Mockito.when(itemService.getItem(SOLDOUT_ITEM_NO)).thenReturn(SOLDOUT_ITEM);
        Mockito.when(itemService.getItem(EXPIRED_ITEM_NO)).thenReturn(EXPIRED_ITEM);

    }

    @Test
    @DisplayName("경매를 생성한다")
    void testCreateAuction() {

        auctionRequest = new AuctionRequest(NO_BID_ITEM_NO, 500000);

        //정상 입찰
        Mockito.when(auctionRepository.getTotalBidCount(NO_BID_ITEM_NO)).thenReturn(0);

        AuctionResponse createdAuction = auctionService.createAuction(auctionRequest.toEntity(USERDETAILS_USER_NO), false);
        assertNotNull(createdAuction);
        assertThat(createdAuction.getBid()).isEqualTo(500000);

        //아이템 만료로 입찰 실패
        AuctionRequest expiredAuctionRequest = new AuctionRequest(EXPIRED_ITEM_NO, 500000);

        Mockito.when(itemService.isItemExpired(EXPIRED_ITEM)).thenReturn(true);

        Assertions.assertThrows(ItemException.class,
                () -> auctionService.createAuction(expiredAuctionRequest.toEntity(USERDETAILS_USER_NO), false), ErrorCode.CANNOT_BID_EXPIRED.getMessage());


        //아이템 등록자 입찰로 입찰실패
        AuctionRequest regAuctionRequest = new AuctionRequest(NO_BID_ITEM_NO, 500000);

        Assertions.assertThrows(AuctionException.class,
                () -> auctionService.createAuction(regAuctionRequest.toEntity(USERDETAILS_REG_USER_NO), false), ErrorCode.CANNOT_BID_MYSELF.getMessage());

        //입찰 횟수 초과로 입찰 실패
        AuctionRequest endAuctionRequest = new AuctionRequest(END_BID_ITEM_NO, 500000);

        Mockito.when(auctionRepository.getLastBid(END_BID_ITEM_NO)).thenReturn(END_BID_ITEM_RESPONSE);

        Assertions.assertThrows(AuctionException.class,
                () -> auctionService.createAuction(endAuctionRequest.toEntity(USERDETAILS_USER_NO), false), ErrorCode.CANNOT_BETTING_OVER_MAXTIMES.getMessage());

        //판매 된 물건 입찰 실패
        AuctionRequest soldAuctionRequest = new AuctionRequest(SOLDOUT_ITEM_NO, 500000);

        Assertions.assertThrows(ItemException.class,
                () -> auctionService.createAuction(soldAuctionRequest.toEntity(USERDETAILS_USER_NO), false), ErrorCode.CAN_BID_ONLY_ONSALE.getMessage());

        //중복 입찰 실패
        Mockito.when(auctionRepository.getLastBid(BID_ITEM_NO)).thenReturn(BID_ITEM_RESPONSE);

        AuctionRequest dupAuctionRequest = new AuctionRequest(BID_ITEM_NO, 500000);

        Assertions.assertThrows(AuctionException.class,
                () -> auctionService.createAuction(dupAuctionRequest.toEntity(USERDETAILS_USER_NO), false), ErrorCode.DUPLICATE_MEMBER_TICK.getMessage());

        //최소 베팅 금액
        AuctionRequest newAuctionRequest = new AuctionRequest(BID_ITEM_NO, 360000);

        Assertions.assertThrows(AuctionException.class,
                () -> auctionService.createAuction(newAuctionRequest.toEntity(USERDETAILS_USER_NO2), false), ErrorCode.NOT_OVER_MINBID.getMessage());

    }

}
