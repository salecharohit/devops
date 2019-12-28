FROM node:alpine as builder
WORKDIR /app
# Copy project files into the docker image
COPY .  /app
RUN cd /app && npm run build

FROM nginx:alpine
RUN rm -rf /usr/share/nginx/html/*
COPY --from=builder /app/dist/frontend /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]