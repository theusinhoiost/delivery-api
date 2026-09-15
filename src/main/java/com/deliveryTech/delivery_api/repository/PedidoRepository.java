package com.deliveryTech.delivery_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deliveryTech.delivery_api.entity.Cliente;
import com.deliveryTech.delivery_api.entity.Pedido;
import com.deliveryTech.delivery_api.enums.StatusPedido;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

        // Buscar orders por client
        List<Pedido> findByClientOrderByDataOrderDesc(Cliente client);

        // Buscar orders por client ID
        List<Pedido> findByClientIdOrderByDataOrderDesc(Long clientId);

        /*
         * Buscar orders por client ID já trazendo os itens junto (fetch join).
         * Usamos DISTINCT porque o JOIN FETCH com uma coleção pode gerar
         * linhas repetidas do mesmo order (uma por item), e o DISTINCT
         * remove essas duplicatas no resultado final.
         */
        @Query("SELECT DISTINCT p FROM Order p LEFT JOIN FETCH p.itens " +
                        "WHERE p.client.id = :clientId ORDER BY p.dataOrder DESC")
        List<Pedido> findByClientIdComItens(@Param("clientId") Long clientId);

        // Buscar por status
        List<Pedido> findByStatusOrderOrderByDataOrderDesc(StatusPedido statusOrder);

        // Buscar por número do order
        Pedido findByNumeroOrder(String numeroOrder);

        /*
         * Buscar order por ID já trazendo os itens junto (fetch join).
         * Isso evita o LazyInitializationException ao serializar o order em JSON,
         * já que a coleção "itens" é carregada dentro da própria consulta,
         * ainda dentro da transação.
         */
        @Query("SELECT p FROM Order p LEFT JOIN FETCH p.itens WHERE p.id = :id")
        Optional<Pedido> findByIdComItens(@Param("id") Long id);

        // Buscar orders por período genérico
        List<Pedido> findByDataOrderBetweenOrderByDataOrderDesc(LocalDateTime inicio, LocalDateTime fim);

        // Buscar orders por restaurante
        @Query("SELECT p FROM Order p WHERE p.restaurante.id = :restauranteId ORDER BY p.dataOrder DESC")
        List<Pedido> findByRestauranteId(@Param("restauranteId") Long restauranteId);

        // Relatório - orders por status
        @Query("SELECT p.statusOrder, COUNT(p) FROM Order p GROUP BY p.statusOrder")
        List<Object[]> countOrdersByStatus();

        // Orders pendentes (para dashboard)
        @Query("SELECT p FROM Order p WHERE p.statusOrder IN ('PENDENTE', 'CONFIRMADO', 'PREPARANDO') " +
                        "ORDER BY p.dataOrder ASC")
        List<Pedido> findOrdersPendentes();

        // CORREÇÃO: Buscar orders de um dia específico usando os parâmetros informados
        @Query("SELECT p FROM Order p WHERE p.dataOrder >= :inicioDia AND p.dataOrder <= :fimDia ORDER BY p.dataOrder DESC")
        List<Pedido> findOrdersDoDia(
                        @Param("inicioDia") LocalDateTime inicioDia,
                        @Param("fimDia") LocalDateTime fimDia);
}