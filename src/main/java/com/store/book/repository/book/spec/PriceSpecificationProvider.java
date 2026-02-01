package com.store.book.repository.book.spec;

import com.store.book.model.Book;
import com.store.book.repository.SpecificationProvider;
import com.store.book.repository.book.BookSpecificationField;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PriceSpecificationProvider implements SpecificationProvider<Book> {

    @Override
    public String getKey() {
        return BookSpecificationField.PRICE.getKey();
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        final List<BigDecimal> priceParams
                = Stream.of(params).map(BigDecimal::new).sorted().toList();
        String priceFrom = String.valueOf(priceParams.get(0));
        String priceTo = String.valueOf(priceParams.get(priceParams.size() - 1));
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.between(root.get(getKey()), priceFrom, priceTo);
    }
}
