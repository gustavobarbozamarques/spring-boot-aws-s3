FROM gradle:6.9-jdk11 as builder
COPY --chown=gradle:gradle . /home/src
WORKDIR /home/src
RUN gradle build

FROM openjdk:11-jre-slim
ENV VERSION=0.0.1-SNAPSHOT
ENV ACCESS_KEY=
ENV SECRET_KEY=
ENV REGION=
ENV BUCKET=
CMD mkdir /app
COPY --from=builder /home/src/build/libs/spring-boot-aws-s3-${VERSION}.jar /app.jar
EXPOSE 8080
ENTRYPOINT java -jar /app.jar