package com.deliverytech.delivery_api;

import com.deliverytech.delivery_api.dto.restaurante.RestauranteDTO;
import com.deliverytech.delivery_api.dto.restaurante.RestauranteResponseDTO;
import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.service.impl.RestauranteServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestauranteServiceTests {

    @Mock
    RestauranteRepository restauranteRepository; // Mock do repositório real

    @InjectMocks
    RestauranteServiceImpl service; // Service que vamos testar de verdade

    @Test
    void deveCriarRestauranteQuandoDadosValidos() {
        // ARRANGE - Prepara tudo que o teste precisa

        // 1. Cria o DTO de entrada (o que o controller receberia)
        RestauranteDTO dtoEntrada = new RestauranteDTO();
        dtoEntrada.setNome("Cantina do Matheus");
        dtoEntrada.setCategoria("Italiana");
        dtoEntrada.setEndereco("Rua A, 123");
        dtoEntrada.setTelefone("19999999999");
        dtoEntrada.setTaxaEntrega(new BigDecimal("5.90"));
        dtoEntrada.setTempoEntrega(30);
        dtoEntrada.setHorarioFuncionamento("18h-23h");

        // 2. Configura os mocks: o que o repositório deve fazer quando for chamado
        // Quando verificar se já existe restaurante com esse nome, retorna vazio (não
        // existe)
        when(restauranteRepository.findByNome("Cantina do Matheus")).thenReturn(Optional.empty());

        // Quando salvar, retorna o restaurante com ID (simulando o banco)
        when(restauranteRepository.save(any(Restaurante.class))).thenAnswer(inv -> {
            Restaurante r = inv.getArgument(0);
            r.setId(1L); // banco gerou o ID
            return r;
        });

        // ACT - Executa o método que você quer testar
        RestauranteResponseDTO resultado = service.cadastrar(dtoEntrada);

        // ASSERT - Verifica se o resultado é o esperado
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("Cantina do Matheus");
        assertThat(resultado.isAtivo()).isTrue();

        // Verifica se os métodos do repository foram chamados
        verify(restauranteRepository).findByNome("Cantina do Matheus");
        verify(restauranteRepository).save(any(Restaurante.class));
    }

    @Test
    void deveLancarExcecaoQuandoNomeJaExiste() {
        RestauranteDTO dto = new RestauranteDTO();
        dto.setNome("Já existe");

        when(restauranteRepository.findByNome("Já existe")).thenReturn(Optional.of(new Restaurante()));

        assertThatThrownBy(() -> service.cadastrar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Restaurante já cadastrado");

        verify(restauranteRepository, never()).save(any());
    }
}