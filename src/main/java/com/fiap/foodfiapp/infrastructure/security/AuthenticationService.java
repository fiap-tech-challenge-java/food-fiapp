package com.fiap.foodfiapp.infrastructure.security;

import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.exception.UnauthorizedException;
import com.fiap.foodfiapp.core.domain.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
            "anonymousUser".equals(authentication.getPrincipal())) {
            throw new UnauthorizedException("User not authenticated");
        }

        String userLogin = authentication.getName();
        return userRepository.findByLogin(userLogin)
                .orElseThrow(() -> new UnauthorizedException("Invalid user credentials"));
    }

    public boolean isCurrentUserAdmin() {
        try {
            User currentUser = getCurrentUser();
            return "ADMIN".equalsIgnoreCase(currentUser.getUserType().getName());
        } catch (UnauthorizedException e) {
            return false;
        }
    }

    public boolean canAccessUserProfile(UUID userId) {
        try {
            User currentUser = getCurrentUser();

            if ("ADMIN".equalsIgnoreCase(currentUser.getUserType().getName())) {
                return true;
            }

            return currentUser.getId().equals(userId);
        } catch (UnauthorizedException e) {
            return false;
        }
    }

    public boolean canModifyUserProfile(UUID userId) {
        return canAccessUserProfile(userId);
    }

    public boolean canDeleteUserProfile(UUID userId) {
        return isCurrentUserAdmin();
    }
}
