package com.deliverytech.delivery_api;

import com.deliverytech.delivery_api.dto.produto.ProdutoDTO;
import com.deliverytech.delivery_api.dto.produto.ProdutoResponseDTO;
import com.deliverytech.delivery_api.entity.Produto;
import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.service.impl.ProdutoServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private RestauranteRepository restauranteRepository;

    @InjectMocks
    private ProdutoServiceImpl service;

    @Test
    @DisplayName("Deve cadastrar produto com sucesso quando restaurante existir")
    void deveCadastrarProdutoComSucesso() {
        // ARRANGE
        Restaurante restaurante = new Restaurante();
        restaurante.setId(10L);
        restaurante.setNome("Pizzaria Bella");

        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome("Pizza Margherita");
        dto.setDescricao("Molho de tomate, queijo e manjericão");
        dto.setPreco(new BigDecimal("45.00"));
        dto.setCategoria("Pizzas");
        dto.setRestauranteId(10L);

        when(restauranteRepository.findById(10L)).thenReturn(Optional.of(restaurante));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(inv -> {
            Produto p = inv.getArgument(0);
            p.setId(1L);
            return p;
        });

        // ACT
        ProdutoResponseDTO resultado = service.cadastrarProduto(dto);

        // ASSERT
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("Pizza Margherita");
        assertThat(resultado.getPreco()).isEqualTo(45.00);
        assertThat(resultado.isDisponivel()).isTrue();
        assertThat(resultado.getRestauranteId()).isEqualTo(10L);

        verify(restauranteRepository).findById(10L);
        verify(produtoRepository).save(any(Produto.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar cadastrar produto para restaurante inexistente")
    void deveLancarExcecaoQuandoRestauranteNaoExistir() {
        // ARRANGE
        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome("Hambúrguer");
        dto.setRestauranteId(99L);

        when(restauranteRepository.findById(99L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> service.cadastrarProduto(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Restaurante não encontrado: 99");

        verify(produtoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve buscar produto por ID com sucesso")
    void deveBuscarProdutoPorIdComSucesso() {
        // ARRANGE
        Restaurante restaurante = new Restaurante();
        restaurante.setId(10L);

        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Refrigerante");
        produto.setPreco(new BigDecimal("7.50"));
        produto.setDisponivel(true);
        produto.setRestaurante(restaurante);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        // ACT
        ProdutoResponseDTO resultado = service.buscarProdutoPorId(1L);

        // ASSERT
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("Refrigerante");
        assertThat(resultado.getPreco()).isEqualTo(7.50);

        verify(produtoRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar produto com ID inexistente")
    void deveLancarExcecaoQuandoProdutoNaoEncontrado() {
        // ARRANGE
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> service.buscarProdutoPorId(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Produto não encontrado: 99");
    }

    @Test
    @DisplayName("Deve remover produto com sucesso")
    void deveRemoverProdutoComSucesso() {
        // ARRANGE
        Produto produto = new Produto();
        produto.setId(1L);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        doNothing().when(produtoRepository).delete(produto);

        // ACT
        service.removerProduto(1L);

        // ASSERT
        verify(produtoRepository).findById(1L);
        verify(produtoRepository).delete(produto);
    }
}
