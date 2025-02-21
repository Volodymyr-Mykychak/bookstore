package com.store.book.service.author.impl;

import com.store.book.dto.author.AuthorDto;
import com.store.book.dto.author.CreateAuthorRequestDto;
import com.store.book.exception.EntityNotFoundException;
import com.store.book.mapper.AuthorMapper;
import com.store.book.model.Author;
import com.store.book.repository.AuthorRepository;
import com.store.book.service.author.AuthorService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    @Override
    public AuthorDto save(CreateAuthorRequestDto authorDto) {

        Author author = authorMapper.toModel(authorDto);
        return authorMapper.toDto(authorRepository.save(author));
    }

    @Override
    public List<AuthorDto> findAll() {

        return authorRepository
                .findAll()
                .stream()
                .map(authorMapper::toDto)
                .toList();
    }

    @Override
    public List<AuthorDto> findAllByName(String name) {

        return authorRepository.findAllByNameContainingIgnoreCase(name)
                .stream()
                .map(authorMapper::toDto)
                .toList();
    }

    @Override
    public AuthorDto findById(Long id) {

        Author author = authorRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find author by id " + id));
        return authorMapper.toDto(author);
    }

    @Override
    public void deleteById(Long id) {

        authorRepository.findById(id).ifPresentOrElse(
                authorRepository::delete,
                () -> {
                    throw new EntityNotFoundException("Can't delete, author with id "
                            + id + "not found.");
                }
        );
    }
}
