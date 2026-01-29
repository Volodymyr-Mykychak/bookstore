INSERT INTO users (id, email, password, first_name, last_name, is_deleted)
VALUES (100, 'test@example.com', 'pass', 'John', 'Doe', false);

INSERT INTO shopping_carts (user_id, is_deleted)
VALUES (100, false);