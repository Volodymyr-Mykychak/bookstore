package com.store.book.security;

import com.store.book.model.User;
import com.store.book.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailWithRoles(email)
                                  .orElseThrow(() -> new UsernameNotFoundException(
                                          "User not found with email: " + email));
        return org.springframework.security.core.userdetails.User.builder()
                                                                 .username(user.getEmail())
                                                                 .password(user.getPassword())
                                                                 .roles(user.getRoles().stream()
                                                                            .map(role -> role
                                                                                    .getName()
                                                                                    .name())
                                                                            .toArray(String[]::new))
                                                                 .build();
    }
}

