package com.js.secondhandauction.core.auction.service;

import com.js.secondhandauction.core.auction.dto.AuctionLastBidResponse;
import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.domain.State;
import com.js.secondhandauction.core.item.exception.ItemException;
import com.js.secondhandauction.core.item.service.ItemService;
import com.js.secondhandauction.core.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AuctionFinalizeFailureService {
    @Autowired
    MemberService memberService;

    @Autowired
    ItemService itemService;

    /**
     * 만료 경매 종료
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = {ItemException.class})
    public void finishExpiredAuction(Item item, AuctionLastBidResponse lastBid) {
        if (item.getIsBid()) {
            memberService.updateMemberTotalBalanceByUserNo(lastBid.getRegId(), lastBid.getBid());
            itemService.updateItemState(item.getItemNo(), State.SOLDOUT);
            log.debug("시간 경과로 인한 아이템 " + item.getItem() + "(" + item.getItemNo() + ") 판매완료");
        } else {
            itemService.updateItemState(item.getItemNo(), State.UNSOLD);
            log.debug("시간 경과로 인한 아이템 " + item.getItem() + "(" + item.getItemNo() + ") 유찰");
        }
    }
}
