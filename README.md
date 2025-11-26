# AI-First Code Generation System

This project is a full-stack application designed to demonstrate an AI-first development methodology. It allows users to input detailed software specifications, which are then sent to an AI service to generate code for backend, frontend, and tests.

## Features

-   **Specification Input**: A simple frontend form for capturing functional/non-functional requirements, data models, API design, and acceptance criteria.
-   **AI-Driven Code Generation**: A backend service that takes the specifications and calls an AI agent to generate code.
-   **Mock & Real AI Services**: Supports toggling between a mock AI service (for testing and development) and a real AI service.
-   **Asynchronous Processing**: Backend jobs run asynchronously, allowing the frontend to remain responsive.
-   **Status Polling**: The frontend polls the backend every 5 seconds to get real-time status updates on generation jobs (`PENDING`, `RUNNING`, `COMPLETE`, `FAILED`).
-   **Resilient Backend**: The backend service includes robust retry logic with exponential backoff for handling transient AI API failures.
-   **Tabbed Results Display**: Generated code is displayed in a clean, tabbed interface with syntax highlighting.
-   **Clean Architecture**: The project follows clean architecture principles with a layered backend (Controller, Service, Repository) and a component-based React frontend.

## Tech Stack

-   **Backend**:
    -   Java 17
    -   Spring Boot 3+
    -   Spring Data JPA / Hibernate
    -   Spring WebFlux (`WebClient`) for reactive API calls
    -   MapStruct for DTO-entity mapping
    -   H2 (for dev/test), PostgreSQL (for prod)
    -   Maven
-   **Frontend**:
    -   React (with Vite)
    -   TypeScript
    -   Playwright for E2E testing
    -   `react-syntax-highlighter` for code display
-   **Database**:
    -   H2 (for development and testing)
    -   PostgreSQL (intended for production)

## Project Structure

```
/
├── backend/      # Spring Boot Application
│   └── src/main/java/com/app/
│       ├── config/
│       ├── controller/
│       ├── dto/
│       ├── mapper/
│       ├── model/
│       ├── repo/
│       └── service/
└── frontend/     # React Application
    ├── e2e/        # Playwright E2E tests
    └── src/
        ├── api/
        ├── components/
        ├── hooks/
        ├── models/
        └── pages/
```

## How to Run the Application

### Prerequisites

-   Java 17+
-   Maven 3.8+
-   Node.js 18+
-   npm or yarn

### 1. Run the Backend

Navigate to the backend directory and use Maven to run the Spring Boot application.

```sh
# From the project root directory
cd backend

# Run the application
mvn spring-boot:run
```

The backend server will start on `http://localhost:8080`.

### 2. Run the Frontend

In a separate terminal, navigate to the frontend directory, install dependencies, and start the development server.

```sh
# From the project root directory
cd frontend

# Install dependencies (only required for the first time)
npm install

# Start the development server
npm run dev
```

The frontend application will be available at `http://localhost:5173`. Open this URL in your web browser to use the application.

## How to Run End-to-End (E2E) Tests

The E2E tests use Playwright to simulate user interaction from the frontend to the backend.

### Prerequisites

-   Both the backend and frontend servers must be running (follow the steps above).

### Run the Tests

```sh
# From the project root directory
cd frontend

# Execute the Playwright tests
npm run test:e2e

# To view the HTML report of the test results
npx playwright show-report
```
