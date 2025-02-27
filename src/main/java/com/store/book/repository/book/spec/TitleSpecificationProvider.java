package com.store.book.repository.book.spec;

import com.store.book.model.Book;
import com.store.book.repository.SpecificationProvider;
import com.store.book.repository.book.BookSpecificationField;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {

    @Override
    public String getKey() {

        return BookSpecificationField.TITLE.getKey();
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {

        return (root, query, criteriaBuilder)
                -> root.get(BookSpecificationField.TITLE.getKey())
                .in(Arrays.stream(params).toArray());
    }
}
