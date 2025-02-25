package com.store.book.repository;

import com.store.book.dto.BookSearchParametersDto;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {

    Specification<T> build(BookSearchParametersDto searchParametersDto);
}
