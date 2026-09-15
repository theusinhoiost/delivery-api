package com.deliveryTech.delivery_api.repository;

import com.deliveryTech.delivery_api.entity.Restaurante;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    // Buscar por nome
    Optional<Restaurante> findByNome(String nome);

    // Buscar restaurants ativos
    List<Restaurante> findByAtivoTrue();

    // Buscar por categoria
    List<Restaurante> findByCategoriaAndAtivoTrue(String categoria);

    // Buscar por nome contendo (case insensitive)
    List<Restaurante> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);

    // Buscar por avaliação mínima
    List<Restaurante> findByAvaliacaoGreaterThanEqualAndAtivoTrue(BigDecimal avaliacao);

    // Ordenar por avaliação (descendente)
    List<Restaurante> findByAtivoTrueOrderByAvaliacaoDesc();

    // Query customizada - restaurants com produtos
    @Query("SELECT DISTINCT r FROM Restaurant r JOIN r.produtos p WHERE r.ativo = true")
    List<Restaurante> findRestaurantsComProdutos();

    // Buscar por faixa de taxa de entrega
    @Query("SELECT r FROM Restaurant r WHERE r.taxaEntrega BETWEEN :min AND :max AND r.ativo = true")
    List<Restaurante> findByTaxaEntregaBetween(@Param("min") BigDecimal min, @Param("max") BigDecimal max);

    // Categorias disponíveis
    @Query("SELECT DISTINCT r.categoria FROM Restaurant r WHERE r.ativo = true ORDER BY r.categoria")
    List<String> findCategoriasDisponiveis();
}