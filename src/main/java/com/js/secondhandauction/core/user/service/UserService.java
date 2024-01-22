package com.js.secondhandauction.core.user.service;

import com.js.secondhandauction.common.exception.ErrorCode;
import com.js.secondhandauction.core.user.domain.User;
import com.js.secondhandauction.core.user.dto.UserCreateRequest;
import com.js.secondhandauction.core.user.dto.UserCreateResponse;
import com.js.secondhandauction.core.user.dto.UserGetResponse;
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
        checkUsernameValidity(userCreateRequest);

        User user = User.builder()
                .username(userCreateRequest.username())
                .nickname(userCreateRequest.nickname())
                .password(passwordEncoder.encode(userCreateRequest.password()))
                .build();

        userRepository.create(user);

        return UserCreateResponse.of(user);
    }

    /**
     * 회원 조회
     */
    public UserGetResponse getUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(NotFoundUserException::new);
        return UserGetResponse.of(user);
    }

    /**
     * 회원 가진금액 더하기 Username 으로
     */
    public void updateUserTotalBalanceByUsername(String username, int totalBalance) {
        if (userRepository.findByUsername(username).orElseThrow(NotFoundUserException::new).getTotalBalance() + totalBalance > 0) {
            userRepository.updateTotalBalance(username, totalBalance);
        } else {
            throw new CannotTotalBalanceMinusException();
        }
    }

    /**
     * 회원 가진금액 더하기 id로
     */
    public void updateUserTotalBalanceById(long id, int totalBalance) {
        User user = userRepository.findById(id).orElseThrow(NotFoundUserException::new);

        if (user.getTotalBalance() + totalBalance > 0) {
            userRepository.updateTotalBalance(user.getUsername(), totalBalance);
        } else {
            throw new CannotTotalBalanceMinusException();
        }
    }

    /**
     * 회원 중복 체크
     */
    private void checkUsernameValidity(UserCreateRequest userCreateRequest) {
        if (userRepository.findByUsername(userCreateRequest.username()).isPresent()) {
            throw new UserException(ErrorCode.ALREADY_EXIST_USER);
        }
    }
}
