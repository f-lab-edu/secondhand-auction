package com.js.secondhandauction.core.user.domain;

import com.js.secondhandauction.core.auction.dto.AuctionResponse;
import com.js.secondhandauction.core.user.dto.UserCreateResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Getter
public class User implements UserDetails {
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

    public UserCreateResponse toResponse() {
        return UserCreateResponse.builder()
                .id(id)
                .name(name)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String authority = "ROLE_USER";

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
