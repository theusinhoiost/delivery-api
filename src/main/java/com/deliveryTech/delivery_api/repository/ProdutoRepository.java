package com.deliveryTech.delivery_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deliveryTech.delivery_api.entity.Produto;
import com.deliveryTech.delivery_api.entity.Restaurante;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

        List<Produto> findByRestauranteAndDisponivelTrue(Restaurante restaurante);

        // Buscar products por restaurante ID
        List<Produto> findByRestauranteIdAndDisponivelTrue(Long restauranteId);

        // Buscar por categoria
        List<Produto> findByCategoriaAndDisponivelTrue(String categoria);

        // Buscar por nome contendo
        List<Produto> findByNomeContainingIgnoreCaseAndDisponivelTrue(String nome);

        // Buscar por faixa de preço
        List<Produto> findByPrecoBetweenAndDisponivelTrue(BigDecimal precoMin, BigDecimal precoMax);

        // Buscar products mais baratos que um valor
        List<Produto> findByPrecoLessThanEqualAndDisponivelTrue(BigDecimal preco);

        // Ordenar por preço
        List<Produto> findByDisponivelTrueOrderByPrecoAsc();

        List<Produto> findByDisponivelTrueOrderByPrecoDesc();

        // Query customizada - products mais vendidos
        @Query("""
                            SELECT ip.product
                            FROM ItemPedido ip
                            GROUP BY ip.product
                            ORDER BY COUNT(ip) DESC
                        """)
        List<Produto> findProductsMaisVendidos();

        // Query customizada - products por restaurante e categoria
        @Query("SELECT p FROM Produto p WHERE p.restaurante.id = :restauranteId " +
                        "AND p.categoria = :categoria AND p.disponivel = true")
        List<Produto> findByRestauranteAndCategoria(@Param("restauranteId") Long restauranteId,
                        @Param("categoria") String categoria);

        // Contar products por restaurante
        @Query("SELECT COUNT(p) FROM Produto p WHERE p.restaurante.id = :restauranteId")
        long countByRestauranteId(@Param("restauranteId") Long restauranteId);
}