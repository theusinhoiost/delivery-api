package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.produto.ProdutoDTO;
import com.deliverytech.delivery_api.dto.produto.ProdutoResponseDTO;
import com.deliverytech.delivery_api.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@Tag(name = "Produtos", description = "Endpoints para gerenciamento do catálogo de produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Cadastrar produto", description = "Cadastra um novo produto vinculado a um restaurante")
    public ResponseEntity<ProdutoResponseDTO> cadastrar(@Valid @RequestBody ProdutoDTO dto) {
        ProdutoResponseDTO response = produtoService.cadastrarProduto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Retorna os detalhes de um produto específico")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
        ProdutoResponseDTO produto = produtoService.buscarProdutoPorId(id);
        return ResponseEntity.ok(produto);
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Atualizar produto", description = "Atualiza os dados de um produto existente")
    public ResponseEntity<ProdutoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoDTO dto) {
        ProdutoResponseDTO atualizado = produtoService.atualizarProduto(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Remover produto", description = "Remove um produto do catálogo pelo seu ID")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        produtoService.removerProduto(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/disponibilidade")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Alternar disponibilidade", description = "Alterna o status de disponibilidade do produto para vendas")
    public ResponseEntity<ProdutoResponseDTO> alterarDisponibilidade(@PathVariable Long id) {
        ProdutoResponseDTO atualizado = produtoService.alterarDisponibilidade(id);
        return ResponseEntity.ok(atualizado);
    }

    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Buscar produtos por categoria", description = "Lista todos os produtos disponíveis de uma categoria")
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorCategoria(@PathVariable String categoria) {
        List<ProdutoResponseDTO> produtos = produtoService.buscarProdutosPorCategoria(categoria);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar produtos por nome", description = "Pesquisa produtos através de parte do seu nome")
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorNome(@RequestParam String nome) {
        List<ProdutoResponseDTO> produtos = produtoService.buscarProdutosPorNome(nome);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/restaurante/{restauranteId}")
    @Operation(summary = "Buscar produtos por restaurante", description = "Lista os produtos de um restaurante, com filtro opcional por disponibilidade")
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorRestaurante(
            @PathVariable Long restauranteId,
            @RequestParam(required = false) Boolean disponivel) {
        List<ProdutoResponseDTO> produtos = produtoService.buscarProdutosPorRestaurante(restauranteId, disponivel);
        return ResponseEntity.ok(produtos);
    }
}
