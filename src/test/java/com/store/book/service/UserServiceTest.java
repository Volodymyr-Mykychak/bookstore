package com.store.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.store.book.dto.user.UserRegistrationRequestDto;
import com.store.book.dto.user.UserResponseDto;
import com.store.book.exception.RegistrationException;
import com.store.book.mapper.UserMapper;
import com.store.book.model.Role;
import com.store.book.model.RoleName;
import com.store.book.model.User;
import com.store.book.repository.RoleRepository;
import com.store.book.repository.UserRepository;
import com.store.book.service.impl.UserServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @Mock
    private ShoppingCartService shoppingCartService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Register: Success scenario")
    void register_ValidRequest_ShouldReturnResponseDto() {
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setPassword("password");

        User user = new User();
        user.setEmail(requestDto.getEmail());

        Role role = new Role();
        role.setName(RoleName.ROLE_USER);

        UserResponseDto expectedResponse = new UserResponseDto();
        expectedResponse.setEmail(user.getEmail());

        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(false);
        when(userMapper.toUser(requestDto)).thenReturn(user);
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("encoded_password");
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.of(role));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserResponse(user)).thenReturn(expectedResponse);

        UserResponseDto actual = userService.register(requestDto);

        assertThat(actual).isEqualTo(expectedResponse);
        verify(shoppingCartService).createShoppingCartForUser(user);
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Register: Should throw RegistrationException when email exists")
    void register_EmailAlreadyExists_ShouldThrowException() {
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto();
        requestDto.setEmail("existing@example.com");

        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(true);

        assertThrows(RegistrationException.class, () -> userService.register(requestDto));
    }

    @Test
    @DisplayName("Register: Should throw IllegalStateException when role not found")
    void register_RoleNotFound_ShouldThrowException() {
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto();
        requestDto.setEmail("new@example.com");

        User user = new User();

        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(false);
        when(userMapper.toUser(requestDto)).thenReturn(user);
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> userService.register(requestDto));
    }
}
