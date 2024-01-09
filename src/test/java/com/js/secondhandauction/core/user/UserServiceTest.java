package com.js.secondhandauction.core.user;

import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.user.domain.User;
import com.js.secondhandauction.core.user.dto.UserCreateRequest;
import com.js.secondhandauction.core.user.dto.UserCreateResponse;
import com.js.secondhandauction.core.user.exception.CannotTotalBalanceMinusException;
import com.js.secondhandauction.core.user.exception.NotFoundUserException;
import com.js.secondhandauction.core.user.repository.UserRepository;
import com.js.secondhandauction.core.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private User user;

    private UserCreateRequest userCreateRequest;

    @BeforeEach
    void setup() {
        user = User.builder()
                .name("Test User")
                .build();

        userCreateRequest = new UserCreateRequest("testID", "Test User", "pw");

    }


    @Test
    @DisplayName("유저를 생성한다")
    void testCreateUser() {
        //when(userRepository.create(any(User.class))).thenReturn(anyString());

        UserCreateResponse createdUser = userService.createUser(userCreateRequest);

        assertNotNull(createdUser);
        Assertions.assertThat(10000000).isEqualTo(user.getTotalBalance());
        Mockito.verify(userRepository, times(1)).create(any(User.class));
    }

    @Test
    @DisplayName("유저를 조회한다")
    void testGetUser() {
        when(userRepository.findById(anyString())).thenReturn(Optional.ofNullable(user));

        User getUser = userService.getUser("10");

        Assertions.assertThat("Test User").isEqualTo(getUser.getName());
        Mockito.verify(userRepository, times(1)).findById(anyString());
    }

    @Test
    @DisplayName("유저가 없어 유저 조회를 실패한다")
    void testGetUserThrowNotFoundUserException() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundUserException.class,
                () -> userService.getUser("10"));
    }

    @Test
    @DisplayName("사용자의 자금을 변경한다")
    void testUpdateTotalBalance() {
        when(userRepository.findById(anyString())).thenReturn(Optional.ofNullable(user));

        userService.updateUserTotalBalance("10", 500000);

        Mockito.verify(userRepository, times(1)).updateTotalBalance(anyString(), anyInt());
    }

    @Test
    @DisplayName("사용자 자금보다 더 큰 금액을 빼려 해 변경에 실패한다")
    void testUpdateTotalBalanceThrowException() {
        when(userRepository.findById(anyString())).thenReturn(Optional.ofNullable(user));

        assertThrows(CannotTotalBalanceMinusException.class,
                () -> userService.updateUserTotalBalance("10", -20000000));
    }

}
