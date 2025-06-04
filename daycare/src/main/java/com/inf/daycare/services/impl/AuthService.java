package com.inf.daycare.services.impl;

import com.inf.daycare.models.User;
import com.inf.daycare.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    @Transactional
    public Long getLoggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException("No hay usuario autenticado en el contexto de seguridad.");
        }

        String userIdString = authentication.getName(); // Este es el ID de tu usuario, como String
        try {
            return Long.parseLong(userIdString);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("El ID del usuario autenticado no tiene un formato numérico válido: " + userIdString, e);
        }
    }

    public boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_" + role.toUpperCase()));
    }

    // --- NUEVOS MÉTODOS PARA OBTENER IDs ESPECÍFICOS DE PERFIL ---

    /**
     * Obtiene el ID del Tutor asociado al usuario autenticado.
     * @return El ID del Tutor.
     * @throws AccessDeniedException si el usuario autenticado no tiene un perfil de Tutor.
     */
    // Asegúrate de que tu User tiene un método getTutorProfile() que retorne el perfil de Tutor

    @Transactional
    public Long getLoggedInTutorId() {
        Long userId = getLoggedInUserId(); // Obtener el ID del usuario logueado
        User user = getUserOrThrow(userId);

        if (!hasRole("TUTOR") || user.getTutorProfile() == null) { // Asegura que tenga el rol Y el perfil
            throw new AccessDeniedException("El usuario autenticado no es un Tutor o no tiene un perfil de Tutor asociado.");
        }
        return user.getTutorProfile().getId(); // Asumiendo que tu User tiene un getTutorProfile()
    }

    /**
     * Obtiene el ID del Teacher asociado al usuario autenticado.
     * @return El ID del Teacher.
     * @throws AccessDeniedException si el usuario autenticado no tiene un perfil de Teacher.
     */

    @Transactional
    public Long getLoggedInTeacherId() {
        Long userId = getLoggedInUserId();
        User user = getUserOrThrow(userId);

        if (!hasRole("MAESTRO") || user.getTeacherProfile() == null) { // Asegura que tenga el rol Y el perfil
            throw new AccessDeniedException("El usuario autenticado no es un Teacher o no tiene un perfil de Teacher asociado.");
        }
        return user.getTeacherProfile().getId();
    }

    @Transactional
    public Long getLoggedInDirectorId() {
        Long userId = getLoggedInUserId();
        User user = getUserOrThrow(userId);

        if (!hasRole("DIRECTOR") || user.getDirectorProfile() == null) {
            throw new AccessDeniedException("El usuario autenticado no es un Director o no tiene un perfil de Director asociado.");
        }
        return user.getDirectorProfile().getId();
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario logueado no encontrado con ID: " + userId));
    }
}
