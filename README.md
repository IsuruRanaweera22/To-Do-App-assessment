# Todo App

This is a full-stack Todo application built with a React frontend and a Spring Boot backend. The application is containerized using Docker and can be easily run using Docker Compose.

## Prerequisites

- Docker
- Docker Compose

## Getting Started

### Clone the Repository
bash
git clone https://github.com/your-username/todo-app.git
cd todo-app


### Build and Run the Application

1. **Build and Start Services**: Use Docker Compose to build and start the services.

   ```bash
   docker-compose up --build
   ```

2. **Access the Application**:
   - **Frontend**: Open [http://localhost:3000](http://localhost:3000) in your browser.
   - **Backend**: The API will be available at [http://localhost:8081](http://localhost:8081).

### Project Structure

- **todoAppBackend**: Contains the Spring Boot backend application.
- **todo-app-frontend**: Contains the React frontend application.
- **docker-compose.yml**: Defines the services and how they interact.

### Environment Variables

- **Backend**:
  - `SPRING_DATASOURCE_URL`: Database connection URL.
  - `SPRING_DATASOURCE_USERNAME`: Database username.
  - `SPRING_DATASOURCE_PASSWORD`: Database password.

- **Frontend**:
  - `REACT_APP_API_URL`: URL of the backend API.

### Database

The application uses MySQL as the database. The database is also containerized and managed by Docker Compose.

### Running Tests

#### Backend Tests

To run the backend tests, navigate to the `todoAppBackend` directory and use Maven:
```bash
cd todoAppBackend
mvn test
```


#### Frontend Tests

To run the frontend tests, navigate to the `todo-app-frontend` directory and use npm:
```bash
cd todo-app-frontend
npm test
```

### Stopping the Application

To stop the application, run:
```bash
docker-compose down
```

### Troubleshooting

- Ensure Docker and Docker Compose are installed and running.
- Check if the ports 3000, 8081, and 3306 are available on your machine.
