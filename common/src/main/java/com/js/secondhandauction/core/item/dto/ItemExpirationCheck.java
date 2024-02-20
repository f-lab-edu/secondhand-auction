package com.js.secondhandauction.core.item.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ItemExpirationCheck {
    private long itemNo;
    private LocalDateTime lastTick;
}
