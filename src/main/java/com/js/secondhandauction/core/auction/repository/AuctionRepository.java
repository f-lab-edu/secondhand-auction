package com.js.secondhandauction.core.auction.repository;

import com.js.secondhandauction.core.auction.domain.Auction;
import com.js.secondhandauction.core.auction.dto.AuctionLastBidResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AuctionRepository {

    long create(Auction auction);

    AuctionLastBidResponse getLastBid(long item_no);

    int getUserBidCount(long itemNo, long regId);

    int getTotalBidCount(long itemNo);

    List<Auction> findByItemNo(long itemNo);
}
