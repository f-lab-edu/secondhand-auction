package com.js.secondhandauction.core.item;

import com.js.secondhandauction.common.exception.ErrorCode;
import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.domain.State;
import com.js.secondhandauction.core.item.dto.ItemRequest;
import com.js.secondhandauction.core.item.dto.ItemResponse;
import com.js.secondhandauction.core.item.exception.ItemException;
import com.js.secondhandauction.core.item.exception.NotFoundItemException;
import com.js.secondhandauction.core.item.repository.ItemRepository;
import com.js.secondhandauction.core.item.service.ItemService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    private ItemRequest itemRequest;

    private final long USER_ID = 1L;

    private final long NOT_REGISTED_USER_ID = 2L;

    private final long NOT_EXIST_ITEM_NO = 2L;

    private final long SOLDOUT_ITEM_NO = 3L;

    private final long UNSOLD_ITEM_NO = 4L;

    private final long ONSALE_ITEM_NO = 5L;

    private final long ONSALE_IS_BID_ITEM_NO = 6L;

    private Item onsaleItem;

    private Item onsaleIsBidItem;

    private Item soldoutItem;

    private Item unsoldItem;


    @BeforeEach
    void setup() {
        itemRequest = new ItemRequest("Test Item", 200000);

        onsaleItem = Item.builder()
                .itemNo(ONSALE_ITEM_NO)
                .item("Test Item")
                .regPrice(200000)
                .regId(USER_ID)
                .state(State.ONSALE)
                .betTime(10)
                .isBid(false)
                .build();

        onsaleIsBidItem = Item.builder()
                .itemNo(ONSALE_ITEM_NO)
                .item("Test Item")
                .regPrice(300000)
                .regId(USER_ID)
                .state(State.ONSALE)
                .betTime(10)
                .isBid(true)
                .build();

        soldoutItem = Item.builder()
                .itemNo(ONSALE_ITEM_NO)
                .item("Test Item")
                .regPrice(200000)
                .regId(USER_ID)
                .state(State.SOLDOUT)
                .betTime(10)
                .isBid(false)
                .build();

        unsoldItem = Item.builder()
                .itemNo(ONSALE_ITEM_NO)
                .item("Test Item")
                .regPrice(200000)
                .regId(USER_ID)
                .state(State.UNSOLD)
                .betTime(10)
                .isBid(false)
                .build();
    }

    @Test
    @DisplayName("상품을 생성한다")
    void testCreateItem() {
        ItemResponse createdItem = itemService.createItem(USER_ID, itemRequest);

        assertNotNull(createdItem);
        Assertions.assertThat(State.ONSALE).isEqualTo(createdItem.getState());
        Assertions.assertThat("Test Item").isEqualTo(createdItem.getItem());

        Mockito.verify(itemRepository, times(1)).create(any(Item.class));
    }

    @Test
    @DisplayName("상품을 조회한다")
    void testGetItem() {
        //정상 조회
        when(itemRepository.findByItemNo(ONSALE_ITEM_NO)).thenReturn(Optional.ofNullable(onsaleItem));

        Item getItem = itemService.getItem(ONSALE_ITEM_NO);

        assertNotNull(getItem);
        Assertions.assertThat("Test Item").isEqualTo(getItem.getItem());
        Mockito.verify(itemRepository, times(1)).findByItemNo(anyLong());

        //상품이 존재하지 않아 조회에 실패
        when(itemRepository.findByItemNo(NOT_EXIST_ITEM_NO)).thenReturn(Optional.empty());

        assertThrows(NotFoundItemException.class,
                () -> itemService.getItem(NOT_EXIST_ITEM_NO));
    }

    @Test
    @DisplayName("상품을 수정한다")
    void testUpdateItem() {

        when(itemRepository.findByItemNo(ONSALE_ITEM_NO)).thenReturn(Optional.ofNullable(onsaleItem));
        when(itemRepository.findByItemNo(SOLDOUT_ITEM_NO)).thenReturn(Optional.ofNullable(soldoutItem));
        when(itemRepository.findByItemNo(UNSOLD_ITEM_NO)).thenReturn(Optional.ofNullable(unsoldItem));
        when(itemRepository.findByItemNo(ONSALE_IS_BID_ITEM_NO)).thenReturn(Optional.ofNullable(onsaleIsBidItem));

        //등록자만 상품을 수정할 수 있다.
        assertThrows(ItemException.class,
                () -> itemService.updateItem(ONSALE_ITEM_NO, NOT_REGISTED_USER_ID, itemRequest), ErrorCode.EDIT_ONLY_REGID.getMessage());

        // 상품 수정
        itemService.updateItem(ONSALE_ITEM_NO, USER_ID, itemRequest);

        // 상품 수정
        itemService.updateItem(UNSOLD_ITEM_NO, USER_ID, itemRequest);

        //입찰이 이미 시작된 아이템의 가격은 수정할 수 없다.
        assertThrows(ItemException.class,
                () -> itemService.updateItem(ONSALE_IS_BID_ITEM_NO, USER_ID, itemRequest), ErrorCode.CANNOT_UPDATE_BID_ITEM.getMessage());

        //판매 완료된 아이템은 수정할 수 없다.
        assertThrows(ItemException.class,
                () -> itemService.updateItem(SOLDOUT_ITEM_NO, USER_ID, itemRequest), ErrorCode.CANNOT_UPDATE_SOLDOUT_ITEM.getMessage());

    }

}
