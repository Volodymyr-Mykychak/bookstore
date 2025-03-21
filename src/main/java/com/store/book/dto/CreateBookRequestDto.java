package com.store.book.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // Ігнорує невідомі поля у JSON
public class CreateBookRequestDto {
    @NotBlank(message = "Назва книги не може бути пустою") // Забороняє null і пустий рядок
    @Size(min = 2, message = "Назва книги повинна містити мінімум 2 символи")
    private String title;
    @NotBlank(message = "Автор книги не може бути пустим")
    private String author;
    @NotBlank(message = "ISBN не може бути пустим")
    @Pattern(regexp = "^(97(8|9))?\\d{9}(\\d|X)$", message = "Некоректний формат ISBN")
    private String isbn;
    @NotNull(message = "Ціна не може бути відсутня")
    @DecimalMin(value = "0.1", inclusive = true, message = "Ціна повинна бути більше 0")
    private BigDecimal price;
    @Size(max = 500, message = "Опис книги не може бути довшим за 500 символів")
    private String description;
    @NotBlank(message = "Посилання на обкладинку книги не може бути порожнім")
    private String coverImage;
}

