package com.store.book.controller;

import com.store.book.dto.user.UserRegistrationRequestDto;
import com.store.book.dto.user.UserResponseDto;
import com.store.book.exception.RegistrationException;
import com.store.book.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Endpoints for user registration")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    @Operation(
            summary = "Register a new user",
            description = "Creates an account using email and password",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully registered"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid data or already "
                                    + "registered"
                    )
            }
    )
    @PostMapping("/registration")
    public UserResponseDto registerUser(
            @RequestBody @Valid UserRegistrationRequestDto requestDto
    ) throws RegistrationException {
        return userService.register(requestDto);
    }
}
