package com.store.book.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import com.store.book.dto.book.BookDtoWithoutCategoryIds;
import com.store.book.dto.category.CategoryDto;
import com.store.book.dto.category.CreateCategoryRequestDto;
import com.store.book.exception.EntityNotFoundException;
import com.store.book.service.CategoryService;
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
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @BeforeEach
    void setUp(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create category: Valid request")
    void createCategory_ValidRequest_ReturnsCreated() throws Exception {
        CreateCategoryRequestDto requestDto = getRequestDto();
        CategoryDto responseDto = getCategoryDto(1L);
        when(categoryService.save(any())).thenReturn(responseDto);

        mockMvc.perform(post("/categories")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create category: Invalid data (Validation error)")
    void createCategory_InvalidData_ReturnsBadRequest() throws Exception {
        CreateCategoryRequestDto invalidRequest = new CreateCategoryRequestDto();

        mockMvc.perform(post("/categories")
                        .content(objectMapper.writeValueAsString(invalidRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get all categories: Returns page")
    void getAll_ReturnsPage() throws Exception {
        CategoryDto dto = getCategoryDto(1L);
        when(categoryService.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(dto)));

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is(dto.getName())));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get category by ID: Existing ID")
    void getById_ExistingId_ReturnsDto() throws Exception {
        CategoryDto dto = getCategoryDto(1L);
        when(categoryService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get category by ID: Non-existing ID")
    void getById_NonExistingId_ReturnsNotFound() throws Exception {
        when(categoryService.findById(99L)).thenThrow(new EntityNotFoundException("Category"
                + " not found"));

        mockMvc.perform(get("/categories/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Update category: Valid request")
    void update_ValidRequest_ReturnsUpdatedDto() throws Exception {
        CreateCategoryRequestDto requestDto = getRequestDto();
        CategoryDto responseDto = getCategoryDto(1L);
        when(categoryService.update(eq(1L), any())).thenReturn(responseDto);

        mockMvc.perform(put("/categories/1")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Delete category: Success")
    void delete_ValidId_ReturnsOk() throws Exception {
        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).deleteById(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Delete category as USER: Forbidden")
    void delete_AsUser_ReturnsForbidden() throws Exception {
        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get books by category ID")
    void getBooksByCategory_ValidId_ReturnsList() throws Exception {
        BookDtoWithoutCategoryIds bookDto = new BookDtoWithoutCategoryIds();
        bookDto.setId(1L);
        bookDto.setTitle("Book Title");

        when(categoryService.getBooksByCategoryId(1L)).thenReturn(List.of(bookDto));

        mockMvc.perform(get("/categories/1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Book Title")));
    }

    private CreateCategoryRequestDto getRequestDto() {
        CreateCategoryRequestDto dto = new CreateCategoryRequestDto();
        dto.setName("Fiction");
        dto.setDescription("Fiction books description");
        return dto;
    }

    private CategoryDto getCategoryDto(Long id) {
        CategoryDto dto = new CategoryDto();
        dto.setId(id);
        dto.setName("Fiction");
        dto.setDescription("Fiction books description");
        return dto;
    }
}
