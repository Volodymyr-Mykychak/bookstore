package com.store.book.dto.book;

public record BookSearchParametersDto(
        String[] titles,
        String[] authors,
        String[] isbns,
        String[] prices) {
}
