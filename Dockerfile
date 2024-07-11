FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /workspace/app

COPY ./mvnw mvnw
COPY ./.mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:17-jre-alpine

RUN addgroup -S tabletennis && adduser -S tabletennis -G tabletennis
USER tabletennis

VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","university.innopolis.tabletennis.tournamentmicroservice.TournamentMicroserviceApplication"]
