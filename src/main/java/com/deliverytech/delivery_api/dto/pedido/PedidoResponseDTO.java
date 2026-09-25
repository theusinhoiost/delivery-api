package com.deliverytech.delivery_api.dto.pedido;

import com.deliverytech.delivery_api.enums.StatusPedido;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDTO {

    private Long id;
    private String numeroPedido;
    private LocalDateTime dataPedido;
    private Boolean entrega;
    private BigDecimal subtotal;
    private BigDecimal taxaEntrega;
    private BigDecimal valorTotal;
    private StatusPedido statusPedido;

    // Dados resumidos do cliente
    private Long clienteId;
    private String clienteNome;

    // Dados resumidos do restaurante
    private Long restauranteId;
    private String restauranteNome;

}