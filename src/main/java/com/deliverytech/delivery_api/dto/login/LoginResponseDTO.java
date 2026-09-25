package com.deliverytech.delivery_api.dto.login;

import com.deliverytech.delivery_api.dto.user.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {

    private String token;
    private String tipo = "Bearer";
    private Long expiracao;
    private UserResponseDTO usuario;

    public LoginResponseDTO(String token, Long expiracao, UserResponseDTO usuario) {
        this.token = token;
        this.tipo = "Bearer";
        this.expiracao = expiracao;
        this.usuario = usuario;
    }
}
