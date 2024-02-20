package com.js.secondhandauction.core.item.repository;

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

    void update(Item item);

    void updateState(long itemNo, State state);

    void updateIsBid(long itemNo, Boolean isBid);

    void delete(long itemNo);

    List<ItemExpirationCheck> getStateCheckItems(int addDay);
}
