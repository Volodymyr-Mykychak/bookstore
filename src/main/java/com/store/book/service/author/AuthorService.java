package com.store.book.service.author;

import com.store.book.dto.author.AuthorDto;
import com.store.book.dto.author.CreateAuthorRequestDto;
import java.util.List;

public interface AuthorService {

    AuthorDto save(CreateAuthorRequestDto requestDto);

    List<AuthorDto> findAll();

    AuthorDto findById(Long id);

    List<AuthorDto> findAllByName(String name);

    void deleteById(Long id);
}
