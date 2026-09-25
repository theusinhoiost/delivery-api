package com.deliverytech.delivery_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health & Info", description = "Endpoints para verificação de status e informações da aplicação")
public class HealthController {

  @GetMapping("/health")
  @Operation(summary = "Verificar integridade da API", description = "Retorna status UP e dados do ambiente")
  public Map<String, String> health() {
    return Map.of(
        "status",
        "UP",
        "timestamp",
        LocalDateTime.now().toString(),
        "service",
        "Delivery API",
        "javaVersion",
        System.getProperty("java.version"));
  }

  @GetMapping("/info")
  @Operation(summary = "Informações da aplicação", description = "Retorna versão, desenvolvedor e tecnologias utilizadas")
  public AppInfo info() {
    return new AppInfo(
        "Delivery Tech API",
        "1.0.0",
        "Matheus",
        "JDK 21",
        "Spring Boot 3.2.x");
  }

  // Record para demonstrar recurso do Java 14+ (disponível no JDK 21)
  public record AppInfo(
      String application,
      String version,
      String developer,
      String javaVersion,
      String framework) {
  }
}
