# Delivery Tech API

Sistema de delivery desenvolvido com Spring Boot e Java 21.

## 🚀 Tecnologias
- **Java 21 LTS** (versão mais recente)
- Spring Boot 3.2.x
- Spring Web
- Spring Data JPA
- H2 Database
- Maven

## ⚡ Recursos Modernos Utilizados
- Records (Java 14+)
- Text Blocks (Java 15+)
- Pattern Matching (Java 17+)
- Virtual Threads (Java 21)

## 🏃‍♂️ Como executar
1. **Pré-requisitos:** JDK 21 instalado
2. Clone o repositório
3. Execute: `./mvnw spring-boot:run`
4. Acesse: http://localhost:8080/health

## 📋 Endpoints
- GET /health - Status da aplicação (inclui versão Java)
- GET /info - Informações da aplicação
- GET /h2-console - Console do banco H2

## 🔧 Configuração
- Porta: 8080
- Banco: H2 em memória
- Profile: development

### Anotações


## 1. Gerenciamento de Transações (`@Transactional`)
O `@Transactional` gerencia o ciclo de vida das transações com o banco de dados de forma declarativa (commit e rollback automáticos em caso de `RuntimeException`).

### Parâmetros e Variações Importantes:
* **`readOnly = true`**: Utilizado em métodos de consulta (`SELECT`). Desativa o *dirty checking* (verificação de alterações) do Hibernate, otimizando a performance e aliviando o banco de dados.
* **`propagation` (Propagação)**: Define como o método lida com transações existentes.
  * `REQUIRED` *(Padrão)*: Usa a transação atual se houver; caso contrário, cria uma nova.
  * `REQUIRES_NEW`: Cria uma nova transação do zero, suspendendo a anterior temporariamente. Ideal para logs e auditorias que não devem ser perdidas se a operação principal falhar.
* **Níveis de Isolamento (`isolation`)**: Controlam a concorrência entre transações:
  * `READ_COMMITTED`: Padrão de mercado. Evita leituras sujas (*dirty reads*).
  * `REPEATABLE_READ`: Garante consistência nos dados lidos durante toda a transação.
  * `SERIALIZABLE`: O nível mais restritivo e seguro, porém o mais lento devido aos bloqueios rígidos de concorrência.

---

## 2. Padrão de Serviços (`impl`)
* **Uso de Interfaces + Pasta `impl`**: É uma convenção forte no ecossistema Java/Spring para separar o **contrato** (Interface na raiz do service) da **implementação** (Classe concreta dentro da pasta `impl`).
* **É obrigatório?** Não. O Spring consegue injetar classes de serviço diretamente (`@Service`) sem interfaces. No entanto, o padrão com `impl` é muito utilizado em projetos corporativos para desacoplamento e facilidade de testes.

---

## 3. Mapeamento e Serialização JSON

### Mapeamento DTO <-> Entity
* Feito manualmente via métodos de conversão (`toEntity` / `toDTO`) para garantir total segurança e controle sobre quais dados entram e saem da API, embora existam bibliotecas como MapStruct para automatizar em projetos maiores.

### Anotações de Relacionamento e Serialização
* **`@JsonIgnore`**: Utilizado em relacionamentos bidirecionais (ex: `@OneToMany`) para evitar loops infinitos (referência circular) e estouro de memória quando a biblioteca Jackson for serializar o objeto Java para JSON na resposta HTTP.

---

## 4. Auditoria e Datas Automáticas (`@PrePersist`)
* **`@PrePersist`**: Método anotado na entidade que executa uma lógica automaticamente logo antes de o registro ser inserido no banco pela primeira vez (ótimo para definir `dataCadastro = LocalDateTime.now()`).
* *Alternativa nativa:* Anotações como `@CreationTimestamp` do Hibernate reduzem ainda mais o boilerplate para esse mesmo cenário.
