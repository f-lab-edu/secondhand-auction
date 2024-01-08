package com.js.secondhandauction.core.user.service;

import com.js.secondhandauction.core.user.domain.User;
import com.js.secondhandauction.core.user.exception.CannotTotalBalanceMinusException;
import com.js.secondhandauction.core.user.exception.NotFoundUserException;
import com.js.secondhandauction.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    final UserRepository userRepository;

    final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.create(user);
        //user.setId(id);
        return user;
    }

    /**
     * 회원 조회
     */
    public User getUser(String id) {
        log.debug("test pw = " + passwordEncoder.encode("test"));

        return userRepository.findById(id).orElseThrow(NotFoundUserException::new);
    }

    /**
     * 회원 가진금액 더하기
     */
    public void updateUserTotalBalance(String id, int totalBalance) {
        if (userRepository.findById(id).orElseThrow(NotFoundUserException::new).getTotalBalance() + totalBalance > 0) {
            userRepository.updateTotalBalance(id, totalBalance);
        } else {
            throw new CannotTotalBalanceMinusException();
        }
    }
}
