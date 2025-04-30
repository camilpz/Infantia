package com.inf.auth.security;

import com.inf.auth.clients.UserClient;
import com.inf.auth.dtos.CustomUserDetails;
import com.inf.auth.dtos.UserRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAuthenticationProvider implements AuthenticationProvider {

    private final UserClient userClient; // Cliente para consultar al micro de users
    private final PasswordEncoder passwordEncoder; // BCryptPasswordEncoder, etc.

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        // 1. Buscar usuario en el microservicio de users
        UserRecord userRecord = userClient.getUserByEmail(email); // Podés tirar una excepción si no lo encuentra

        if (userRecord == null || !passwordEncoder.matches(password, userRecord.password())) {
            throw new BadCredentialsException("Email o contraseña incorrectos");
        }

        // 2. Crear CustomUserDetails
        UserDetails userDetails = new CustomUserDetails(userRecord);

        // 3. Crear el token con authorities
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
