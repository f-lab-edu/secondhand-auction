package com.js.secondhandauction.core.item;

import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.domain.State;
import com.js.secondhandauction.core.item.dto.ItemRequest;
import com.js.secondhandauction.core.item.dto.ItemResponse;
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

    private Item item;

    private ItemRequest itemRequest;

    private final String USER_ID = "1";

    @BeforeEach
    void setup() {
        item = Item.builder()
                .item("Test Item")
                .regId(USER_ID)
                .regPrice(200000)
                .build();

        itemRequest = new ItemRequest("Test Item", 200000);
    }

    @Test
    @DisplayName("상품을 생성한다")
    void testCreateItem() {
        when(itemRepository.create(any(Item.class))).thenReturn(anyLong());

        ItemResponse createdItem = itemService.createItem(USER_ID, itemRequest);

        assertNotNull(createdItem);
        Assertions.assertThat(State.ONSALE).isEqualTo(createdItem.getState());

        Mockito.verify(itemRepository, times(1)).create(any(Item.class));
    }

    @Test
    @DisplayName("상품을 조회한다")
    void testGetItem() {
        when(itemRepository.findByItemNo(anyLong())).thenReturn(Optional.ofNullable(item));

        Item getItem = itemService.getItem(1L);

        assertNotNull(getItem);
        Assertions.assertThat("Test Item").isEqualTo(getItem.getItem());
        Mockito.verify(itemRepository, times(1)).findByItemNo(anyLong());
    }

    @Test
    @DisplayName("상품이 존재하지 않아 조회에 실패한다")
    void testGetItemThrowNotFoundItemException() {
        when(itemRepository.findByItemNo(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundItemException.class,
                () -> itemService.getItem(1L));
    }

    @Test
    @DisplayName("상품 상태를 확인한다")
    void testIsOnSale() {
        when(itemRepository.getState(anyLong())).thenReturn(State.ONSALE);

        Assertions.assertThat(itemService.getItemState(1L)).isEqualTo(State.ONSALE);
        Mockito.verify(itemRepository, times(1)).getState(anyLong());
    }

    @Test
    @DisplayName("상품 상태를 변경한다")
    void testUpdateState() {
        // 상품 생성
        when(itemRepository.create(any(Item.class))).thenReturn(anyLong());
        ItemResponse createdItem = itemService.createItem(USER_ID, itemRequest);

        // 상태 변경
        when(itemRepository.updateState(anyLong(), any(State.class))).thenReturn(1);
        itemService.updateItemState(createdItem.getItemNo(), State.SOLDOUT);

        // 상태 확인
        when(itemRepository.getState(anyLong())).thenReturn(State.SOLDOUT); // getState() 메소드를 Mocking
        Assertions.assertThat(itemService.getItemState(createdItem.getItemNo())).isEqualTo(State.SOLDOUT);

        // Mocking된 메소드가 호출되었는지 검증
        Mockito.verify(itemRepository, times(1)).updateState(anyLong(), any(State.class));
        Mockito.verify(itemRepository, times(1)).getState(anyLong());
    }

    @Test
    @DisplayName("상품을 수정한다")
    void testUpdateItem() {
        // 상품 생성
        when(itemRepository.create(any(Item.class))).thenReturn(anyLong());
        ItemResponse createdItem = itemService.createItem(USER_ID, itemRequest);

        when(itemRepository.getState(anyLong())).thenReturn(State.UNSOLD);

        // 상품 수정
        when(itemRepository.updateForUnsold(any(Item.class))).thenReturn(1);
        ItemRequest itemUpdateRequest = new ItemRequest("Updated Item", 300000);
        ItemResponse imemUpadteRequest = itemService.updateItem(createdItem.getItemNo(), USER_ID, itemUpdateRequest);

        Assertions.assertThat(imemUpadteRequest.getItem()).isEqualTo("Updated Item");

        // Mocking된 메소드가 호출되었는지 검증
        Mockito.verify(itemRepository, times(1)).updateForUnsold(any(Item.class));
    }

}
