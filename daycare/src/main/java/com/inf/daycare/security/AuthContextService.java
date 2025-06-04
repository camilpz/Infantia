package com.inf.daycare.security;

import com.inf.daycare.models.Director;
import com.inf.daycare.models.Teacher;
import com.inf.daycare.models.Tutor;
import com.inf.daycare.repositories.DirectorRepository;
import com.inf.daycare.repositories.TeacherRepository;
import com.inf.daycare.repositories.TutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthContextService {
    private final TutorRepository tutorRepository;
    private final TeacherRepository teacherRepository;
    private final DirectorRepository directorRepository;

    /**
     * Obtiene el ID del usuario autenticado del contexto de seguridad de Spring.
     * Este es el ID de la tabla 'user_table'.
     * @return El ID del usuario autenticado como Long.
     * @throws IllegalStateException Si el usuario no está autenticado o el principal no es del tipo esperado.
     */
    public Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new IllegalStateException("Usuario no autenticado o no disponible en el contexto de seguridad.");
        }

        // El 'principal' aquí es el objeto que pusimos en el UserDetailsServiceImpl
        // que debería ser el ID de tu User (Long), pero lo pasamos como String.
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // El 'username' de UserDetails es el ID de tu User (Long) como String
            return Long.valueOf(userDetails.getUsername());
        } else if (authentication.getPrincipal() instanceof String) {
            // En algunos casos, si no se carga el UserDetails completo, podría ser solo el String del ID.
            return Long.valueOf((String) authentication.getPrincipal());
        }
        throw new IllegalStateException("Tipo de principal de autenticación inesperado.");
    }

    /**
     * Verifica si el usuario autenticado tiene un rol específico.
     * @param roleName El nombre del rol (ej. "ROLE_TUTOR", "ROLE_ADMIN").
     * @return true si el usuario tiene el rol, false en caso contrario.
     */
    public boolean hasRole(String roleName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(roleName));
    }

    /**
     * Obtiene el perfil de Tutor del usuario autenticado.
     * @return Un Optional que contiene el Tutor si el usuario es un tutor, vacío en caso contrario.
     */
    public Optional<Tutor> getAuthenticatedTutor() {
        Long userId = getAuthenticatedUserId();
        // Asegúrate de que tu UserRepository tenga un método findById o que puedas cargar el User
        // y luego acceder a user.getTutorProfile() con fetch EAGER o cargado previamente.
        // O directamente buscar el Tutor por el User ID:
        return tutorRepository.findByUser_Id(userId); // Asumiendo que existe findByUser_Id en TutorRepository
    }

    /**
     * Obtiene el perfil de Teacher del usuario autenticado.
     * @return Un Optional que contiene el Teacher si el usuario es un profesor, vacío en caso contrario.
     */
    public Optional<Teacher> getAuthenticatedTeacher() {
        Long userId = getAuthenticatedUserId();
        return teacherRepository.findByUser_Id(userId); // Asumiendo que existe findByUser_Id en TeacherRepository
    }

    /**
     * Obtiene el perfil de Director del usuario autenticado.
     * @return Un Optional que contiene el Director si el usuario es un director, vacío en caso contrario.
     */
    public Optional<Director> getAuthenticatedDirector() {
        Long userId = getAuthenticatedUserId();
        return directorRepository.findByUser_Id(userId); // Asumiendo que existe findByUser_Id en DirectorRepository
    }
}
