package com.fiap.foodfiapp.infrastructure.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
@Profile("!test")
public class DatabaseInitializer implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        updatePasswordHashes();
    }

    private void updatePasswordHashes() {
        String adminPassword = passwordEncoder.encode("admin123");
        jdbcTemplate.update(
                "UPDATE users SET password = ? WHERE login = ?",
                adminPassword, "admin"
        );

        String ownerPassword = passwordEncoder.encode("owner123");
        jdbcTemplate.update(
                "UPDATE users SET password = ? WHERE login = ?",
                ownerPassword, "joaosilva"
        );
        jdbcTemplate.update(
                "UPDATE users SET password = ? WHERE login = ?",
                ownerPassword, "mariasantos"
        );

        String customerPassword = passwordEncoder.encode("customer123");
        jdbcTemplate.update(
                "UPDATE users SET password = ? WHERE login = ?",
                customerPassword, "carlosoliveira"
        );
        jdbcTemplate.update(
                "UPDATE users SET password = ? WHERE login = ?",
                customerPassword, "anacosta"
        );
        jdbcTemplate.update(
                "UPDATE users SET password = ? WHERE login = ?",
                customerPassword, "pedrolima"
        );

        List<Map<String, Object>> users = jdbcTemplate.queryForList(
                "SELECT u.login, u.name, ut.name as user_type FROM users u " +
                        "JOIN users_type ut ON u.user_type_uuid = ut.uuid " +
                        "ORDER BY ut.name, u.name"
        );

        System.out.println("\n=== USUÁRIOS INICIALIZADOS NO POSTGRESQL ===");
        users.forEach(user -> {
            System.out.printf("Login: %s | Nome: %s | Tipo: %s%n",
                    user.get("login"), user.get("name"), user.get("user_type"));
        });
        System.out.println("==========================================\n");

        System.out.println("Credenciais de acesso:");
        System.out.println("Admin - Login: admin, Senha: admin123");
        System.out.println("Owners - Login: joaosilva/mariasantos, Senha: owner123");
        System.out.println("Customers - Login: carlosoliveira/anacosta/pedrolima, Senha: customer123");
    }
}
