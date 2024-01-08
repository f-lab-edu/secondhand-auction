package com.js.secondhandauction.core.user.repository;

import com.js.secondhandauction.core.user.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserRepository {
    String create(User user);

    Optional<User> findById(String id);

    void updateTotalBalance(String id, int totalBalance);

}
