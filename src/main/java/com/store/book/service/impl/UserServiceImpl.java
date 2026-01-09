package com.store.book.service.impl;

import com.store.book.dto.user.UserRegistrationRequestDto;
import com.store.book.dto.user.UserResponseDto;
import com.store.book.exception.RegistrationException;
import com.store.book.mapper.UserMapper;
import com.store.book.model.Role;
import com.store.book.model.RoleName;
import com.store.book.model.User;
import com.store.book.repository.RoleRepository;
import com.store.book.repository.UserRepository;
import com.store.book.service.ShoppingCartService;
import com.store.book.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final ShoppingCartService shoppingCartService;

    @Override
    @Transactional
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("Can't register user. Email "
                                            + requestDto.getEmail() + " is already taken");
        }
        User user = userMapper.toUser(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        Role defaultRole = roleRepository.findByName(RoleName.USER)
                                         .orElseThrow(() -> new IllegalStateException(
                                                 "Default role " + RoleName.USER.name()
                                                 + " was not found in DB"));
        user.setRoles(Set.of(defaultRole));
        User savedUser = userRepository.save(user);
        shoppingCartService.createShoppingCartForUser(savedUser);
        return userMapper.toUserResponse(savedUser);
    }
}
