package com.store.book.dto.author;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateAuthorRequestDto(String name, String bio) {

}
