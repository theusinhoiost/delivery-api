package com.deliverytech.delivery_api.dto.pedido;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculoPedidoResponseDTO {

    private BigDecimal valorTotal;
}
