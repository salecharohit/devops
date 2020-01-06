FROM openjdk:8-jre-alpine
ARG FILE_NAME
RUN addgroup boot
RUN adduser -D -h /home/boot -s /bin/ash boot -G boot
USER boot
WORKDIR /home/boot
ADD target/"$FILE_NAME".jar devops.jar
EXPOSE 80
CMD java -jar devops.jar