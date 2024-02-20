package com.js.secondhandauction.core.auction.service;

import com.js.secondhandauction.common.exception.ErrorCode;
import com.js.secondhandauction.common.security.service.CustomUserDetails;
import com.js.secondhandauction.core.auction.domain.Auction;
import com.js.secondhandauction.core.auction.dto.*;
import com.js.secondhandauction.core.auction.exception.AuctionException;
import com.js.secondhandauction.core.auction.repository.AuctionRepository;
import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.domain.State;
import com.js.secondhandauction.core.item.exception.ItemException;
import com.js.secondhandauction.core.item.service.ItemService;
import com.js.secondhandauction.core.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class AuctionService {

    @Autowired
    AuctionRepository auctionRepository;

    @Autowired
    ItemService itemService;

    @Autowired
    MemberService memberService;

    @Autowired
    AuctionFinalizeFailureService auctionFinalizeFailureService;

    final int MIN_BETTING_PERCENT = 5;
    final int USER_MAX_BETTING_TIMES = 5;

    /**
     * 경매 등록
     */
    @Transactional
    public AuctionResponse createAuction(CustomUserDetails customUserDetails, AuctionRequest auctionRequest) {
        Auction auction = auctionRequest.toEntity(customUserDetails);

        Item item = itemService.getItem(auction.getItemNo());

        AuctionLastBidResponse lastBid = auctionRepository.getLastBid(auction.getItemNo());

        //경매 체크
        validateRequest(item, auction, lastBid);

        if (item.getIsBid()) {
            validateForBidItem(item, auction, lastBid);
            updateBidItem(auction, lastBid);
        } else {
            validateFirstItem(item, auction);
            updateFirstItem(auction);
        }

        auctionRepository.create(auction);

        //if (isFinalBid(item)) {
        finishAuction(auction, item);
        //}

        return AuctionResponse.of(auction);
    }

    /**
     * 즉시 구매
     */
    @Transactional
    public AuctionResponse createImmediateAuction(CustomUserDetails customUserDetails, AuctionImmediatePurchaseRequest auctionImmediatePurchaseRequest) {
        Auction auction = auctionImmediatePurchaseRequest.toEntity(customUserDetails);

        Item item = itemService.getItem(auction.getItemNo());

        auction.setBid(item.getImmediatePrice());

        AuctionLastBidResponse lastBid = auctionRepository.getLastBid(auction.getItemNo());

        //경매 체크
        validateRequest(item, auction, lastBid);

        if (item.getIsBid()) {
            updateBidItem(auction, lastBid);
        } else {
            updateFirstItem(auction);
        }

        auctionRepository.create(auction);

        finishAuction(auction, item);

        return AuctionResponse.of(auction);
    }

    /**
     * 경매 조회
     */
    public List<Auction> getAuctions(long itemNo) {
        return auctionRepository.findByItemNo(itemNo);
    }

    /**
     * 경매 공통 체크
     */
    protected void validateRequest(Item item, Auction auction, AuctionLastBidResponse lastBid) {
        if (!State.ONSALE.equals(item.getState())) {
            throw new ItemException(ErrorCode.CAN_BID_ONLY_ONSALE);
        }

        if (auction.getRegId() == item.getRegId()) {
            throw new AuctionException(ErrorCode.CANNOT_BID_MYSELF);
        }

        if (auction.getBid() > item.getImmediatePrice()) {
            throw new AuctionException(ErrorCode.CANNOT_OVER_IMMEDIATEPRICE);
        }

        if (itemService.isItemExpired(item)) {
            auctionFinalizeFailureService.finishExpiredAuction(item, lastBid);
            throw new ItemException(ErrorCode.CANNOT_BID_EXPIRED);
        }
    }

    /**
     * 경매 입찰된 아이템 체크
     */
    protected void validateForBidItem(Item item, Auction auction, AuctionLastBidResponse lastBid) {
        if (auction.getRegId() == lastBid.getRegId()) {
            throw new AuctionException(ErrorCode.DUPLICATE_MEMBER_TICK);
        }

        int minBid = lastBid.getBid() + (item.getRegPrice() * MIN_BETTING_PERCENT / 100);

        if (auction.getBid() < minBid) {
            throw new AuctionException(ErrorCode.NOT_OVER_MINBID);
        }

        if (auctionRepository.getUserBidCount(auction.getItemNo(), auction.getRegId()) >= USER_MAX_BETTING_TIMES) {
            throw new AuctionException(ErrorCode.CANNOT_BETTING_OVER_MAXTIMES);
        }

    }

    /**
     * 경매 입찰된 아이템 업데이트
     */
    protected void updateBidItem(Auction auction, AuctionLastBidResponse lastBid) {
        memberService.updateMemberTotalBalanceByUserNo(auction.getRegId(), auction.getBid() * -1);
        memberService.updateMemberTotalBalanceByUserNo(lastBid.getRegId(), lastBid.getBid());
    }

    /**
     * 경매 입찰전 아이템 체크
     */
    protected void validateFirstItem(Item item, Auction auction) {
        if (auction.getBid() < item.getRegPrice()) {
            throw new AuctionException(ErrorCode.NOT_OVER_MINBID);
        }
    }

    /**
     * 경매 입찰전 아이템 업데이트
     */
    protected void updateFirstItem(Auction auction) {
        memberService.updateMemberTotalBalanceByUserNo(auction.getRegId(), auction.getBid() * -1);
        itemService.updateItemIsBid(auction.getItemNo(), true);
    }

    /**
     * 경매 종료 체크
     */
    protected boolean isFinalBid(Item item) {
        return item.getBetTime() - 1 <= auctionRepository.getTotalBidCount(item.getItemNo());
    }


    /**
     * 경매 종료
     */
    protected void finishAuction(Auction auction, Item item) {
        if (isFinalBid(item)) {
            memberService.updateMemberTotalBalanceByUserNo(item.getRegId(), auction.getBid());

            itemService.updateItemState(item.getItemNo(), State.SOLDOUT);

            log.debug("정상 " + item.getItem() + "(" + item.getItemNo() + ") 판매완료");
        }
    }

    /**
     * 경매 참가자 조회
     */
    public List<AuctionParticipantsResponse> getAuctionParticipants(long itemNo) {
        return auctionRepository.findParticipantsByItemNo(itemNo);
    }

}
