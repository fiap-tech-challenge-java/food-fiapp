package com.fiap.foodfiapp.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
@Profile("dev")
public class DatabaseInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.database.init.enabled:true}")
    private boolean initializationEnabled;

    @Value("${app.database.init.verbose-logging:false}")
    private boolean verboseLogging;

    public DatabaseInitializer(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (!initializationEnabled) {
            logger.info("Database initialization is disabled via configuration");
            return;
        }

        logger.info("Initializing database with development data...");
        updatePasswordHashes();
    }

    private void updatePasswordHashes() {
        try {
            int totalUpdated = 0;

            totalUpdated += updateUserPasswordIfNeeded("admin", "admin123");

            totalUpdated += updateUserPasswordIfNeeded("joaosilva", "owner123");
            totalUpdated += updateUserPasswordIfNeeded("mariasantos", "owner123");

            totalUpdated += updateUserPasswordIfNeeded("carlosoliveira", "customer123");
            totalUpdated += updateUserPasswordIfNeeded("anacosta", "customer123");
            totalUpdated += updateUserPasswordIfNeeded("pedrolima", "customer123");

            if (totalUpdated > 0) {
                logger.info("Updated {} user passwords with proper hashing", totalUpdated);
            } else {
                logger.info("All users already have properly hashed passwords");
            }

            if (verboseLogging) {
                displayInitializedUsers();
            } else {
                logger.info("Development users are available (set app.database.init.verbose-logging=true for details)");
            }

            logger.info("Database initialization completed successfully");

        } catch (Exception e) {
            logger.error("Error during database initialization", e);
            throw e;
        }
    }

    private int updateUserPasswordIfNeeded(String login, String plainPassword) {
        try {
            String currentPassword = jdbcTemplate.queryForObject(
                    "SELECT password FROM users WHERE login = ?",
                    String.class,
                    login
            );

            if (currentPassword != null && (currentPassword.startsWith("$2a$") ||
                    currentPassword.startsWith("$2b$") ||
                    currentPassword.startsWith("$2y$"))) {
                logger.debug("User '{}' already has a hashed password, skipping", login);
                return 0;
            }

            String hashedPassword = passwordEncoder.encode(plainPassword);
            int updated = jdbcTemplate.update(
                    "UPDATE users SET password = ? WHERE login = ?",
                    hashedPassword, login
            );

            if (updated > 0) {
                logger.debug("Updated password for user '{}'", login);
            }

            return updated;

        } catch (Exception e) {
            logger.warn("Could not update password for user '{}': {}", login, e.getMessage());
            return 0;
        }
    }

    private void displayInitializedUsers() {
        try {
            List<Map<String, Object>> users = jdbcTemplate.queryForList(
                    "SELECT u.login, u.name, ut.name as user_type FROM users u " +
                            "JOIN users_type ut ON u.user_type_uuid = ut.uuid " +
                            "ORDER BY ut.name, u.name"
            );

            logger.info("=== DEVELOPMENT USERS AVAILABLE ===");

            Map<String, Long> userTypeCounts = users.stream()
                    .collect(java.util.stream.Collectors.groupingBy(
                            user -> (String) user.get("user_type"),
                            java.util.stream.Collectors.counting()
                    ));

            userTypeCounts.forEach((type, count) ->
                    logger.info("{} users: {} available", type, count));

            logger.info("===================================");
            logger.warn("This is a DEVELOPMENT environment with test users");

        } catch (Exception e) {
            logger.error("Error displaying user information", e);
        }
    }
}
