package com.store.book.service;

import com.store.book.dto.user.UserRegistrationRequestDto;
import com.store.book.dto.user.UserResponseDto;
import com.store.book.exception.RegistrationException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;

    UserResponseDto getById(Long id);

    List<UserResponseDto> getAll();

    void deleteById(Long id);

    Page<UserResponseDto> findAll(Pageable pageable);

    UserResponseDto getByEmail(String email);
}
