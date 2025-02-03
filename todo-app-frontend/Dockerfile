# Use a Node.js image to build the application
FROM node:18 AS build
WORKDIR /app
COPY . .
RUN npm install
RUN npm run build

# Use a Nginx image to serve the application
FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"] 