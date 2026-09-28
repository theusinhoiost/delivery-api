# ==========================================================
# 🐳 Dockerfile - Delivery Tech API
# ==========================================================
# 1. Imagem base: Java 21 JRE oficial (Alpine = ultra leve e segura)
FROM eclipse-temurin:21-jre-alpine

# 2. Pasta de trabalho dentro do container
WORKDIR /app

# 3. Copia o executável gerado pelo Maven para dentro da imagem
COPY target/delivery-api-0.0.1-SNAPSHOT.jar app.jar

# 4. Declara a porta onde o Spring Boot escuta
EXPOSE 8080

# 5. Comando que inicializa a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
