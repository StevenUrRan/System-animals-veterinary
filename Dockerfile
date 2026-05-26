# syntax=docker/dockerfile:1.7

# La linea anterior habilita la sintaxis moderna de Dockerfile para usar cache mounts en RUN.

# Define una etapa de compilacion con JDK 17, la misma version declarada en pom.xml.
FROM eclipse-temurin:17-jdk-jammy AS build

# Establece el directorio de trabajo donde se copiara y compilara el proyecto.
WORKDIR /workspace

# Copia el wrapper de Maven para compilar sin depender de Maven instalado en la maquina host.
COPY mvnw .

# Copia la configuracion del Maven Wrapper necesaria para descargar la version correcta de Maven.
COPY .mvn .mvn

# Copia el archivo de dependencias antes del codigo fuente para aprovechar mejor la cache de Docker.
COPY pom.xml .

# Descarga dependencias y plugins en una capa cacheable para acelerar builds posteriores.
RUN --mount=type=cache,target=/root/.m2 ./mvnw -B -DskipTests dependency:go-offline

# Copia el codigo fuente despues de cachear dependencias para que cambios de codigo no invaliden todo.
COPY src src

# Compila la aplicacion y genera el JAR ejecutable sin correr pruebas dentro del build de imagen.
RUN --mount=type=cache,target=/root/.m2 ./mvnw -B -DskipTests package

# Define una etapa final solo con JRE 17 para reducir el peso y la superficie de ataque.
FROM eclipse-temurin:17-jre-jammy AS runtime

# Establece el directorio donde vivira la aplicacion dentro del contenedor.
WORKDIR /app

# Crea un grupo y usuario de sistema para ejecutar la aplicacion sin privilegios de root.
RUN groupadd --system spring && useradd --system --gid spring --home-dir /app --shell /usr/sbin/nologin spring

# Copia el JAR generado desde la etapa de compilacion y asigna propiedad al usuario no privilegiado.
COPY --chown=spring:spring --from=build /workspace/target/*.jar app.jar

# Define opciones por defecto para la JVM y permite sobrescribirlas desde Docker Compose o docker run.
ENV JAVA_OPTS=""

# Documenta que la aplicacion Spring Boot escucha en el puerto 8080 dentro del contenedor.
EXPOSE 8080

# Cambia al usuario no privilegiado antes de arrancar la aplicacion.
USER spring

# Ejecuta el JAR usando sh para permitir expandir JAVA_OPTS en tiempo de ejecucion.
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
