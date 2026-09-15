package com.deliveryTech.service;

import java.util.List;

import com.deliveryTech.delivery_api.dto.ItemPedidoDTO;
import com.deliveryTech.delivery_api.dto.PedidoDTO;
import com.deliveryTech.delivery_api.dto.PedidoResponseDTO;
import com.deliveryTech.delivery_api.enums.StatusPedido;

import java.math.BigDecimal;

public interface PedidoService {

    PedidoResponseDTO criarPedido(PedidoDTO dto);

    PedidoResponseDTO buscarPedidoPorId(Long id);

    List<PedidoResponseDTO> buscarPedidosPorCliente(Long clienteId);

    PedidoResponseDTO atualizarStatusPedido(Long id, StatusPedido status);

    BigDecimal calcularTotalPedido(List<ItemPedidoDTO> itens);

    void cancelarPedido(Long id);

}