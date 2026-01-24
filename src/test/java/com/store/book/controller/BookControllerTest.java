package com.store.book.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.book.dto.book.BookDto;
import com.store.book.dto.book.CreateBookRequestDto;
import com.store.book.exception.EntityNotFoundException;
import com.store.book.service.impl.BookServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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

    @MockBean
    private BookServiceImpl bookService;

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
    void createBook_Valid_ReturnsCreated() throws Exception {
        CreateBookRequestDto requestDto = getFirstCreateBookRequest();
        BookDto responseDto = getFirstBookDto();

        when(bookService.save(any())).thenReturn(responseDto);

        mockMvc.perform(post("/books")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
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
    void findAll_ReturnsPage() throws Exception {
        when(bookService.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(getFirstBookDto())));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Find by ID: Existing ID")
    void findById_ExistingId_ReturnsDto() throws Exception {
        BookDto dto = getFirstBookDto();
        when(bookService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Find by ID: Non-existing ID")
    void findById_NotExistingId_ReturnsNotFound() throws Exception {
        when(bookService.findById(99L)).thenThrow(new EntityNotFoundException("Not found"));

        mockMvc.perform(get("/books/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Update book: Success")
    void update_ValidRequest_ReturnsUpdatedDto() throws Exception {
        CreateBookRequestDto requestDto = getFirstCreateBookRequest();
        BookDto responseDto = getFirstBookDto();
        when(bookService.update(any(), any())).thenReturn(responseDto);

        mockMvc.perform(put("/books/1")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Delete book: Success")
    void delete_ValidId_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteById(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Delete book as USER: Forbidden")
    void delete_AsUser_ReturnsForbidden() throws Exception {
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isForbidden());
    }

    private CreateBookRequestDto getFirstCreateBookRequest() {
        CreateBookRequestDto dto = new CreateBookRequestDto();
        dto.setTitle("Effective Java");
        dto.setAuthor("Joshua Bloch");
        dto.setIsbn("9780134685991");
        dto.setPrice(new BigDecimal("45.00"));
        dto.setCategoryIds(List.of(1L));
        return dto;
    }

    private BookDto getFirstBookDto() {
        BookDto dto = new BookDto();
        dto.setId(1L);
        dto.setTitle("Effective Java");
        dto.setAuthor("Joshua Bloch");
        dto.setIsbn("9780134685991");
        dto.setPrice(new BigDecimal("45.00"));
        dto.setCategoryIds(List.of(1L));
        return dto;
    }
}
