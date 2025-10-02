INSERT INTO restaurants (id, name, address, cuisine_type, opening_hours, owner_id) VALUES
('7a8b9c0d-1e2f-3a4b-5c6d-7e8f9a0b1c2d', 'Restaurante do João', 'Avenida Paulista, 1000, Bela Vista, São Paulo - SP', 'Brasileira', '11:00 - 22:00', 'c9bf9e57-1685-4c89-bafb-ff5af830be8a'),
('2f3a4b5c-6d7e-8f9a-0b1c-2d3e4f5a6b7c', 'Cantina da Maria', 'Rua Augusta, 500, Consolação, São Paulo - SP', 'Italiana', '18:00 - 23:00', 'a1b2c3d4-e5f6-7890-abcd-1234567890ef')
ON CONFLICT (id) DO NOTHING;

INSERT INTO menu_items (id, restaurant_id, name, description, price, available_for_in_store_only) VALUES
('9d8c7b6a-5f4e-3d2c-1b0a-9f8e7d6c5b4a', '7a8b9c0d-1e2f-3a4b-5c6d-7e8f9a0b1c2d', 'Feijoada Completa', 'Feijoada tradicional brasileira com todos os acompanhamentos', 32.90, false),
('3b2a1f0e-9d8c-7b6a-5f4e-3d2c1b0a9f8e', '7a8b9c0d-1e2f-3a4b-5c6d-7e8f9a0b1c2d', 'Moqueca de Camarão', 'Deliciosa moqueca de camarão com arroz e pirão', 45.00, false),
('6e5d4c3b-2a1f-0e9d-8c7b-6a5f4e3d2c1b', '7a8b9c0d-1e2f-3a4b-5c6d-7e8f9a0b1c2d', 'Picanha Grelhada', 'Picanha na chapa com fritas e salada', 55.00, true),
('8a7f6e5d-4c3b-2a1f-0e9d-8c7b6a5f4e3d', '7a8b9c0d-1e2f-3a4b-5c6d-7e8f9a0b1c2d', 'Caipirinha da Casa', 'Caipirinha especial da casa com cachaça artesanal', 12.00, true)
ON CONFLICT (id) DO NOTHING;

INSERT INTO menu_items (id, restaurant_id, name, description, price, available_for_in_store_only) VALUES
('1c0b9a8f-7e6d-5c4b-3a2f-1e0d9c8b7a6f', '2f3a4b5c-6d7e-8f9a-0b1c-2d3e4f5a6b7c', 'Lasanha Bolonhesa', 'Lasanha tradicional italiana com molho bolonhesa', 28.90, false),
('4f3e2d1c-0b9a-8f7e-6d5c-4b3a2f1e0d9c', '2f3a4b5c-6d7e-8f9a-0b1c-2d3e4f5a6b7c', 'Risotto de Camarão', 'Risotto cremoso com camarões frescos', 42.00, false),
('7b6a5f4e-3d2c-1b0a-9f8e-7d6c5b4a3f2e', '2f3a4b5c-6d7e-8f9a-0b1c-2d3e4f5a6b7c', 'Pizza Margherita', 'Pizza tradicional com mozzarella, tomate e manjericão', 35.00, true),
('0e9d8c7b-6a5f-4e3d-2c1b-0a9f8e7d6c5b', '2f3a4b5c-6d7e-8f9a-0b1c-2d3e4f5a6b7c', 'Tiramisu da Casa', 'Sobremesa italiana tradicional', 18.00, false)
ON CONFLICT (id) DO NOTHING;
