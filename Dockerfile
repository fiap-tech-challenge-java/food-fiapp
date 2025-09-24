#syntax=docker/dockerfile:1.4
########## BUILD (Maven + OpenJDK 21) ##########
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /build

#Cache de dependências
COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -U -Dmaven.repo.local=/root/.m2 dependency:go-offline || true

#Código-fonte
COPY . .

#Build completo com geração de fontes OpenAPI + testes unitários
#Integração roda no serviço de testes do docker-compose (com DB/MinIO disponíveis)
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -Dmaven.repo.local=/root/.m2 clean compile generate-sources test package -DskipITs=true \
 && sh -c 'JAR=$(ls -1 target/*.jar | grep -vE "(sources|javadoc|original)" | head -n1) && mv "$JAR" target/app.jar'

########## EXTRACT LAYERS ##########
FROM eclipse-temurin:21-jre-jammy AS extract
WORKDIR /work
COPY --from=builder /build/target/app.jar /work/app.jar
RUN java -Djarmode=layertools -jar /work/app.jar extract --destination /work/extracted

########## RUNTIME (JRE 21) ##########
FROM eclipse-temurin:21-jre-jammy AS final
ARG UID=10001

#Usuário sem privilégios
RUN adduser --disabled-password --gecos "" --home "/nonexistent" --shell "/sbin/nologin" --no-create-home --uid "${UID}" appuser
USER appuser
WORKDIR /app

#Camadas extraídas do JAR
COPY --from=extract /work/extracted/dependencies/           ./
COPY --from=extract /work/extracted/snapshot-dependencies/ ./
COPY --from=extract /work/extracted/spring-boot-loader/    ./
COPY --from=extract /work/extracted/application/           ./

#Parâmetros de execução padrão para produção
ENV SPRING_PROFILES_ACTIVE=prod
# ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+UseG1GC -Dfile.encoding=UTF-8"

# Define variáveis de ambiente do MinIO
ENV MINIO_ENDPOINT=http://minio:9000 \
    MINIO_ACCESS_KEY=minioadmin \
    MINIO_SECRET_KEY=minioadmin \
    MINIO_BUCKET=menu-items

EXPOSE 8080
ENTRYPOINT ["sh","-c","java org.springframework.boot.loader.launch.JarLauncher"]
