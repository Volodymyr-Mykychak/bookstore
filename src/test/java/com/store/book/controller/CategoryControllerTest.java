package com.store.book.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.book.dto.category.CreateCategoryRequestDto;
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
public class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create category: Valid request")
    @Sql(scripts = "classpath:database/categories/delete-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/delete-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_ValidRequest_ShouldReturnCreatedCategory() throws Exception {
        CreateCategoryRequestDto requestDto = TestDataHelper.createCategoryRequestDto();
        requestDto.setName("New Category");
        mockMvc.perform(post("/categories").content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Category"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get all categories: Returns page")
    @Sql(scripts = {"classpath:database/categories/delete-categories.sql",
                    "classpath:database/categories/add-categories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/delete-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllCategories_GivenExistingCategories_ShouldReturnPage() throws Exception {
        mockMvc.perform(get("/categories")).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Fiction"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get category by ID: Existing ID")
    @Sql(scripts = {"classpath:database/categories/delete-categories.sql",
                    "classpath:database/categories/add-categories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/delete-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getCategoryById_ExistingId_ShouldReturnCategoryDto() throws Exception {
        Long id = 1L;
        mockMvc.perform(get("/categories/{id}", id)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Fiction"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Update category: Valid request")
    @Sql(scripts = {"classpath:database/categories/delete-categories.sql",
                    "classpath:database/categories/add-categories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/delete-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateCategory_ValidIdAndRequest_ShouldReturnUpdatedDto() throws Exception {
        Long id = 1L;
        CreateCategoryRequestDto requestDto = TestDataHelper.createCategoryRequestDto();
        requestDto.setName("Updated Fiction");
        mockMvc.perform(
                        put("/categories"
                                + "/{id}", id).content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Fiction"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Delete category: Success")
    @Sql(scripts = {"classpath:database/categories/delete-categories.sql",
                    "classpath:database/categories/add-categories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/delete-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteCategory_ValidId_ShouldReturnOk() throws Exception {
        mockMvc.perform(delete("/categories/{id}", 1L)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get books by category ID")
    @Sql(scripts = {"classpath:database/books/delete-books.sql",
                    "classpath:database/categories/delete-categories.sql",
                    "classpath:database/categories/add-categories.sql",
                    "classpath:database/books/add-books.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/books/delete-books.sql",
                    "classpath:database/categories/delete-categories.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBooksByCategoryId_ValidId_ShouldReturnBooksList() throws Exception {
        Long categoryId = 1L;
        mockMvc.perform(get("/categories/{id}/books", categoryId)).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("Effective Java"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create category: Invalid request (Empty Name)")
    void createCategory_InvalidRequest_ShouldReturnBadRequest() throws Exception {
        CreateCategoryRequestDto invalidDto = TestDataHelper.createCategoryRequestDto();
        invalidDto.setName("");
        mockMvc.perform(post("/categories").content(objectMapper.writeValueAsString(invalidDto))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get category by ID: Non-existing ID")
    void getCategoryById_NonExistingId_ShouldReturnNotFound() throws Exception {
        Long nonExistingId = 999L;
        mockMvc.perform(get("/categories/{id}", nonExistingId)).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Create category as USER: Forbidden")
    void createCategory_AsUser_ShouldReturnForbidden() throws Exception {
        CreateCategoryRequestDto dto = TestDataHelper.createCategoryRequestDto();
        mockMvc.perform(post("/categories").content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
    }
}
