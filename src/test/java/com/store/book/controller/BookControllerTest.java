package com.store.book.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.book.dto.book.CreateBookRequestDto;
import com.store.book.util.TestDataHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create book: Valid request")
    @Sql(scripts = {
            "classpath:database/books/delete-books.sql",
            "classpath:database/categories/delete-categories.sql",
            "classpath:database/categories/add-fiction-category.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/delete-books.sql",
            "classpath:database/categories/delete-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_ValidRequestDto_ShouldReturnCreatedBook() throws Exception {
        CreateBookRequestDto requestDto = TestDataHelper.createBookRequestDto();

        mockMvc.perform(post("/books")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(requestDto.getTitle()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create book: Invalid data")
    void createBook_InvalidData_ReturnsBadRequest() throws Exception {
        CreateBookRequestDto invalidRequest = new CreateBookRequestDto();

        mockMvc.perform(post("/books")
                        .content(objectMapper.writeValueAsString(invalidRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Find all: Returns page of books")
    @Sql(scripts = {
            "classpath:database/books/delete-books.sql",
            "classpath:database/categories/delete-categories.sql",
            "classpath:database/categories/add-fiction-category.sql",
            "classpath:database/books/add-effective-java-book.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/delete-books.sql",
            "classpath:database/categories/delete-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_GivenBooksInDb_ShouldReturnAllBooks() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Find by ID: Existing ID")
    @Sql(scripts = {
            "classpath:database/books/delete-books.sql",
            "classpath:database/categories/delete-categories.sql",
            "classpath:database/categories/add-fiction-category.sql",
            "classpath:database/books/add-effective-java-book.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/delete-books.sql",
            "classpath:database/categories/delete-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBookById_ValidId_ShouldReturnBookDto() throws Exception {
        Long bookId = 1L;
        mockMvc.perform(get("/books/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Find by ID: Non-existing ID")
    void findById_NotExistingId_ReturnsNotFound() throws Exception {
        Long bookId = 999L;
        mockMvc.perform(get("/books/{id}", bookId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Update book: Success")
    @Sql(scripts = {
            "classpath:database/books/delete-books.sql",
            "classpath:database/categories/delete-categories.sql",
            "classpath:database/categories/add-fiction-category.sql",
            "classpath:database/books/add-effective-java-book.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/delete-books.sql",
            "classpath:database/categories/delete-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateBook_ValidRequest_ShouldReturnUpdatedBookDto() throws Exception {
        Long bookId = 1L;
        CreateBookRequestDto requestDto = TestDataHelper.createBookRequestDto();
        requestDto.setTitle("Updated Title");

        mockMvc.perform(put("/books/{id}", bookId)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Delete book: Success")
    @Sql(scripts = {
            "classpath:database/books/delete-books.sql",
            "classpath:database/categories/delete-categories.sql",
            "classpath:database/categories/add-fiction-category.sql",
            "classpath:database/books/add-effective-java-book.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/delete-books.sql",
            "classpath:database/categories/delete-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteBook_ValidId_ShouldReturnNoContent() throws Exception {
        Long bookId = 1L;

        mockMvc.perform(delete("/books/{id}", bookId))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Delete book as USER: Forbidden")
    void delete_AsUser_ReturnsForbidden() throws Exception {
        Long bookId = 1L;

        mockMvc.perform(delete("/books/{id}", bookId))
                .andExpect(status().isForbidden());
    }
}
