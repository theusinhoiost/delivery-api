package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.pedido.CalculoPedidoDTO;
import com.deliverytech.delivery_api.dto.pedido.CalculoPedidoResponseDTO;
import com.deliverytech.delivery_api.dto.pedido.PedidoDTO;
import com.deliverytech.delivery_api.dto.pedido.PedidoResponseDTO;
import com.deliverytech.delivery_api.dto.pedido.StatusPedidoDTO;
import com.deliverytech.delivery_api.enums.StatusPedido;
import com.deliverytech.delivery_api.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Endpoints para criação, acompanhamento e gerenciamento de pedidos")
@SecurityRequirement(name = "Bearer Authentication")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    @Operation(summary = "Criar novo pedido", description = "Cria um novo pedido validando cliente ativo, restaurante ativo e itens")
    public ResponseEntity<PedidoResponseDTO> criar(@Valid @RequestBody PedidoDTO dto) {
        PedidoResponseDTO response = pedidoService.criarPedido(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Retorna os detalhes de um pedido específico")
    public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable Long id) {
        PedidoResponseDTO pedido = pedidoService.buscarPedidoPorId(id);
        return ResponseEntity.ok(pedido);
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Buscar pedidos por cliente", description = "Lista todos os pedidos de um cliente específico")
    public ResponseEntity<?> buscarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(pedidoService.buscarPedidosPorCliente(clienteId));
    }

    @GetMapping("/restaurante/{restauranteId}")
    @Operation(summary = "Buscar pedidos por restaurante", description = "Lista todos os pedidos de um restaurante, com filtro opcional por status")
    public ResponseEntity<?> buscarPorRestaurante(
            @PathVariable Long restauranteId,
            @RequestParam(required = false) StatusPedido status) {
        return ResponseEntity.ok(pedidoService.buscarPedidosPorRestaurante(restauranteId, status));
    }

    @GetMapping
    @Operation(summary = "Listar pedidos paginados", description = "Retorna uma lista paginada de todos os pedidos")
    public ResponseEntity<?> listar(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        return ResponseEntity.ok(pedidoService.listarPedidos(pageable));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do pedido", description = "Atualiza o status de um pedido respeitando o fluxo permitido")
    public ResponseEntity<PedidoResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusPedidoDTO dto) {
        PedidoResponseDTO response = pedidoService.atualizarStatusPedido(id, dto.getStatus());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/calcular")
    @Operation(summary = "Calcular total do pedido", description = "Calcula o valor total dos itens selecionados antes de finalizar o pedido")
    public ResponseEntity<CalculoPedidoResponseDTO> calcularTotal(@Valid @RequestBody CalculoPedidoDTO dto) {
        CalculoPedidoResponseDTO response = pedidoService.calcularTotalPedido(dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar pedido", description = "Cancela um pedido em status permitido (PENDENTE ou CONFIRMADO)")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        pedidoService.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }
}
