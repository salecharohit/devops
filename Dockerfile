FROM openjdk:8-jre-alpine
RUN addgroup boot
RUN adduser -D -h /home/boot -s /bin/ash boot -G boot
USER boot
WORKDIR /home/boot
ADD target/devops**.jar devops.jar
EXPOSE 8080
CMD java -jar devops.jar