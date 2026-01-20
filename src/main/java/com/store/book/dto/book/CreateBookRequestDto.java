package com.store.book.dto.book;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateBookRequestDto {
    @NotBlank(message = "Book title must not be empty")
    @Size(min = 2, message = "Book title must be at least 2 characters long")
    private String title;
    @NotBlank(message = "Author must not be empty")
    private String author;
    @NotBlank(message = "ISBN must not be empty")
    @Pattern(regexp = "^(97(8|9))?\\d{9}(\\d|X)$", message = "Invalid ISBN format")
    private String isbn;
    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.1", inclusive = true, message = "Price must be greater than 0")
    private BigDecimal price;
    @Size(max = 500, message = "Book description must not exceed 500 characters")
    private String description;
    private String coverImage;
    @NotNull(message = "Quantity must not be null")
    @PositiveOrZero(message = "Quantity must be zero or greater")
    private Integer quantity;
    @NotEmpty(message = "Book must have at least one category")
    @Size(min = 1, message = "Book must belong to at least one category")
    private List<Long> categoryIds;
}
