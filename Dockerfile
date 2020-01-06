FROM openjdk:8-jre-alpine
RUN addgroup boot
RUN adduser -D -h /home/boot -s /bin/ash boot -G boot
USER boot
WORKDIR /home/boot
ADD target/$file_name devops.jar
EXPOSE 80
CMD java -jar devops.jar