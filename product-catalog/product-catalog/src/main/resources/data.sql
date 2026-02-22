DELETE FROM products;

INSERT INTO products (name, description, price, category, stock_quantity, image_url) VALUES
('iPhone 15 Pro', 'Latest Apple flagship with titanium design, A17 Pro chip, and 48MP camera system.', 1199.99, 'Electronics', 25, 'https://images.unsplash.com/photo-1696446701796-da61339901b7?w=400'),
('Samsung Galaxy S24 Ultra', 'Premium Android flagship with built-in S Pen, 200MP camera, and AI-powered features.', 1099.99, 'Electronics', 18, 'https://images.unsplash.com/photo-1707741426054-d7b3c20633db?w=400'),
('Sony WH-1000XM5', 'Industry-leading noise cancelling wireless headphones with 30-hour battery life.', 279.99, 'Electronics', 40, 'https://images.unsplash.com/photo-1618366712010-f4ae9c647dcb?w=400'),
('Apple MacBook Air M3', '13-inch laptop with M3 chip, 18-hour battery life, and stunning Liquid Retina display.', 1299.99, 'Electronics', 12, 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=400'),
('iPad Pro 12.9', 'Most powerful iPad with M2 chip, Liquid Retina XDR display, and Apple Pencil support.', 899.99, 'Electronics', 15, 'https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=400'),
('Nike Air Max 270', 'Iconic lifestyle sneakers with large Air unit for all-day comfort and bold style.', 149.99, 'Clothing', 60, 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400'),
('Levi''s 501 Original Jeans', 'The original blue jean since 1873. Straight fit with signature button fly.', 79.99, 'Clothing', 85, 'https://images.unsplash.com/photo-1541099649105-f69ad21f3246?w=400'),
('North Face Puffer Jacket', 'Warm and lightweight 550-fill down jacket, perfect for cold weather adventures.', 189.99, 'Clothing', 30, 'https://images.unsplash.com/photo-1547949003-9792a18a2601?w=400'),
('Clean Code', 'A handbook of agile software craftsmanship by Robert C. Martin. Essential reading for developers.', 34.99, 'Books', 50, 'https://images.unsplash.com/photo-1532012197267-da84d127e765?w=400'),
('The Pragmatic Programmer', '20th anniversary edition covering practical software development approaches and best practices.', 39.99, 'Books', 45, 'https://images.unsplash.com/photo-1524995997946-a1c2e315a42f?w=400'),
('Dyson V15 Detect', 'Powerful cordless vacuum with laser dust detection and up to 60 minutes run time.', 649.99, 'Home & Kitchen', 20, 'https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400'),
('Nespresso Vertuo Pop', 'Coffee machine with one-touch brewing, 5 cup sizes, and 36 capsule varieties.', 99.99, 'Home & Kitchen', 35, 'https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=400'),
('Yoga Mat Pro', 'Non-slip 6mm thick yoga mat with alignment lines, carrying strap, and eco-friendly materials.', 49.99, 'Sports', 70, 'https://images.unsplash.com/photo-1601925228008-1e9f6d4f8e5c?w=400'),
('The Ordinary Skincare Set', 'Complete beginner skincare routine with Niacinamide, Hyaluronic Acid, and SPF moisturiser.', 44.99, 'Beauty', 55, 'https://images.unsplash.com/photo-1556228578-8c89e6adf883?w=400'),
('LEGO Technic Ferrari', '1,677-piece LEGO Technic Ferrari 488 GTE with detailed engine, steering, and gearbox functions.', 179.99, 'Toys', 22, 'https://images.unsplash.com/photo-1558618047-3c8c76ca7d13?w=400');

DELETE FROM users WHERE email = 'admin@catalog.com';
INSERT INTO users (name, email, password, role) VALUES ('Admin', 'admin@catalog.com', '$2a$10$v.zxlTbhz85r3WNi9mKHAuivuFLx8iRxLReluCp8k.qql4mcuwBk2', 'ADMIN');