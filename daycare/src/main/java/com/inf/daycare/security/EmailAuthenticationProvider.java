package com.inf.daycare.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import com.inf.daycare.repositories.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EmailAuthenticationProvider implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        // 1. Buscar al usuario por email para obtener su ID y contraseña hasheada
        com.inf.daycare.models.User userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Email o contraseña incorrectos."));

        // 2. Verificar la contraseña usando el PasswordEncoder
        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            throw new BadCredentialsException("Email o contraseña incorrectos.");
        }

        // 3. ¡CONSTRUYE EL UserDetails AQUÍ DIRECTAMENTE! (sin llamar a UserDetailsService.loadUserByUsername)
        // Esto evita el ciclo, ya que no estamos inyectando UserDetailsService.
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                String.valueOf(userEntity.getId()),       // Identificador único para Spring Security (el ID del usuario)
                userEntity.getPassword(),                 // Contraseña hasheada
                userEntity.isEnabled(),
                true,                               // accountNonExpired
                true,                               // credentialsNonExpired
                true,                               // accountNonLocked
                userEntity.getRoles().stream()            // authorities
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList())
        );

        // 4. Devolver un token de autenticación exitoso
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
