package com.deliverytech.delivery_api.dto.cliente;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email é obrigatório")
    private String email;

    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Telefone deve ter apenas dígitos e deve ser DDD + Telefone")
    private String telefone;

    @NotBlank(message = "Endereço é obrigatório")
    @Size(max = 200, message = "Endereço deve conter no máximo 200 caracteres")
    private String endereco;
}
