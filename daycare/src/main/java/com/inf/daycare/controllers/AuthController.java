package com.inf.daycare.controllers;

import com.inf.daycare.dtos.JwtResponseDto;
import com.inf.daycare.dtos.post.LoginDto;
import com.inf.daycare.models.User;
import com.inf.daycare.repositories.UserRepository;
import com.inf.daycare.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository; // Necesario para obtener el ID y email del usuario

    // No inyectamos AuthContextService aquí directamente para el login,
    // ya que este controlador es la puerta de entrada a la autenticación.
    // El AuthContextService se usará en tus servicios de negocio una vez que el usuario esté autenticado.

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider,
                          UserRepository userRepository) { // Constructor actualizado
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDto loginDto) {

        // 1. Autenticar al usuario usando el AuthenticationManager
        // authenticationManager usará tu UserDetailsServiceImpl y PasswordEncoder
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

        // 2. Establecer la autenticación en el SecurityContextHolder para la duración de esta petición
        // Esto es importante si otras partes de la aplicación necesitan acceder al contexto de seguridad
        // inmediatamente después del login (aunque el JWT es sin estado y se usará en futuras peticiones).
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Generar el token JWT utilizando el objeto Authentication recién creado
        String jwt = jwtTokenProvider.generateJwtToken(authentication);

        // 4. Obtener los detalles del usuario autenticado del objeto UserDetails
        // Este UserDetails es el que retornó tu UserDetailsServiceImpl
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 5. Obtener el ID del usuario (que configuramos como el "username" en UserDetailsServiceImpl)
        Long userId = Long.valueOf(userDetails.getUsername());

        // 6. Cargar el objeto User completo de la base de datos para obtener el email y roles
        // Hacemos esto porque userDetails solo contiene el 'username' (ID), password y authorities (roles).
        // Si necesitas el email original o otros campos de la entidad User para la respuesta, la cargas aquí.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Error: Usuario no encontrado después de una autenticación exitosa. Esto no debería ocurrir."));

        // 7. Obtener los nombres de los roles del usuario para la respuesta
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName()) // Asumiendo que Role.getName() devuelve "ROLE_TUTOR", "ROLE_ADMIN", etc.
                .collect(Collectors.toList());

        // 8. Devolver el token JWT y la información básica del usuario en la respuesta
        return ResponseEntity.ok(new JwtResponseDto(jwt, user.getId(), user.getEmail(), roles));
    }
}
