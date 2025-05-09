package com.store.book.security;

public interface Authentication {
    Object getPrincipal();

    Object getCredentials();
}
