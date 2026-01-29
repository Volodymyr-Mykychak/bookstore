INSERT INTO orders (id, user_id, status, total, order_date, shipping_address, is_deleted)
VALUES (1, 1, 'PENDING', 100.00, '2024-01-28 10:00:00', 'Main St 123', false);

INSERT INTO order_items (id, order_id, book_id, quantity, price, is_deleted)
VALUES (1, 1, 1, 1, 100.00, false);
