package com.deliverytech.delivery_api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.deliverytech.delivery_api.dto.cliente.ClienteResponseDTO;
import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.service.impl.ClienteServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    ClienteRepository repository;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    ClienteServiceImpl service;

    @Test
    void deveCriarUsuarioQuandoDadosValidos() {
        // ARRANGE
        when(repository.findByEmail("matheus@email.com")).thenReturn(Optional.empty());
        when(repository.save(any(Cliente.class))).thenAnswer(inv -> {
            Cliente c = inv.getArgument(0);
            c.setId(1L);
            return c;
        });

        // Mocka o ModelMapper para retornar um DTO fake
        ClienteResponseDTO dtoFake = new ClienteResponseDTO();
        dtoFake.setId(1L);
        dtoFake.setNome("Matheus");
        dtoFake.setEmail("matheus@email.com");
        when(modelMapper.map(any(Cliente.class), eq(ClienteResponseDTO.class))).thenReturn(dtoFake);

        Cliente novo = new Cliente();
        novo.setNome("Matheus");
        novo.setEmail("matheus@email.com");
        novo.setTelefone("19999999999");
        novo.setEndereco("Rua A, 123");
        novo.setAtivo(true);

        // ACT
        ClienteResponseDTO resultado = service.cadastrarCliente(novo);

        // ASSERT
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("Matheus");

        verify(repository).save(any());
    }
}