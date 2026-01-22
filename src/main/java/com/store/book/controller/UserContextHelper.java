package com.store.book.controller;

import com.store.book.model.User;
import org.springframework.security.core.Authentication;

public interface UserContextHelper {
    default Long getUserId(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new RuntimeException("User is not authenticated");
        }
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
}
