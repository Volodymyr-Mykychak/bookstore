package com.store.book.service.impl;

import com.store.book.dto.user.UserRegistrationRequestDto;
import com.store.book.dto.user.UserResponseDto;
import com.store.book.exception.RegistrationException;
import com.store.book.mapper.UserMapper;
import com.store.book.model.User;
import com.store.book.repository.UserRepository;
import com.store.book.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(
            UserRegistrationRequestDto requestDto) throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("Can't register user");
        }
        User user = userMapper.toUser(requestDto);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }
}
