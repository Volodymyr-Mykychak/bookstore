package com.store.book.util;

import com.store.book.dto.book.BookDto;
import com.store.book.dto.book.CreateBookRequestDto;
import com.store.book.dto.cart.CartItemRequestDto;
import com.store.book.dto.cart.CartItemUpdateDto;
import com.store.book.dto.cart.ShoppingCartDto;
import com.store.book.dto.category.CategoryDto;
import com.store.book.dto.category.CreateCategoryRequestDto;
import com.store.book.model.Book;
import com.store.book.model.CartItem;
import com.store.book.model.Category;
import com.store.book.model.Role;
import com.store.book.model.RoleName;
import com.store.book.model.ShoppingCart;
import com.store.book.model.User;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestDataHelper {

    public static CreateBookRequestDto createBookRequestDto() {
        CreateBookRequestDto dto = new CreateBookRequestDto();
        dto.setTitle("Effective Java");
        dto.setAuthor("Joshua Bloch");
        dto.setIsbn("9780134685991");
        dto.setPrice(new BigDecimal("1000.00"));
        dto.setCategoryIds(List.of(1L));
        return dto;
    }

    public static Book createBook(CreateBookRequestDto dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPrice(dto.getPrice());
        book.setIsbn(dto.getIsbn());
        return book;
    }

    public static BookDto createBookDto(Long id, Book book) {
        BookDto dto = new BookDto();
        dto.setId(id);
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setPrice(book.getPrice());
        dto.setIsbn(book.getIsbn());
        return dto;
    }

    public static CreateCategoryRequestDto createCategoryRequestDto() {
        CreateCategoryRequestDto dto = new CreateCategoryRequestDto();
        dto.setName("Fiction");
        dto.setDescription("Fiction books description");
        return dto;
    }

    public static Category createCategory(Long id, CreateCategoryRequestDto dto) {
        Category category = new Category();
        category.setId(id);
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return category;
    }

    public static CategoryDto createCategoryDto(Long id, String name) {
        CategoryDto dto = new CategoryDto();
        dto.setId(id);
        dto.setName(name);
        return dto;
    }

    public static Book createBookWithQuantity(String title, int quantity) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor("Author");
        book.setIsbn("ISBN-" + title.hashCode());
        book.setPrice(BigDecimal.valueOf(100));
        book.setQuantity(quantity);
        return book;
    }

    public static User getUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setEmail("user" + id + "@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password");

        Role role = new Role();
        role.setId(1L);
        role.setName(RoleName.ROLE_USER);

        user.setRoles(Set.of(role));
        return user;
    }

    public static ShoppingCart getShoppingCart(User user) {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(user.getId());
        cart.setUser(user);
        cart.setCartItems(new HashSet<>());
        return cart;
    }

    public static Book getBook(Long id) {
        Book book = new Book();
        book.setId(id);
        book.setTitle("Sample Book");
        book.setAuthor("Author");
        book.setIsbn("ISBN-" + id);
        book.setPrice(new BigDecimal("100.00"));
        return book;
    }

    public static CartItem getCartItem(Long id, ShoppingCart cart, Book book) {
        CartItem item = new CartItem();
        item.setId(id);
        item.setShoppingCart(cart);
        item.setBook(book);
        item.setQuantity(2);
        return item;
    }

    public static ShoppingCartDto mapToDto(ShoppingCart cart) {
        ShoppingCartDto dto = new ShoppingCartDto();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUser().getId());
        dto.setCartItems(new HashSet<>());
        return dto;
    }

    public static CartItemRequestDto createCartItemRequestDto() {
        CartItemRequestDto dto = new CartItemRequestDto();
        dto.setBookId(1L);
        dto.setQuantity(2);
        return dto;
    }

    public static CartItemUpdateDto createCartItemUpdateDto() {
        CartItemUpdateDto dto = new CartItemUpdateDto();
        dto.setQuantity(5);
        return dto;
    }
}
