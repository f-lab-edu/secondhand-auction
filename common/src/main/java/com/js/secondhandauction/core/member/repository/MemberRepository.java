package com.js.secondhandauction.core.member.repository;

import com.js.secondhandauction.core.member.domain.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberRepository {
    void create(Member user);

    Optional<Member> findByUserId(String userId);

    Optional<Member> findByUserNo(long id);

    void updateTotalBalance(String userId, int totalBalance);

    void updatePassword(String userId, String password);
}
