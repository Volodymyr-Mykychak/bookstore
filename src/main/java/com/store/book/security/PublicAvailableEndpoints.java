package com.store.book.security;

import java.util.List;

public class PublicAvailableEndpoints {
    private static final List<String> publicEndpoints = List.of(
            "/api/auth/login",
            "/api/auth/registration"
    );

    public static List<String> getPublicEndpoints() {
        return publicEndpoints;
    }
}
