package com.store.book.service.impl;

import com.store.book.dto.user.UserRegistrationRequestDto;
import com.store.book.dto.user.UserResponseDto;
import com.store.book.exception.RegistrationException;
import com.store.book.mapper.UserMapper;
import com.store.book.model.User;
import com.store.book.repository.UserRepository;
import com.store.book.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(
            UserRegistrationRequestDto requestDto) throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can't register user");
        }
        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setPassword(requestDto.getPassword());
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setShippingAddress(requestDto.getShippingAddress());
        User savedUser = userRepository.save(user);
        return userMapper.toUserResponse(savedUser);
    }

    @Override
    public UserResponseDto getById(Long id) {
        return userRepository.findById(id).map(userMapper::toUserResponse)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<UserResponseDto> getAll() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Page<UserResponseDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toUserResponse);
    }

    @Override
    public UserResponseDto getByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toUserResponse)
                .orElseThrow(() -> new RuntimeException("Can't find user"));
    }
}
