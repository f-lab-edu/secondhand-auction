package com.js.secondhandauction.core.item.service;

import com.js.secondhandauction.common.exception.ErrorCode;
import com.js.secondhandauction.common.security.service.CustomUserDetails;
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
    public ItemResponse createItem(CustomUserDetails customUserDetails, ItemRequest itemRequest) {
        Item item = itemRequest.toEntity();

        item.setRegId(customUserDetails.getId());
        item.setState(State.ONSALE);
        item.setBetTime(makeItemBettime());
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
    public ItemResponse updateItem(long itemNo, CustomUserDetails customUserDetails, ItemRequest itemRequest) {
        Item getItem = itemRepository.findByItemNo(itemNo).orElseThrow(NotFoundItemException::new);

        validateEditableItem(customUserDetails.getId(), getItem);

        Item item = itemRequest.toEntity();
        item.setItemNo(itemNo);
        item.setRegId(customUserDetails.getId());
        item.setIsBid(getItem.getIsBid());
        item.setState(State.ONSALE);

        if (getItem.getIsBid() && getItem.getRegPrice() != item.getRegPrice() && getItem.getImmediatePrice() != item.getImmediatePrice()) {
            throw new ItemException(ErrorCode.CANNOT_CHANGE_ITEM);
        }

        if (getItem.getState() == State.UNSOLD) {
            throw new ItemException(ErrorCode.CANNOT_CHANGE_ITEM);
        }

        itemRepository.update(item);

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

    /**
     * 상품삭제
     */
    public void deleteItem(long itemNo, CustomUserDetails customUserDetails) {
        Item item = itemRepository.findByItemNo(itemNo).orElseThrow(NotFoundItemException::new);

        validateEditableItem(customUserDetails.getId(), item);

        if (item.getIsBid() && item.getState() == State.ONSALE) {
            throw new ItemException(ErrorCode.CANNOT_CHANGE_ITEM);
        }

        itemRepository.delete(itemNo);
    }

    private void validateEditableItem(long id, Item item) {
        if (item.getRegId() != id) {
            throw new ItemException(ErrorCode.EDIT_ONLY_REGID);
        }

        if (item.getState() == State.SOLDOUT) {
            throw new ItemException(ErrorCode.CANNOT_CHANGE_ITEM);
        }

    }

    /**
     * 상품 입찰 횟수 생성
     *
     * @return 20 ~ 50 사이 숫자
     */
    private int makeItemBettime() {
        return (int) (Math.random() * 20) + 30;
    }

    public boolean isItemExpired(Item item) {
        return item.getRegDate().plusHours(24L * addDay).isBefore(LocalDateTime.now());
    }

    @CacheEvict(key = "#item.itemNo", value = "ITEM_ITEMNO")
    public Item evictCache(Item item) {
        return item;
    }
}
