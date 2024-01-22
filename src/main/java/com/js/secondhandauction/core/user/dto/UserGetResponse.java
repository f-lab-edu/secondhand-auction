package com.js.secondhandauction.core.user.dto;

import com.js.secondhandauction.core.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserGetResponse {
    private long id;
    private String username;
    private String nickname;
    private int totalBalance;
    private LocalDateTime regDate;
    private LocalDateTime uptDate;

    public static UserGetResponse of(User user) {
        return UserGetResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .totalBalance(user.getTotalBalance())
                .regDate(user.getRegDate())
                .uptDate(user.getUptDate())
                .build();
    }
}
