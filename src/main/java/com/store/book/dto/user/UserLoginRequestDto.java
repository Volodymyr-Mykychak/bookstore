package com.store.book.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
        @NotBlank
        @Email
        @Size(min = 8, max = 255)
        String email,
        @NotBlank
        @Size(min = 8, max = 128)
        String password
) {
}
