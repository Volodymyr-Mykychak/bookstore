package com.store.book.util;

import com.store.book.dto.book.BookDto;
import com.store.book.dto.book.CreateBookRequestDto;
import com.store.book.dto.category.CategoryDto;
import com.store.book.dto.category.CreateCategoryRequestDto;
import com.store.book.model.Book;
import com.store.book.model.Category;
import java.math.BigDecimal;
import java.util.List;

public class TestDataHelper {

    public static CreateBookRequestDto createBookRequestDto() {
        CreateBookRequestDto dto = new CreateBookRequestDto();
        dto.setTitle("Effective Java");
        dto.setAuthor("Joshua Bloch");
        dto.setIsbn("9780134685991");
        dto.setPrice(new BigDecimal("1000.00"));
        dto.setCategoryIds(List.of(1L));
        return dto;
    }

    public static Book createBook(CreateBookRequestDto dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPrice(dto.getPrice());
        book.setIsbn(dto.getIsbn());
        return book;
    }

    public static BookDto createBookDto(Long id, Book book) {
        BookDto dto = new BookDto();
        dto.setId(id);
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setPrice(book.getPrice());
        dto.setIsbn(book.getIsbn());
        return dto;
    }

    public static CreateCategoryRequestDto createCategoryRequestDto() {
        CreateCategoryRequestDto dto = new CreateCategoryRequestDto();
        dto.setName("Fiction");
        dto.setDescription("Fiction books description");
        return dto;
    }

    public static Category createCategory(Long id, CreateCategoryRequestDto dto) {
        Category category = new Category();
        category.setId(id);
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return category;
    }

    public static CategoryDto createCategoryDto(Long id, String name) {
        CategoryDto dto = new CategoryDto();
        dto.setId(id);
        dto.setName(name);
        return dto;
    }

    public static Book createBookWithQuantity(String title, int quantity) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor("Author");
        book.setIsbn("ISBN-" + title.hashCode());
        book.setPrice(BigDecimal.valueOf(100));
        book.setQuantity(quantity);
        return book;
    }
}
