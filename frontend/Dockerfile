FROM node:alpine as builder
WORKDIR /app
ARG STAGE
# Copy project files into the docker image
COPY ./frontend/  /app
RUN cd /app && npm run build:$STAGE

FROM nginx:alpine
RUN rm -rf /usr/share/nginx/html/*
COPY --from=builder /app/dist/frontend /usr/share/nginx/html
COPY ./nginx-$STAGE.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]