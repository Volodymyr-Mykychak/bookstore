package com.store.book.dto.book;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateBookRequestDto {
    private String title;
    private String isbn;
    private BigDecimal price;
    private String description;
    private String coverImage;
    private Long authorId;
}
