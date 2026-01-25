INSERT INTO books (id, title, author, isbn, price, description, is_deleted)
VALUES (1, 'Effective Java', 'Joshua Bloch', '9780134685991', 45.00, 'Description', false);

INSERT INTO books_categories (book_id, category_id) VALUES (1, 1);