package com.store.book.dto.book;

public record BookSearchParametersDto(String[] title, String[] author, String[] isbn) {
}
