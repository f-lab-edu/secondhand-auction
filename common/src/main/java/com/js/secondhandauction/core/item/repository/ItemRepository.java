package com.js.secondhandauction.core.item.repository;

import com.js.secondhandauction.core.auction.domain.Auction;
import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.domain.State;
import com.js.secondhandauction.core.item.dto.ItemExpirationCheck;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ItemRepository {
    long create(Item item);

    Optional<Item> findByItemNo(long itemNo);

    int update(Item item);

    int updateState(long itemNo, State state);

    int updateIsBid(long itemNo, Boolean isBid);

    int delete(long itemNo);

    List<ItemExpirationCheck> getStateCheckItems(int addDay);
}
