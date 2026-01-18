package com.store.book.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateOrderRequestDto {
    @NotBlank(message = "Shipping address cannot be blank")
    @Size(min = 10, max = 255, message = "Shipping address must be between 10 and 255 characters")
    @Schema(example = "123 Main St, Kyiv, 01001", description = "Full shipping address for "
            + "delivery")
    private String shippingAddress;
}
