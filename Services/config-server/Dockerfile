FROM bellsoft/liberica-openjdk-alpine:21 AS builder

WORKDIR /home/app
ADD ./ /home/app/sritel
RUN chmod +x /home/app/sritel/mvnw
RUN cd sritel && ./mvnw -Dmaven.test.skip=true clean package


FROM bellsoft/liberica-openjre-alpine:21

WORKDIR /home/app
EXPOSE 8888
RUN apk --no-cache add curl
SHELL ["/bin/sh", "-c"]
COPY --from=builder /home/app/sritel/target/*.jar sritel.jar
CMD ["java", "-jar", "sritel.jar"]