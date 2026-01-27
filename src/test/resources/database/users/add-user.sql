INSERT INTO users (id, email, password, first_name, last_name, is_deleted)
VALUES (10, 'user@example.com', '$2a$10$wfB05ha61EtV4FYa16kuaewPld0Qw7isWneDv321273uPcagQADyC', 'John', 'Doe', false);

INSERT INTO users_roles (user_id, role_id)
SELECT 10, id FROM roles WHERE name = 'ROLE_USER';

INSERT INTO shopping_carts (user_id, is_deleted) VALUES (10, false);