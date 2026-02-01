package com.store.book.repository.book.spec;

import com.store.book.model.Book;
import com.store.book.repository.SpecificationProvider;
import com.store.book.repository.book.BookSpecificationField;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
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
        return (root, query, criteriaBuilder) -> {
            List<Predicate> titlePredicates = new ArrayList<>();
            for (String param : params) {
                titlePredicates.add(criteriaBuilder.like(root.get(getKey()), "%" + param + "%"));
            }
            return criteriaBuilder.or(titlePredicates.toArray(new Predicate[0]));
        };
    }
}
