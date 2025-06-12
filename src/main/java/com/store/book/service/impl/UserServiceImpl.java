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
import com.store.book.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(
            UserRegistrationRequestDto requestDto) throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("User already exists with email");
        }
        User user = userMapper.toUser(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        Role role = roleRepository.findByName(RoleName.USER).orElseThrow(
                () -> new IllegalStateException("Role USER not found"));
        user.setRoles(Set.of(role));
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }
}
