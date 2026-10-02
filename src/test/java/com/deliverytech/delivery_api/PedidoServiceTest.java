package com.deliverytech.delivery_api;

import com.deliverytech.delivery_api.dto.pedido.ItemPedidoDTO;
import com.deliverytech.delivery_api.dto.pedido.PedidoDTO;
import com.deliverytech.delivery_api.dto.pedido.PedidoResponseDTO;
import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.entity.Pedido;
import com.deliverytech.delivery_api.entity.Produto;
import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.enums.StatusPedido;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.service.impl.PedidoServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PedidoServiceImpl service;

    @Test
    @DisplayName("Deve criar pedido com sucesso e calcular valores corretamente")
    void deveCriarPedidoComSucessoECalcularValores() {
        // ARRANGE
        Long clienteId = 1L;
        Long restauranteId = 2L;
        Long produtoId = 3L;

        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        cliente.setAtivo(true);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(restauranteId);
        restaurante.setAtivo(true);
        restaurante.setTaxaEntrega(new BigDecimal("6.00"));

        Produto produto = new Produto();
        produto.setId(produtoId);
        produto.setNome("Pizza 4 Queijos");
        produto.setPreco(new BigDecimal("40.00"));
        produto.setDisponivel(true);
        produto.setRestaurante(restaurante);

        ItemPedidoDTO itemDTO = new ItemPedidoDTO();
        itemDTO.setProdutoId(produtoId);
        itemDTO.setQuantidade(2); // 2 * 40.00 = 80.00 subtotal + 6.00 taxa = 86.00 total

        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setClienteId(clienteId);
        pedidoDTO.setRestauranteId(restauranteId);
        pedidoDTO.setEnderecoEntrega("Rua das Flores, 100");
        pedidoDTO.setItens(List.of(itemDTO));

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.of(restaurante));
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));

        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> {
            Pedido p = inv.getArgument(0);
            p.setId(100L);
            return p;
        });

        PedidoResponseDTO responseFake = new PedidoResponseDTO();
        responseFake.setId(100L);
        responseFake.setStatusPedido(StatusPedido.PENDENTE);
        responseFake.setSubtotal(new BigDecimal("80.00"));
        responseFake.setTaxaEntrega(new BigDecimal("6.00"));
        responseFake.setValorTotal(new BigDecimal("86.00"));

        when(modelMapper.map(any(Pedido.class), eq(PedidoResponseDTO.class))).thenReturn(responseFake);

        // ACT
        PedidoResponseDTO resultado = service.criarPedido(pedidoDTO);

        // ASSERT
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(100L);
        assertThat(resultado.getValorTotal()).isEqualByComparingTo(new BigDecimal("86.00"));
        assertThat(resultado.getStatusPedido()).isEqualTo(StatusPedido.PENDENTE);

        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando cliente estiver inativo")
    void deveLancarExcecaoQuandoClienteEstiverInativo() {
        // ARRANGE
        Cliente clienteInativo = new Cliente();
        clienteInativo.setId(1L);
        clienteInativo.setAtivo(false);

        PedidoDTO dto = new PedidoDTO();
        dto.setClienteId(1L);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteInativo));

        // ACT & ASSERT
        assertThatThrownBy(() -> service.criarPedido(dto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Cliente inativo não pode fazer pedidos");

        verify(pedidoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando produto pertencer a outro restaurante")
    void deveLancarExcecaoQuandoProdutoNaoPertenceAoRestaurante() {
        // ARRANGE
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setAtivo(true);

        Restaurante restaurante1 = new Restaurante();
        restaurante1.setId(1L);
        restaurante1.setAtivo(true);
        restaurante1.setTaxaEntrega(BigDecimal.ZERO);

        Restaurante restaurante2 = new Restaurante();
        restaurante2.setId(2L); // Outro restaurante

        Produto produto = new Produto();
        produto.setId(10L);
        produto.setNome("Burguer");
        produto.setPreco(new BigDecimal("25.00"));
        produto.setDisponivel(true);
        produto.setRestaurante(restaurante2); // pertence ao restaurante 2

        ItemPedidoDTO itemDTO = new ItemPedidoDTO();
        itemDTO.setProdutoId(10L);
        itemDTO.setQuantidade(1);

        PedidoDTO dto = new PedidoDTO();
        dto.setClienteId(1L);
        dto.setRestauranteId(1L); // pedido é pro restaurante 1
        dto.setItens(List.of(itemDTO));

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante1));
        when(produtoRepository.findById(10L)).thenReturn(Optional.of(produto));

        // ACT & ASSERT
        assertThatThrownBy(() -> service.criarPedido(dto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Produto não pertence ao restaurante selecionado");

        verify(pedidoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao tentar cancelar pedido já entregue")
    void deveLancarExcecaoAoCancelarPedidoJaEntregue() {
        // ARRANGE
        Pedido pedidoEntregue = new Pedido();
        pedidoEntregue.setId(50L);
        pedidoEntregue.setStatusPedido(StatusPedido.ENTREGUE);

        when(pedidoRepository.findById(50L)).thenReturn(Optional.of(pedidoEntregue));

        // ACT & ASSERT
        assertThatThrownBy(() -> service.cancelarPedido(50L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Pedido não pode ser cancelado no status: ENTREGUE");

        verify(pedidoRepository, never()).save(any());
    }
}
