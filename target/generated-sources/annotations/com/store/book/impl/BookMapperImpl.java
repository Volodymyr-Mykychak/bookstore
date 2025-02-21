package com.store.book.impl;

import com.store.book.dto.book.BookDto;
import com.store.book.dto.book.CreateBookRequestDto;
import com.store.book.mapper.AuthorMapper;
import com.store.book.mapper.BookMapper;
import com.store.book.model.Author;
import com.store.book.model.Book;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-21T16:20:34+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Ubuntu)"
)
@Component
public class BookMapperImpl implements BookMapper {

    private final AuthorMapper authorMapper;

    @Autowired
    public BookMapperImpl(AuthorMapper authorMapper) {

        this.authorMapper = authorMapper;
    }

    @Override
    public BookDto toDto(Book book) {
        if ( book == null ) {
            return null;
        }

        BookDto bookDto = new BookDto();

        Long id = bookAuthorId( book );
        if ( id != null ) {
            bookDto.setAuthorId( id );
        }
        if ( book.getId() != null ) {
            bookDto.setId( book.getId() );
        }
        if ( book.getTitle() != null ) {
            bookDto.setTitle( book.getTitle() );
        }
        if ( book.getIsbn() != null ) {
            bookDto.setIsbn( book.getIsbn() );
        }
        if ( book.getPrice() != null ) {
            bookDto.setPrice( book.getPrice() );
        }
        if ( book.getDescription() != null ) {
            bookDto.setDescription( book.getDescription() );
        }
        if ( book.getCoverImage() != null ) {
            bookDto.setCoverImage( book.getCoverImage() );
        }

        return bookDto;
    }

    @Override
    public Book toModel(CreateBookRequestDto requestDto) {
        if ( requestDto == null ) {
            return null;
        }

        Book book = new Book();

        if ( requestDto.getAuthorId() != null ) {
            book.setAuthor( authorMapper.authorById( requestDto.getAuthorId() ) );
        }
        if ( requestDto.getTitle() != null ) {
            book.setTitle( requestDto.getTitle() );
        }
        if ( requestDto.getIsbn() != null ) {
            book.setIsbn( requestDto.getIsbn() );
        }
        if ( requestDto.getPrice() != null ) {
            book.setPrice( requestDto.getPrice() );
        }
        if ( requestDto.getDescription() != null ) {
            book.setDescription( requestDto.getDescription() );
        }
        if ( requestDto.getCoverImage() != null ) {
            book.setCoverImage( requestDto.getCoverImage() );
        }

        return book;
    }

    private Long bookAuthorId(Book book) {
        if ( book == null ) {
            return null;
        }
        Author author = book.getAuthor();
        if ( author == null ) {
            return null;
        }
        Long id = author.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
