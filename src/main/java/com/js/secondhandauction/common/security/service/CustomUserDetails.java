package com.js.secondhandauction.common.security.service;

import com.js.secondhandauction.core.member.domain.Member;
import com.js.secondhandauction.core.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails, Serializable {

    private long uniqId;
    private String userId;
    private String password;
    @Getter
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    public long getId() {
        return uniqId;
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

    public static CustomUserDetails of(final Member member) {
        return new CustomUserDetails(member.getUniqId(), member.getUserId(), member.getPassword(), member.getRole());
    }
}
