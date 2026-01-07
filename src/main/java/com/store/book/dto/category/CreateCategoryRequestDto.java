package com.store.book.dto.category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateCategoryRequestDto {
    @NotBlank(message = "Category name must not be empty")
    @Size(min = 2, message = "Category name must be at least 2 characters long")
    private String name;
    private String description;
}
