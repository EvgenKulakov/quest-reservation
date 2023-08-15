FROM bellsoft/liberica-runtime-container:jre-17-musl
LABEL authors="Evgeniy"
WORKDIR /app
VOLUME /tmp
EXPOSE 8080
COPY target/quest-reservation-0.0.1-SNAPSHOT.jar app.jar
CMD ["java","-jar","app.jar"]