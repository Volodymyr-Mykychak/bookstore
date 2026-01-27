INSERT INTO users (id, email, password, first_name, last_name, is_deleted)
VALUES (100, 'test-user@example.com', 'hashed_password', 'John', 'Doe', false);

INSERT INTO shopping_carts (user_id, is_deleted)
VALUES (100, false);

INSERT INTO books (id, title, author, isbn, price, is_deleted)
VALUES (100, 'Clean Code', 'Robert Martin', '978-0132350884', 50.00, false);

INSERT INTO cart_items (id, shopping_cart_id, book_id, quantity, is_deleted)
VALUES (1, 100, 100, 5, false);