#syntax=docker/dockerfile:1.4

########## BUILD (Maven + OpenJDK 21) ##########
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /build

# 1️⃣ Cache de dependências Maven
COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -U -Dmaven.repo.local=/root/.m2 dependency:go-offline || true

# 2️⃣ Copiar o arquivo OpenAPI separadamente (para forçar rebuild quando ele mudar)
ARG OPENAPI_SHA=dev
COPY src/main/resources/openapi/openapi.yaml ./src/main/resources/openapi/openapi.yaml
RUN echo "OPENAPI_SHA=${OPENAPI_SHA}"

# 3️⃣ Copiar o restante do código-fonte
COPY . .

# 4️⃣ Build sempre LIMPO e com geração das fontes OpenAPI
#     - clean: limpa target (garante que não há lixo de build anterior)
#     - compile + generate-sources: gera as fontes do OpenAPI
#     - test: roda unitários
#     - package: empacota o JAR final
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -U -Dmaven.repo.local=/root/.m2 clean compile generate-sources test package -DskipITs=true \
 && sh -c 'JAR=$(ls -1 target/*.jar | grep -vE "(sources|javadoc|original)" | head -n1) && mv "$JAR" target/app.jar'


########## EXTRACT LAYERS ##########
FROM eclipse-temurin:21-jre-jammy AS extract
WORKDIR /work

# Copia o JAR do estágio anterior
COPY --from=builder /build/target/app.jar /work/app.jar

# Extrai as camadas do JAR para otimizar o tempo de rebuild
RUN java -Djarmode=layertools -jar /work/app.jar extract --destination /work/extracted


########## RUNTIME (JRE 21) ##########
FROM eclipse-temurin:21-jre-jammy AS final
ARG UID=10001

# Usuário sem privilégios
RUN adduser --disabled-password --gecos "" \
    --home "/nonexistent" --shell "/sbin/nologin" \
    --no-create-home --uid "${UID}" appuser

USER appuser
WORKDIR /app

# Copia as camadas extraídas do JAR
COPY --from=extract /work/extracted/dependencies/           ./
COPY --from=extract /work/extracted/snapshot-dependencies/ ./
COPY --from=extract /work/extracted/spring-boot-loader/    ./
COPY --from=extract /work/extracted/application/           ./

# Perfil padrão
ENV SPRING_PROFILES_ACTIVE=prod

# Porta exposta
EXPOSE 8080

# Comando de inicialização
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher"]
