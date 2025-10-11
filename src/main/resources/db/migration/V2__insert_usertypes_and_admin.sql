-- Cria tipos de usuário se não existirem
INSERT INTO user_types (name)
VALUES ('ADMIN')
ON CONFLICT (name) DO NOTHING;

INSERT INTO user_types (name)
VALUES ('OWNER')
ON CONFLICT (name) DO NOTHING;

INSERT INTO user_types (name)
VALUES ('CUSTOMER')
ON CONFLICT (name) DO NOTHING;

-- Cria usuário admin se não existir
DO $$
    DECLARE
        admin_type_id UUID;
        existing_admin_id UUID;
    BEGIN
        SELECT id INTO admin_type_id FROM user_types WHERE name = 'ADMIN';

        SELECT id INTO existing_admin_id FROM users WHERE email = 'admin@foodfiapp.local';

        IF existing_admin_id IS NULL THEN
            INSERT INTO users (id, name, email, login, password, user_type_id, created_at, updated_at)
            VALUES (
                       gen_random_uuid(),
                       'Admin',
                       'admin@foodfiapp.local',
                       'admin',
                       '$2a$10$YQGxZs6lAmQJoZQpM0I1xuE5zRzR9bM93hEnjokb6aZT6H5V2ID2O', -- senha: admin123 (BCrypt)
                       admin_type_id,
                       NOW(),
                       NOW()
                   );
            RAISE NOTICE 'Usuário admin criado com email=admin@foodfiapp.local (senha=admin123)';
        ELSE
            RAISE NOTICE 'Usuário admin já existe.';
        END IF;
    END $$;
