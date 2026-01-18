package com.store.book.dto.order;

import lombok.Data;

@Data
public class OrderItemDto {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private int quantity;
}
