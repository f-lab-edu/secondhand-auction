package com.js.secondhandauction.core.user.service;

import com.js.secondhandauction.common.exception.ErrorCode;
import com.js.secondhandauction.core.user.domain.User;
import com.js.secondhandauction.core.user.dto.UserCreateRequest;
import com.js.secondhandauction.core.user.dto.UserCreateResponse;
import com.js.secondhandauction.core.user.exception.CannotTotalBalanceMinusException;
import com.js.secondhandauction.core.user.exception.NotFoundUserException;
import com.js.secondhandauction.core.user.exception.UserException;
import com.js.secondhandauction.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    final UserRepository userRepository;

    final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    public UserCreateResponse createUser(UserCreateRequest userCreateRequest) {
        User user = User.builder()
                .id(userCreateRequest.id())
                .name(userCreateRequest.name())
                .password(passwordEncoder.encode(userCreateRequest.password()))
                .build();

        try {
            userRepository.create(user);
        } catch (DataAccessException e) {
            throw new UserException(ErrorCode.ALREADY_EXIST_USER);
        }

        return user.toResponse();
    }

    /**
     * 회원 조회
     */
    public User getUser(String id) {
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
