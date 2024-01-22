package com.js.secondhandauction.core.user;

import com.js.secondhandauction.core.user.domain.User;
import com.js.secondhandauction.core.user.dto.UserCreateRequest;
import com.js.secondhandauction.core.user.dto.UserCreateResponse;
import com.js.secondhandauction.core.user.dto.UserGetResponse;
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
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;

    private UserCreateRequest userCreateRequest;

    @BeforeEach
    void setup() {
        user = User.builder()
                .username("Test User")
                .nickname("Test Name")
                .build();

        userCreateRequest = new UserCreateRequest("Test User", "Test Name", "pw");

    }


    @Test
    @DisplayName("유저를 생성한다")
    void testCreateUser() {
        UserCreateResponse createdUser = userService.createUser(userCreateRequest);

        assertNotNull(createdUser);
        Assertions.assertThat("Test User").isEqualTo(createdUser.getUsername());
        Assertions.assertThat("Test Name").isEqualTo(createdUser.getNickname());
        Mockito.verify(userRepository, times(1)).create(any(User.class));
    }

    @Test
    @DisplayName("유저를 조회한다")
    void testGetUser() {
        //정상 조회
        when(userRepository.findByUsername("10")).thenReturn(Optional.ofNullable(user));

        UserGetResponse getUser = userService.getUser("10");

        Assertions.assertThat("Test User").isEqualTo(getUser.getUsername());
        Mockito.verify(userRepository, times(1)).findByUsername(anyString());

        //조회 실패
        when(userRepository.findByUsername("10")).thenReturn(Optional.empty());

        assertThrows(NotFoundUserException.class,
                () -> userService.getUser("10"));
    }

    @Test
    @DisplayName("유저의 자금을 변경한다")
    void testUpdateTotalBalance() {
        //정상 자금 변경
        when(userRepository.findById(10L)).thenReturn(Optional.ofNullable(user));

        userService.updateUserTotalBalanceById(10L, 500000);

        Mockito.verify(userRepository, times(1)).updateTotalBalance(anyString(), anyInt());

        //사용자 자금보다 더 큰 금액을 빼려 해 변경에 실패
        when(userRepository.findById(10L)).thenReturn(Optional.ofNullable(user));

        assertThrows(CannotTotalBalanceMinusException.class,
                () -> userService.updateUserTotalBalanceById(10L, -20000000));

    }

}
