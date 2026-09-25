package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.cliente.ClienteDTO;
import com.deliverytech.delivery_api.dto.cliente.ClienteResponseDTO;
import com.deliverytech.delivery_api.entity.Cliente;

import java.util.List;

public interface ClienteService {

    ClienteResponseDTO cadastrarCliente(Cliente novo);

    ClienteResponseDTO buscarClientePorId(Long id);

    ClienteResponseDTO buscarClientePorEmail(String email);

    ClienteResponseDTO atualizarCliente(Long id, ClienteDTO dto);

    ClienteResponseDTO ativarDesativarCliente(Long id);

    List<ClienteResponseDTO> listarClientesAtivos();

}