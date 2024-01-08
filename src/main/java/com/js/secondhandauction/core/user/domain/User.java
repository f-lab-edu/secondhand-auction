package com.js.secondhandauction.core.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class User {
    private String id;
    private String name;
    private String password;
    private int totalBalance = 0;
    private LocalDateTime regDate;
    private LocalDateTime uptDate;

    @Builder
    public User(String id,
                String name,
                String password,
                int totalBalance,
                LocalDateTime regDate,
                LocalDateTime uptDate
    ) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.totalBalance = totalBalance;
        this.regDate = regDate;
        this.uptDate = uptDate;
    }
}
