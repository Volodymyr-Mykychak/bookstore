package com.store.book.security;

import com.store.book.dto.user.UserLoginRequestDto;
import com.store.book.model.User;
import com.store.book.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;

    public boolean authenticate(UserLoginRequestDto requestDto) {
        Optional<User> user = userRepository.findByEmail(requestDto.email());
        return (user.isPresent() && user.get().getPassword().equals(requestDto.password()));
    }
}
