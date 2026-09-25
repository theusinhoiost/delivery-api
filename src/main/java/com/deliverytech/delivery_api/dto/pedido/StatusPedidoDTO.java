package com.deliverytech.delivery_api.dto.pedido;

import com.deliverytech.delivery_api.enums.StatusPedido;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusPedidoDTO {

    @NotNull(message = "Status é obrigatório")
    private StatusPedido status;
}
