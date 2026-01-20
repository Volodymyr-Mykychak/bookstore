package com.store.book.repository.order;

import com.store.book.model.Order;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"orderItems.book"})
    Page<Order> findAllByUserId(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"orderItems.book"})
    Optional<Order> findByIdAndUserId(Long id, Long userId);

    Page<Order> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"orderItems.book"})
    Optional<Order> findById(Long id);
}
