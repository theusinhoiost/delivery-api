package com.deliverytech.delivery_api.dto.pedido;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculoPedidoDTO {

    @NotEmpty(message = "A lista de itens não pode estar vazia")
    @Valid
    private List<ItemPedidoDTO> itens;
}
