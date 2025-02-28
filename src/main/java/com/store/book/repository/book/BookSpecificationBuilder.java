package com.store.book.repository.book;

import com.store.book.dto.BookSearchParametersDto;
import com.store.book.model.Book;
import com.store.book.repository.SpecificationBuilder;
import com.store.book.repository.SpecificationProviderManager;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParametersDto) {
        Specification<Book> spec = Specification.where(null);
        Map<BookSpecificationField, String[]> filters = Map.of(
                BookSpecificationField.TITLE, getValues(searchParametersDto.title()),
                BookSpecificationField.AUTHOR, getValues(searchParametersDto.author()),
                BookSpecificationField.ISBN, getValues(searchParametersDto.isbn())
        );

        for (var entry : filters.entrySet()) {
            if (entry.getValue().length > 0) {
                spec = spec.and(bookSpecificationProviderManager
                        .getSpecificationProvider(entry.getKey().getKey())
                        .getSpecification(entry.getValue()));
            }
        }
        return spec;
    }

    private String[] getValues(String[] values) {
        return (values != null && values.length > 0) ? values : new String[0];
    }
}
