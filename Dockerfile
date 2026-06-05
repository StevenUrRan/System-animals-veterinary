
FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /workspace

COPY mvnw .

COPY .mvn .mvn

COPY pom.xml .

RUN --mount=type=cache,target=/root/.m2 ./mvnw -B -DskipTests dependency:go-offline

COPY src src

RUN --mount=type=cache,target=/root/.m2 ./mvnw -B -DskipTests package

FROM eclipse-temurin:17-jre-jammy AS runtime


WORKDIR /app

RUN groupadd --system spring && useradd --system --gid spring --home-dir /app --shell /usr/sbin/nologin spring

COPY --chown=spring:spring --from=build /workspace/target/*.jar app.jar

ENV JAVA_OPTS=""

EXPOSE 8080

USER spring

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
