package com.js.secondhandauction.core.auction.repository;

import com.js.secondhandauction.core.auction.domain.Auction;
import com.js.secondhandauction.core.item.domain.Item;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AuctionRepository {

    long create(Auction auction);

    Auction getLastTick(long item_no);

    int getCountTick(long item_no);

    List<Auction> findByItemNo(long itemNo);
}
