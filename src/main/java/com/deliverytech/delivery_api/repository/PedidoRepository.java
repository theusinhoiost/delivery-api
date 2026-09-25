package com.deliverytech.delivery_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.entity.Pedido;
import com.deliverytech.delivery_api.enums.StatusPedido;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

        // Buscar pedidos por cliente
        List<Pedido> findByClienteOrderByDataPedidoDesc(Cliente cliente);

        // Buscar pedidos por cliente ID
        List<Pedido> findByClienteIdOrderByDataPedidoDesc(Long clienteId);

        /*
         * Buscar pedidos por cliente ID já trazendo os itens junto (fetch join).
         */
        @Query("SELECT DISTINCT p FROM Pedido p LEFT JOIN FETCH p.itens " +
                        "WHERE p.cliente.id = :clienteId ORDER BY p.dataPedido DESC")
        List<Pedido> findByClienteIdComItens(@Param("clienteId") Long clienteId);

        // Buscar por status
        List<Pedido> findByStatusPedidoOrderByDataPedidoDesc(StatusPedido statusPedido);

        // Buscar por número do pedido
        Pedido findByNumeroPedido(String numeroPedido);

        /*
         * Buscar pedido por ID já trazendo os itens junto (fetch join).
         */
        @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.itens WHERE p.id = :id")
        Optional<Pedido> findByIdComItens(@Param("id") Long id);

        // Buscar pedidos por período genérico
        List<Pedido> findByDataPedidoBetweenOrderByDataPedidoDesc(LocalDateTime inicio, LocalDateTime fim);

        // Buscar pedidos por restaurante
        @Query("SELECT p FROM Pedido p WHERE p.restaurante.id = :restauranteId ORDER BY p.dataPedido DESC")
        List<Pedido> findByRestauranteId(@Param("restauranteId") Long restauranteId);

        // Relatório - pedidos por status
        @Query("SELECT p.statusPedido, COUNT(p) FROM Pedido p GROUP BY p.statusPedido")
        List<Object[]> countOrdersByStatus();

        // Pedidos pendentes (para dashboard)
        @Query("SELECT p FROM Pedido p WHERE p.statusPedido IN ('PENDENTE', 'CONFIRMADO', 'PREPARANDO') " +
                        "ORDER BY p.dataPedido ASC")
        List<Pedido> findOrdersPendentes();

        // Buscar pedidos de um dia específico
        @Query("SELECT p FROM Pedido p WHERE p.dataPedido >= :inicioDia AND p.dataPedido <= :fimDia ORDER BY p.dataPedido DESC")
        List<Pedido> findOrdersDoDia(
                        @Param("inicioDia") LocalDateTime inicioDia,
                        @Param("fimDia") LocalDateTime fimDia);

        // Buscar pedidos por restaurante (alternativo)
        @Query("SELECT p FROM Pedido p WHERE p.restaurante.id = :restauranteId ORDER BY p.dataPedido DESC")
        List<Pedido> findPedidosPorRestaurante(@Param("restauranteId") Long restauranteId);
}