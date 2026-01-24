INSERT INTO books (id, title, author, isbn, price, description)
VALUES (1, 'Effective Java', 'Joshua Bloch', '9780134685991', 45.00, 'Best Java book');

-- Зв'язок книги з категорією (Many-to-Many table)
INSERT INTO books_categories (book_id, category_id) VALUES (1, 1);