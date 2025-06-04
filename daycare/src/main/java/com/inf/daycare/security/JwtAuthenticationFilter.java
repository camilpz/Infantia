package com.inf.daycare.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsServiceImpl userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = parseJwt(request); //Intenta extraer el token JWT

        if (jwt != null) {
            //Loguea el token recibido antes de la validación
            logger.info("JWT recibido en el filtro: {}", jwt);

            //Valida el token. JwtTokenProvider debería loguear sus propias excepciones.
            boolean isTokenValid = jwtTokenProvider.validateJwtToken(jwt);

            //Loguea el resultado de la validación
            logger.info("Resultado de la validación del JWT: {}", isTokenValid);

            if (isTokenValid) {
                try {
                    //Si el token es válido, extraemos el ID del usuario del token
                    String userId = jwtTokenProvider.getUserIdFromJwtToken(jwt);
                    logger.info("ID de usuario extraído del JWT: {}", userId);

                    //Cargamos los detalles del usuario desde la base de datos
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
                    logger.info("UserDetails cargados para usuario: {}", userDetails.getUsername());

                    //Creamos el objeto de autenticación de Spring Security
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    //Establecemos detalles adicionales de la petición
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    //Establecemos la autenticación en el SecurityContextHolder
                    //Esto hace que el usuario esté "autenticado" para esta petición
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.info("Autenticación establecida en SecurityContextHolder para: {}", userDetails.getUsername());

                } catch (Exception e) {
                    //Si ocurre CUALQUIER otra excepción aquí (ej. UserNotFoundException, etc.)
                    //la logueamos. El SecurityContextHolder NO se establecerá.
                    logger.error("Error al procesar el token JWT o establecer la autenticación: {}", e.getMessage(), e);
                }
            } else {
                //Mensaje si el token no es válido o ha expirado
                logger.warn("El token JWT no es válido o ha expirado. La autenticación será saltada para esta petición.");
            }
        } else {
            //Mensaje si no se encontró ningún token JWT en la cabecera
            logger.info("No se encontró token JWT 'Bearer' en la cabecera de la petición.");
        }

        //Siempre se debe llamar a filterChain.doFilter para que la petición continúe
        //a los siguientes filtros en la cadena de Spring Security.
        filterChain.doFilter(request, response);
    }

    //Método auxiliar para extraer el JWT del encabezado Authorization
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.toLowerCase().startsWith("bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
