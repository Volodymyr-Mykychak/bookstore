package com.store.book.repository.cart;

import com.store.book.model.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByShoppingCartIdAndBookId(Long shoppingCartId, Long bookId);

    @Query("SELECT ci FROM CartItem ci WHERE ci.id = :id AND ci.shoppingCart.user.id = :userId")
    Optional<CartItem> findByIdAndShoppingCartId(@Param("id") Long id,
                                                 @Param("userId") Long userId);
}
