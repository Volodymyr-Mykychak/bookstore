package com.store.book.dto.category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateCategoryRequestDto {
    @NotBlank(message = "Category name must not be empty")
    @Size(min = 2, message = "Category name must be at least 2 characters long")
    @Schema(example = "Science Fiction", description = "Name of the book category")
    private String name;
    @Schema(example = "Books about futuristic science and technology", description = "Detailed "
            + "description of the category")
    private String description;
}
