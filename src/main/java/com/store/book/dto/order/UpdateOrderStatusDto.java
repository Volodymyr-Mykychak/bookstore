package com.store.book.dto.order;

import com.store.book.model.Order;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusDto {
    @NotNull(message = "Status cannot be null")
    private Order.Status status;
}
