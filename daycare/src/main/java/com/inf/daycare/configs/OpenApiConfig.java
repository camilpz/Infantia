package com.inf.daycare.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Tu Nombre",
                        email = "tu.email@example.com",
                        url = "https://tu-sitio-web.com"
                ),
                description = "Documentación OpenAPI para el sistema de guardería (Daycare)",
                title = "API de Gestión de Guardería",
                version = "1.0",
                license = @License(
                        name = "Licencia de tu Proyecto",
                        url = "https://tu-licencia.com"
                ),
                termsOfService = "Términos del servicio"
        ),
        servers = {
                @Server(
                        description = "Servidor Local DEV",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Servidor de Producción",
                        url = "https://tu-api-produccion.com" // Si tuvieras uno
                )
        }
)
@SecurityScheme( // <--- ¡Esto es CRUCIAL para el JWT en Swagger!
        name = "BearerAuth", // Nombre que aparecerá en el botón "Authorize" de Swagger
        description = "JWT authentication using Bearer token",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER // Indica que el token va en el header Authorization
)
@Configuration // Asegúrate de que esta clase sea una configuración de Spring
public class OpenApiConfig {
}
