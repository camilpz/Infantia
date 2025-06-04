package com.inf.daycare.security;

import com.inf.daycare.models.User;
import com.inf.daycare.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String idAsString) throws UsernameNotFoundException {
        // 1. Parsear el ID del token JWT
        Long userId = Long.parseLong(idAsString);

        // 2. Cargar el usuario desde la base de datos
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con ID: " + userId));

        // 3. Forzar la carga de relaciones LAZY si las necesitas inmediatamente
        //    (aunque tu User ya implementa UserDetails, si Spring Security
        //     intenta acceder a un LAZY profile o documentType después de
        //     que la sesión se cierra, seguirás teniendo LazyInitializationException
        //     en esos puntos. Por lo tanto, es buena práctica forzar aquí si
        //     son usados por getAuthorities() o por tu AuthService sin @Transactional)

        // roles y contacts son EAGER en tu User, así que no necesitan .size() aquí
        // user.getRoles().size(); // No necesario si ya es EAGER
        // user.getContacts().size(); // No necesario si ya es EAGER

        // Estos sí son LAZY, y si tu AuthService o alguna otra lógica los necesita
        // fuera de una @Transactional, deberían ser cargados aquí o manejados transaccionalmente allá.
        // La excepción LazyInitializationException en tu depuración era para Teacher, Tutor, Director, DocumentType.
        if (user.getTeacherProfile() != null) user.getTeacherProfile().getId();
        if (user.getTutorProfile() != null) user.getTutorProfile().getId();
        if (user.getDirectorProfile() != null) user.getDirectorProfile().getId();
        if (user.getDocumentType() != null) user.getDocumentType().getId();


        // 4. ¡Devolver tu entidad User directamente!
        //    Esto es posible porque tu clase User YA implementa UserDetails.
        return user;
    }

}
