package com.inf.daycare.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwt.secret}") // Inyecta la clave secreta de application.properties
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}") // Inyecta el tiempo de expiración
    private int jwtExpirationMs;

    // Obtiene la clave de firma desde la cadena secreta
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    /**
     * Genera un token JWT para un usuario autenticado.
     * @param authentication El objeto Authentication de Spring Security
     * @return El token JWT generado
     */
    public String generateJwtToken(Authentication authentication) {
        // El 'principal' aquí es el objeto que pusimos en el UserDetailsServiceImpl
        // que debería ser el ID de tu User (Long), pero lo pasamos como String.
        // Asegúrate que tu UserDetailsServiceImpl devuelve user.getId().toString() como username.
        String userId = authentication.getName(); // Obtiene el "username" que configuramos como el userId

        // Extrae los roles/autoridades
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(userId) // El 'subject' (sub) del token será el userId
                .claim("roles", roles) // Añade los roles como un claim personalizado
                .setIssuedAt(new Date()) // Fecha de emisión
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // Fecha de expiración
                .signWith(key(), SignatureAlgorithm.HS256) // Firma el token con la clave secreta y algoritmo
                .compact(); // Construye y compacta el JWT
    }

    /**
     * Obtiene el ID del usuario (subject) de un token JWT.
     * @param token El token JWT
     * @return El ID del usuario como String
     */
    public String getUserIdFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Valida un token JWT.
     * @param authToken El token JWT a validar
     * @return true si el token es válido, false en caso contrario
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Token JWT inválido: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Token JWT expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Token JWT no soportado: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string vacío: {}", e.getMessage());
        } catch (SignatureException e) {
            logger.error("Firma JWT inválida: {}", e.getMessage()); // Excepción para firmas que no coinciden
        }

        return false;
    }
}
