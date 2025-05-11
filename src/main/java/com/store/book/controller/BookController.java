package com.store.book.controller;

import com.store.book.dto.book.BookDto;
import com.store.book.dto.book.BookSearchParametersDto;
import com.store.book.dto.book.CreateBookRequestDto;
import com.store.book.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Books", description = "Endpoints for managing books")
@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @PostMapping
    @Operation(
            summary = "Create a new book",
            description = "Creates a book with title, author, ISBN, price and more",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Book successfully created"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data"
                    )
            }
    )
    public BookDto save(@Valid @RequestBody CreateBookRequestDto bookDto) {
        return bookService.save(bookDto);
    }

    @GetMapping
    @Operation(
            summary = "Get all books",
            description = "Returns paginated list of all books"
    )
    public Page<BookDto> findAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get book by ID",
            description = "Returns a single book by its ID"
    )
    public BookDto get(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete book by ID",
            description = "Marks book as deleted"
    )
    public void delete(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search books",
            description = "Filters books by author, title or ISBN"
    )
    public List<BookDto> searchBooks(BookSearchParametersDto searchParameters) {
        return bookService.search(searchParameters);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update book by ID",
            description = "Updates book details by given ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Book successfully updated"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Book not found"
                    )
            }
    )
    public BookDto update(
            @PathVariable Long id,
            @Valid @RequestBody CreateBookRequestDto bookDto
    ) {
        return bookService.update(id, bookDto);
    }
}
