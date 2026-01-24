package com.store.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.store.book.dto.book.BookDto;
import com.store.book.dto.book.CreateBookRequestDto;
import com.store.book.exception.EntityNotFoundException;
import com.store.book.exception.OrderProcessingException;
import com.store.book.mapper.BookMapper;
import com.store.book.model.Book;
import com.store.book.repository.book.BookRepository;
import com.store.book.service.impl.BookServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Verify save() method works")
    void save_ValidRequest_ReturnsBookDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        Book book = new Book();
        BookDto expected = new BookDto();
        expected.setId(1L);
        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expected);
        BookDto actual = bookService.save(requestDto);
        assertThat(actual).isEqualTo(expected);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    @DisplayName("Verify findById() returns book when it exists")
    void findById_ExistingId_ReturnsBookDto() {
        Long id = 1L;
        Book book = new Book();
        BookDto expected = new BookDto();
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expected);
        BookDto actual = bookService.findById(id);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Verify findById() throws exception when book not found")
    void findById_NonExistingId_ThrowsException() {
        Long id = 999L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookService.findById(id)).isInstanceOf(
                EntityNotFoundException.class).hasMessage("Can't find book by id " + id);
    }

    @Test
    @DisplayName("Verify updateStock() throws exception when update fails")
    void updateStock_InsufficientStock_ThrowsOrderProcessingException() {
        Long bookId = 1L;
        int change = -10;
        when(bookRepository.updateQuantity(bookId, change)).thenReturn(0);
        assertThatThrownBy(() -> bookService.updateStock(bookId, change)).isInstanceOf(
                OrderProcessingException.class).hasMessageContaining("Not enough stock");
    }

    @Test
    @DisplayName("Verify deleteById() throws exception when book doesn't exist")
    void deleteById_NonExistingId_ThrowsException() {
        Long id = 1L;
        when(bookRepository.existsById(id)).thenReturn(false);
        assertThatThrownBy(() -> bookService.deleteById(id)).isInstanceOf(
                EntityNotFoundException.class);
        verify(bookRepository, times(0)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Verify update() method works")
    void update_ExistingId_ReturnsUpdatedDto() {
        Long id = 1L;
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        Book book = new Book();
        BookDto expected = new BookDto();
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expected);
        BookDto actual = bookService.update(id, requestDto);
        assertThat(actual).isEqualTo(expected);
        verify(bookMapper).updateBookFromDto(requestDto, book);
    }
}
