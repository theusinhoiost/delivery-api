package com.deliverytech.delivery_api.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados para autenticação de usuário")
public class LoginRequestDTO {

    @Schema(description = "Email cadastrado", example = "admin@delivery.com")
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter formato válido")
    private String email;

    @Schema(description = "Senha de acesso", example = "senha123")
    @NotBlank(message = "Senha é obrigatória")
    private String senha;
}
