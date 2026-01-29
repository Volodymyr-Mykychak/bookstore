INSERT INTO users (id, email, password, first_name, last_name, is_deleted)
VALUES (100, 'order-user@example.com', '$2a$10$wfB05ha61EtV4FYa16kuaewPld0Qw7isWneDv321273uPcagQADyC', 'Order', 'User', false);

INSERT INTO users_roles (user_id, role_id) VALUES (100, 1);