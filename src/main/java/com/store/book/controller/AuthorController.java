package com.store.book.controller;

import com.store.book.dto.author.AuthorDto;
import com.store.book.dto.author.CreateAuthorRequestDto;
import com.store.book.service.author.AuthorService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    public AuthorDto save(@RequestBody CreateAuthorRequestDto requestDto) {

        return authorService.save(requestDto);
    }

    @GetMapping
    List<AuthorDto> findAll() {

        return authorService.findAll();
    }

    @GetMapping("/{id}")
    public AuthorDto findById(@PathVariable Long id) {

        return authorService.findById(id);
    }

    @GetMapping("/by-name")
    public List<AuthorDto> findAllByName(@RequestParam String name) {

        return authorService.findAllByName(name);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {

        authorService.deleteById(id);
    }
}
