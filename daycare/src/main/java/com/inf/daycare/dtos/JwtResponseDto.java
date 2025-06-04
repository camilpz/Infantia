package com.inf.daycare.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponseDto {
    private String token;
    private Long id;
    private String email;
    private String type = "Bearer"; //Tipo de token
    private List<String> roles; //Los roles del usuario

    //Constructor específico para la respuesta del token
    public JwtResponseDto(String accessToken, Long id, String email, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.email = email;
        this.roles = roles;
    }
}
