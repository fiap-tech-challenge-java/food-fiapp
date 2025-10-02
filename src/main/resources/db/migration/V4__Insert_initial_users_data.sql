INSERT INTO users_type (uuid, name) VALUES
('f47ac10b-58cc-4372-a567-0e02b2c3d479', 'ADMIN'),
('6ba7b810-9dad-11d1-80b4-00c04fd430c8', 'OWNER'),
('3e4666bf-d5e6-4231-aa4c-4e2d7b1e8934', 'CUSTOMER')
ON CONFLICT (uuid) DO NOTHING;

INSERT INTO users (id, name, email, cpf, login, password, user_type_uuid, active) VALUES
('8f14e45f-ceea-467a-9a36-fcc6dc4a3b1c', 'Administrador do Sistema', 'admin@foodfiapp.com', '000.000.000-00', 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM7lbrkWg6kGqxRzKlYu', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', true)
ON CONFLICT (id) DO NOTHING;

INSERT INTO users (id, name, email, cpf, login, password, user_type_uuid, active) VALUES
('c9bf9e57-1685-4c89-bafb-ff5af830be8a', 'João Silva Proprietário', 'joao.silva@foodfiapp.com', '111.111.111-11', 'joaosilva', '$2a$10$8YGZ4cGfT9zXnKqX8YGZ4OzXnKqX8YGZ4cGfT9zXnKqX8YGZ4cGfTe', '6ba7b810-9dad-11d1-80b4-00c04fd430c8', true),
('a1b2c3d4-e5f6-7890-abcd-1234567890ef', 'Maria Santos Restaurantes', 'maria.santos@foodfiapp.com', '222.222.222-22', 'mariasantos', '$2a$10$8YGZ4cGfT9zXnKqX8YGZ4OzXnKqX8YGZ4cGfT9zXnKqX8YGZ4cGfTe', '6ba7b810-9dad-11d1-80b4-00c04fd430c8', true)
ON CONFLICT (id) DO NOTHING;

INSERT INTO users (id, name, email, cpf, login, password, user_type_uuid, active) VALUES
('d4c94a6e-ed6b-4d02-b9b4-d6b9c4b3e8f7', 'Carlos Oliveira Cliente', 'carlos.oliveira@foodfiapp.com', '333.333.333-33', 'carlosoliveira', '$2a$10$7YGZ4cGfT9zXnKqX8YGZ4OzXnKqX8YGZ4cGfT9zXnKqX8YGZ4cGfTe', '3e4666bf-d5e6-4231-aa4c-4e2d7b1e8934', true),
('f9a3b2c1-8d7e-4f5a-b6c7-d8e9f0a1b2c3', 'Ana Costa Cliente', 'ana.costa@foodfiapp.com', '444.444.444-44', 'anacosta', '$2a$10$7YGZ4cGfT9zXnKqX8YGZ4OzXnKqX8YGZ4cGfT9zXnKqX8YGZ4cGfTe', '3e4666bf-d5e6-4231-aa4c-4e2d7b1e8934', true),
('e2f8a9b7-c6d5-4e3f-a1b2-c3d4e5f6a7b8', 'Pedro Lima Cliente', 'pedro.lima@foodfiapp.com', '555.555.555-55', 'pedrolima', '$2a$10$7YGZ4cGfT9zXnKqX8YGZ4OzXnKqX8YGZ4cGfT9zXnKqX8YGZ4cGfTe', '3e4666bf-d5e6-4231-aa4c-4e2d7b1e8934', true)
ON CONFLICT (id) DO NOTHING;

INSERT INTO addresses (id, user_id, public_place, number, complement, neighborhood, city, state, postal_code)
VALUES
('b1c2d3e4-f5a6-7b8c-9d0e-1f2a3b4c5d6e'::UUID, '8f14e45f-ceea-467a-9a36-fcc6dc4a3b1c'::UUID, 'Rua da Administração', '100', 'Sala 1', 'Centro', 'São Paulo', 'SP', '01000-000'),
('a9b8c7d6-e5f4-3a2b-1c0d-9e8f7a6b5c4d'::UUID, 'c9bf9e57-1685-4c89-bafb-ff5af830be8a'::UUID, 'Avenida Paulista', '1000', 'Conjunto 501', 'Bela Vista', 'São Paulo', 'SP', '01310-100'),
('c5d4e3f2-a1b0-9c8d-7e6f-5a4b3c2d1e0f'::UUID, 'a1b2c3d4-e5f6-7890-abcd-1234567890ef'::UUID, 'Rua Augusta', '500', 'Loja 1', 'Consolação', 'São Paulo', 'SP', '01305-000'),
('f8e7d6c5-b4a3-2f1e-0d9c-8b7a6f5e4d3c'::UUID, 'd4c94a6e-ed6b-4d02-b9b4-d6b9c4b3e8f7'::UUID, 'Rua das Flores', '123', 'Apto 45', 'Jardim Primavera', 'São Paulo', 'SP', '04567-890'),
('d2c1b0a9-f8e7-6d5c-4b3a-2f1e0d9c8b7a'::UUID, 'f9a3b2c1-8d7e-4f5a-b6c7-d8e9f0a1b2c3'::UUID, 'Avenida Brasil', '456', 'Casa', 'Vila Nova', 'Rio de Janeiro', 'RJ', '12345-678'),
('e6f5a4b3-c2d1-0e9f-8a7b-6c5d4e3f2a1b'::UUID, 'e2f8a9b7-c6d5-4e3f-a1b2-c3d4e5f6a7b8'::UUID, 'Praça da Liberdade', '789', 'Bloco A Apto 102', 'Liberdade', 'Belo Horizonte', 'MG', '30140-010')
ON CONFLICT (id) DO NOTHING;
