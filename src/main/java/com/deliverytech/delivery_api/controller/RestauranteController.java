package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.restaurante.RestauranteDTO;
import com.deliverytech.delivery_api.dto.restaurante.RestauranteResponseDTO;
import com.deliverytech.delivery_api.service.RestauranteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/restaurantes")
@Tag(name = "Restaurantes", description = "Endpoints para gerenciamento de restaurantes e consultas")
public class RestauranteController {

    @Autowired
    private RestauranteService restauranteService;

    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Cadastrar restaurante", description = "Cadastra um novo restaurante no catálogo")
    public ResponseEntity<RestauranteResponseDTO> cadastrar(@Valid @RequestBody RestauranteDTO dto) {
        RestauranteResponseDTO response = restauranteService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar restaurantes", description = "Lista restaurantes com paginação e filtros opcionais por categoria e status ativo")
    public ResponseEntity<Page<RestauranteResponseDTO>> listar(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Boolean ativo,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<RestauranteResponseDTO> pagina = restauranteService.listarRestaurantes(categoria, ativo, pageable);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar restaurante por ID", description = "Retorna os detalhes de um restaurante específico pelo seu ID")
    public ResponseEntity<RestauranteResponseDTO> buscarPorId(@PathVariable Long id) {
        RestauranteResponseDTO restaurante = restauranteService.buscarRestaurantePorId(id);
        return ResponseEntity.ok(restaurante);
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Atualizar restaurante", description = "Atualiza os dados de um restaurante existente")
    public ResponseEntity<RestauranteResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody RestauranteDTO dto) {
        RestauranteResponseDTO atualizado = restauranteService.atualizarRestaurante(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @PatchMapping("/{id}/status")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Alternar status do restaurante", description = "Ativa ou desativa um restaurante existente")
    public ResponseEntity<RestauranteResponseDTO> alterarStatus(@PathVariable Long id) {
        RestauranteResponseDTO response = restauranteService.alterarStatusRestaurante(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Buscar restaurantes por categoria", description = "Retorna restaurantes ativos pertencentes a uma categoria específica")
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorCategoria(@PathVariable String categoria) {
        List<RestauranteResponseDTO> restaurantes = restauranteService.buscarRestaurantesPorCategoria(categoria);
        return ResponseEntity.ok(restaurantes);
    }

    @GetMapping("/{id}/taxa-entrega")
    @Operation(summary = "Calcular taxa de entrega", description = "Calcula o valor da taxa de entrega do restaurante para o CEP informado")
    public ResponseEntity<Map<String, Object>> calcularTaxaEntrega(
            @PathVariable Long id,
            @RequestParam String cep) {
        BigDecimal taxa = restauranteService.calcularTaxaEntrega(id, cep);
        return ResponseEntity.ok(Map.of("restauranteId", id, "cep", cep, "taxaEntrega", taxa));
    }

    @GetMapping("/proximos")
    @Operation(summary = "Buscar restaurantes próximos", description = "Busca restaurantes próximos a um CEP dentro do raio informado (em km)")
    public ResponseEntity<List<RestauranteResponseDTO>> buscarProximos(
            @RequestParam String cep,
            @RequestParam(defaultValue = "10") Integer raio) {
        List<RestauranteResponseDTO> restaurantes = restauranteService.buscarRestaurantesProximos(cep, raio);
        return ResponseEntity.ok(restaurantes);
    }

    @GetMapping("/avaliacao")
    @Operation(summary = "Buscar restaurantes por avaliação mínima", description = "Retorna restaurantes com nota de avaliação maior ou igual à informada")
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorAvaliacao(@RequestParam BigDecimal nota) {
        List<RestauranteResponseDTO> restaurantes = restauranteService.buscarRestauranteAvaliacao(nota);
        return ResponseEntity.ok(restaurantes);
    }
}
