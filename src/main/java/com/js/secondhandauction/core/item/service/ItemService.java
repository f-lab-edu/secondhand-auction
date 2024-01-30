package com.js.secondhandauction.core.item.service;

import com.js.secondhandauction.common.exception.ErrorCode;
import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.domain.State;
import com.js.secondhandauction.core.item.dto.ItemRequest;
import com.js.secondhandauction.core.item.dto.ItemResponse;
import com.js.secondhandauction.core.item.exception.ItemException;
import com.js.secondhandauction.core.item.exception.NotFoundItemException;
import com.js.secondhandauction.core.item.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@Slf4j
public class ItemService {

    @Autowired
    ItemRepository itemRepository;

    final int addDay = 1;

    /**
     * 상품등록
     */
    public ItemResponse createItem(long id, ItemRequest itemRequest) {
        Item item = itemRequest.toEntity();

        item.setRegId(id);
        item.setState(State.ONSALE);
        item.setBetTime((int) (Math.random() * 16) + 5);
        item.setIsBid(false);

        itemRepository.create(item);

        return ItemResponse.of(item);
    }

    /**
     * 상품조회
     */
    @Cacheable(key = "#itemNo", value = "ITEM_ITEMNO")
    public Item getItem(long itemNo) {
        return itemRepository.findByItemNo(itemNo).orElseThrow(NotFoundItemException::new);
    }

    /**
     * 상품수정
     */
    public ItemResponse updateItem(long itemNo, long id, ItemRequest itemRequest) {
        Item getItem = isEditableItem(id, itemNo);

        Item item = itemRequest.toEntity();
        item.setItemNo(itemNo);
        item.setRegId(id);
        item.setIsBid(getItem.getIsBid());

        switch (getItem.getState()) {
            case ONSALE:
                if (getItem.getIsBid() && getItem.getRegPrice() != item.getRegPrice()) {
                    throw new ItemException(ErrorCode.CANNOT_UPDATE_BID_ITEM);
                }
                item.setState(State.ONSALE);

                itemRepository.updateForOnsale(item);
                break;
            case UNSOLD:
                item.setState(State.ONSALE);
                item.setBetTime((int) (Math.random() * 16) + 5);

                itemRepository.updateForUnsold(item);
                break;
            case SOLDOUT:
                throw new ItemException(ErrorCode.CANNOT_UPDATE_SOLDOUT_ITEM);
            default:
                throw new ItemException(ErrorCode.NOT_FOUND_ITEM);
        }

        return ItemResponse.of(evictCache(item));
    }

    /**
     * 상품상태 업데이트
     */
    @CacheEvict(key = "#itemNo", value = "ITEM_ITEMNO")
    public void updateItemState(long itemNo, State state) {
        itemRepository.updateState(itemNo, state);
    }

    @CacheEvict(key = "#itemNo", value = "ITEM_ITEMNO")
    public void updateItemIsBid(long itemNo, Boolean isBid) {
        itemRepository.updateIsBid(itemNo, isBid);
    }

    /**
     * 스케줄러를 통한 아이템 상태 업데이트
     */
    public void updateStateCheckItems() {
        log.debug("스케줄러 실행");
        itemRepository.getStateCheckItems(addDay).stream().forEach(itemExpirationCheck -> {
            if (itemExpirationCheck.getLastTick() == null) {
                updateItemState(itemExpirationCheck.getItemNo(), State.UNSOLD);
                log.debug("상품번호: {} 상태: {} 로 변경", itemExpirationCheck.getItemNo(), State.UNSOLD);
            } else if (LocalDateTime.now().isAfter(itemExpirationCheck.getLastTick().plusHours(24L * addDay))) {
                updateItemState(itemExpirationCheck.getItemNo(), State.SOLDOUT);
                log.debug("상품번호: {} 상태: {} 로 변경", itemExpirationCheck.getItemNo(), State.SOLDOUT);
            }
        });
    }

    private Item isEditableItem(long id, long itemNo) {
        Item item = itemRepository.findByItemNo(itemNo).orElseThrow(NotFoundItemException::new);
        if (item.getRegId() != id) {
            throw new ItemException(ErrorCode.EDIT_ONLY_REGID);
        }

        return item;
    }

    @CacheEvict(key = "#itemNo", value = "ITEM_ITEMNO")
    public Item evictCache(Item item) {
        return item;
    }
}
