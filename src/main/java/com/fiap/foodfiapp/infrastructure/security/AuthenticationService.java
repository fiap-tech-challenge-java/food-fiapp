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

    /**
     * Obtém o usuário atualmente autenticado
     */
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

    /**
     * Verifica se o usuário atual é um administrador
     */
    public boolean isCurrentUserAdmin() {
        try {
            User currentUser = getCurrentUser();
            return "ADMIN".equalsIgnoreCase(currentUser.getUserType().getName());
        } catch (UnauthorizedException e) {
            return false;
        }
    }

    /**
     * Verifica se o usuário atual pode acessar o perfil com o ID fornecido
     * (próprio usuário ou administrador)
     */
    public boolean canAccessUserProfile(UUID userId) {
        try {
            User currentUser = getCurrentUser();

            // Administrador pode acessar qualquer perfil
            if ("ADMIN".equalsIgnoreCase(currentUser.getUserType().getName())) {
                return true;
            }

            // Usuário pode acessar apenas seu próprio perfil
            return currentUser.getId().equals(userId);
        } catch (UnauthorizedException e) {
            return false;
        }
    }

    /**
     * Verifica se o usuário atual pode modificar o perfil com o ID fornecido
     * (próprio usuário ou administrador)
     */
    public boolean canModifyUserProfile(UUID userId) {
        return canAccessUserProfile(userId);
    }

    /**
     * Verifica se o usuário atual pode deletar o perfil com o ID fornecido
     * (apenas administrador)
     */
    public boolean canDeleteUserProfile(UUID userId) {
        return isCurrentUserAdmin();
    }
}
