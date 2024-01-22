package com.js.secondhandauction.core.item;

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

    private final long NOT_EXIST_ITEM_NO = 2L;

    private final long SOLDOUT_ITEM_NO = 3L;

    private final long UNSOLD_ITEM_NO = 4L;

    private final long ONSALE_ITEM_NO = 2L;

    private Item item;


    @BeforeEach
    void setup() {
        itemRequest = new ItemRequest("Test Item", 200000);

        item = Item.builder()
                .itemNo(ONSALE_ITEM_NO)
                .item("Test Item")
                .regPrice(200000)
                .regId(USER_ID)
                .state(State.ONSALE)
                .betTime(10)
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
        when(itemRepository.findByItemNo(ONSALE_ITEM_NO)).thenReturn(Optional.ofNullable(item));

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
    @DisplayName("상품 상태를 확인한다")
    void testIsOnSale() {
        when(itemRepository.getState(ONSALE_ITEM_NO)).thenReturn(State.ONSALE);

        Assertions.assertThat(itemService.getItemState(ONSALE_ITEM_NO)).isEqualTo(State.ONSALE);
        Mockito.verify(itemRepository, times(1)).getState(anyLong());
    }

    @Test
    @DisplayName("상품을 수정한다")
    void testUpdateItem() {

        when(itemService.getItemState(ONSALE_ITEM_NO)).thenReturn(State.ONSALE);
        when(itemService.getItemState(SOLDOUT_ITEM_NO)).thenReturn(State.SOLDOUT);
        when(itemService.getItemState(UNSOLD_ITEM_NO)).thenReturn(State.UNSOLD);

        // 상품 수정
        itemService.updateItem(ONSALE_ITEM_NO, USER_ID, itemRequest);

        // 상품 수정
        itemService.updateItem(UNSOLD_ITEM_NO, USER_ID, itemRequest);

        assertThrows(ItemException.class,
                () -> itemService.updateItem(SOLDOUT_ITEM_NO, USER_ID, itemRequest));

    }

}
