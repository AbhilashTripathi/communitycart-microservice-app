-- test-data.sql
-- Create a test seller
INSERT INTO seller (seller_id, seller_name, email) VALUES (1, 'Test Seller', 'seller@test.com');

-- Create a test category
INSERT INTO category (category_id, category_name, description) VALUES (1, 'Test Category', 'A category for testing');

-- Create some test products
INSERT INTO product (product_id, product_name, seller_id, category_id, product_quantity, available, rating, price)
VALUES
(1, 'Test Product 1', 1, 1, 100, true, 4.5, 19.99),
(2, 'Test Product 2', 1, 1, 50, true, 4.0, 29.99);