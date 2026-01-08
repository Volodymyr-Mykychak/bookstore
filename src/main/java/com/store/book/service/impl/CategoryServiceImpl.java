package com.store.book.service.impl;

import com.store.book.dto.book.BookDtoWithoutCategoryIds;
import com.store.book.dto.category.CategoryDto;
import com.store.book.dto.category.CreateCategoryRequestDto;
import com.store.book.exception.EntityNotFoundException;
import com.store.book.mapper.BookMapper;
import com.store.book.mapper.CategoryMapper;
import com.store.book.model.Category;
import com.store.book.repository.CategoryRepository;
import com.store.book.repository.book.BookRepository;
import com.store.book.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapper::toDto);
    }

    @Override
    public CategoryDto findById(Long id) {
        return categoryRepository.findById(id)
                                 .map(categoryMapper::toDto)
                                 .orElseThrow(() -> new EntityNotFoundException(
                                         "Category not found with id " + id));
    }

    @Override
    @Transactional
    public CategoryDto save(CreateCategoryRequestDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryDto update(Long id, CreateCategoryRequestDto requestDto) {
        Category category = categoryRepository.findById(id)
                                              .orElseThrow(() -> new EntityNotFoundException(
                                                      "Category not found"));
        categoryMapper.updateCategoryFromDto(requestDto, category);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category not found with id " + id);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category not found with id " + id);
        }
        return bookRepository.findAllByCategoriesId(id).stream()
                             .map(bookMapper::toDtoWithoutCategories)
                             .toList();
    }
}
