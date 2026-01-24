package com.store.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.store.book.dto.book.BookDtoWithoutCategoryIds;
import com.store.book.dto.category.CategoryDto;
import com.store.book.dto.category.CreateCategoryRequestDto;
import com.store.book.exception.EntityNotFoundException;
import com.store.book.mapper.BookMapper;
import com.store.book.mapper.CategoryMapper;
import com.store.book.model.Book;
import com.store.book.model.Category;
import com.store.book.repository.CategoryRepository;
import com.store.book.repository.book.BookRepository;
import com.store.book.service.impl.CategoryServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Find all: Returns page of CategoryDto")
    void findAll_ValidPageable_ReturnsPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Category category = new Category();
        CategoryDto categoryDto = new CategoryDto();
        Page<Category> categoryPage = new PageImpl<>(List.of(category));
        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        Page<CategoryDto> result = categoryService.findAll(pageable);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(categoryDto);
    }

    @Test
    @DisplayName("Find by ID: Existing ID returns DTO")
    void findById_ExistingId_ReturnsDto() {
        Long id = 1L;
        Category category = new Category();
        CategoryDto expected = new CategoryDto();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expected);
        CategoryDto actual = categoryService.findById(id);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Find by ID: Non-existing ID throws Exception")
    void findById_NonExistingId_ThrowsException() {
        Long id = 99L;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> categoryService.findById(id)).isInstanceOf(
                        EntityNotFoundException.class)
                .hasMessageContaining("Category not found with id " + id);
    }

    @Test
    @DisplayName("Save category: Success")
    void save_ValidDto_ReturnsSavedDto() {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        Category category = new Category();
        CategoryDto expected = new CategoryDto();
        when(categoryMapper.toEntity(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expected);
        CategoryDto actual = categoryService.save(requestDto);
        assertThat(actual).isEqualTo(expected);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    @DisplayName("Update category: Success")
    void update_ExistingId_ReturnsUpdatedDto() {
        Long id = 1L;
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        Category category = new Category();
        CategoryDto expected = new CategoryDto();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expected);
        CategoryDto actual = categoryService.update(id, requestDto);
        assertThat(actual).isEqualTo(expected);
        verify(categoryMapper).updateCategoryFromDto(requestDto, category);
    }

    @Test
    @DisplayName("Delete by ID: Throws exception if not exists")
    void deleteById_NonExistingId_ThrowsException() {
        Long id = 1L;
        when(categoryRepository.existsById(id)).thenReturn(false);
        assertThatThrownBy(() -> categoryService.deleteById(id)).isInstanceOf(
                EntityNotFoundException.class);
    }

    @Test
    @DisplayName("Get books by Category ID: Returns list of books")
    void getBooksByCategoryId_ValidId_ReturnsList() {
        Long categoryId = 1L;
        Book book = new Book();
        BookDtoWithoutCategoryIds bookDto = new BookDtoWithoutCategoryIds();
        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        when(bookRepository.findAllByCategoriesId(categoryId)).thenReturn(List.of(book));
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDto);
        List<BookDtoWithoutCategoryIds> result = categoryService.getBooksByCategoryId(categoryId);
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(bookDto);
    }
}
