# Chat History

> **User:** Add gitignore for java, springboot, typescript and react
>
> No spaces allowed
> No comments
> No todo

> **Assistant:** Okay, I'll generate a `.gitignore` for Java, Spring Boot, TypeScript, and React. I'll ignore common build artifacts and dependencies for each, like `target/`, `node_modules/`, and compiled files. First, I'll check for an existing `.gitignore`.

> **User:** Add environement variables to the gitignore

> **Assistant:** Of course. I'll add common environment variable files like `.env` and `application.properties` to the `.gitignore`. First, I need to read the existing file.

> **Assistant:** Okay, I'll add a section for environment variables to the existing `.gitignore`.

> **Assistant:** Okay, I'll remove the comments from the `.gitignore` file.

> **Assistant:** I've added the environment variable rules to the `.gitignore` file. Is there anything else?

> **Assistant:** I have updated the .gitignore file to include entries for environment variables.

> **User:** You are my AI-First Engineer. Follow strict AI-first development methodology.
>
> ====================
> SYSTEM CONTRACT
> ====================
> - Do NOT write any code until I approve the project plan.
> - First: Read my requirements.
> - Then: generate a detailed multi-step implementation plan.
> - Include architecture, components, APIs, data model usage, and full folder structure.
> - All tasks must be small, deterministic, and independently testable.
> - STOP after generating the plan. Wait for my approval.
> - When coding tasks begin, follow these rules:
>
> CODING RULES:
> 1. No placeholders or pseudo-code.
> 2. Real compilable code only.
> 3. Never invent features.
> 4. Never modify unrelated files.
> 5. Always stop after each task.
> 6. Validate assumptions and ask questions if unclear.
>
> ====================
> PROJECT REQUIREMENTS
> ====================
>
> Fucntional Requirements
> Specification Input
>
> The user must be able to enter the following:
>
> Functional requirements
>
> Non-functional requirements
>
> Data models
>
> API design
>
> Acceptance test scenarios
>
> The system must store this input as a structured SpecRequest object.
>
> Validation
> The system must validate that the required fields are present before triggering code generation.
>
> Invalid or missing fields should produce a clear error message.
>
> AI-Driven Code Generation
> The system must be able to call the Gemini API (or a mocked Gemini service) to generate:
>
> Backend code
>
> Frontend code
>
> Test code
>
> Suggested folder structure
>
> The system must display a status such as:
>
> RECEIVED
>
> GENERATING
>
> GENERATED
>
> FAILED
>
> Display Generated Output
> The system must display the generated artifacts in a readable format:
>
> Separate tabs or sections for backend, frontend, tests, etc.
>
> Syntax highlighting (optional but ideal)
>
> Test Execution
> The user must be able to request the system to run the generated tests.
>
> The system must show test output, including:
>
> Passed tests
>
> Failed tests
>
> Error messages
>
> Feedback & Iteration
> The user must be able to request regeneration of code based on:
>
> Updated requirements
>
> Feedback text
>
> Modified acceptance criteria
>
>
> NON-FUNCTIONAL REQUIREMENTS
> Maintainability
> The system must be structured using clean architecture principles to ensure maintainability.
>
> Modules for specification handling, AI generation, test execution, and GitHub integration must be logically separated.
>
> The codebase must follow consistent naming conventions and project structure.
>
> Testability
> All backend modules must be testable independently (unit tests + integration tests).
>
> Generated code should also be testable through automated or semi-automated test execution.
>
> Mock services (e.g., MockGeminiService) must allow testing without external API calls.
>
> Stateless Backend API
> The backend API must be stateless:
>
> No user session storage
>
> No in-memory user state (besides request-scoped processing)
>
> All state transitions (e.g., GENERATING → GENERATED) should be trackable through IDs or timestamps if needed.
>
> Responsive Frontend
> The frontend must remain responsive during long AI-generation tasks.
>
> The system must show progress states (e.g., “Generating…”, “Processing…”, “Completing…”).
>
> No blocking UI operations are allowed; long-running tasks should use async requests.
>
> Toggle AI Mode
> The system must support switching between AI Mocking or AI real agent
>
> AI providers must be abstracted behind a common interface (e.g., AIService).
>
>
> Code Readability & Documentation
> All critical components must include meaningful documentation:
>
> API endpoints
>
> Data models
>
> Services (AI, GitHub integration, test executor)
>
> Inline comments should be added for non-obvious logic.
>
> A README must describe how to run, test, and extend the system.
>
> Reliability
> The system must handle AI API timeouts, retries, or rate limitations gracefully.
>
> Failures must be clearly surfaced to the user via status messages.
>
> Security
> tokens, key and sensitive informations must never be logged or stored in plaintext.
>
> Environment variables must be used for sensitive keys.
>
> AI provider API keys must be stored securely.
>
> Data models
>
> SpecRequest { systemDescription: string functionalRequirements: string[] nonFunctionalRequirements: string[] dataModel: string apiDesign: string acceptanceCriteria: string[, isRealModel: boolean] }
>
> GeneratedOutput { backendCode: string frontendCode: string testCode: string folderStructure: string }
>
>
> API Contract:
> POST /api/generate → UUID
> Starts generation.
>
> GET /api/status/{id} → StatusResponse
>
> GET /api/result/{id} -> GeneratedOutput
>
> 1- System Architecture:
> Java 17
> Springboot v3+
> node
> react
> H2
> Layered architecture:
> controller
> service
> repo
> model
> config
>
> No business logic inside the controller
>
> 2- AI Genereate Service
>
> - Use async non-blocking api calls using Spring WebClient
>
> - both MockAISevice RealAIService must implement the AIServiceInterface
>
> - The AIServiceInterface must has String generate(SpecRequest) throws AICustomException
>
> - The RealAIService must call an api defined in application.properties
>
> - The selection of which AIServiceInterface to call inside the generate function is based on the isRealModel flag, if true then choose RealAIService else then MockAISevice, and this selection should be handled in seperate function AIServiceInterface getAIService(SpecRequest) using the factory pattern.
>
> 3- Async Processing For the generate function
>
> - Must be annotated by spring @Async
> - Status must be stored in DB as:
>
> PENDING
>
> RUNNING
>
> COMPLETE
>
> FAILED
>
> Result retrieval must be:
>
> GET /status/{id}
>
> GET /result/{id}
>
> 4- Database Constraints
>
> - h2 database for testing and postgress for production to be controlled by profiles
> - use jpa and hibernate
>
> - Must include createdAt, updatedAt.
> Must persist:
> - SpecRequest (raw user input)
> - Job execution status
>
> 5- Api Constrains
> - API must be stateless.
> - All requests and responses must be JSON.
> API must follow REST conventions.
> - use javax validation on domain entities, all that fields are required and (notblank or null) systemDescription, functionalRequirements, nonFunctionalRequirements, dataModel, apiDesign, acceptanceCriteria
> - use http methods as defined exactly in the API Contract requirements section.
> - no authentication or authorization.
>
> 6- Frontend Architecture
> Use React (with Vite or CRA).
>
> Use TypeScript (optional).
>
> UI constraints:
>
> Must include a toggle for useRealModel.
>
> Must allow user to enter all fields:
>
> functional requirements
>
> non-functional requirements
>
> data models
>
> API design
>
> acceptance criteria
>
> Must poll backend /status every 5 seconds.
>
> Must display loading indicators during async processing.
>
> Frontend must not freeze during code generation.
>
> 7- commuication with the backend:
> - the frontend must use fetch or async/await
> - the frontend must display the messages from the backend specially the error messages.
> - should handle network failures, timeouts, server errors.
>
> 8- Project Structure Constraints
>
> backend:
>
> src/main/java/com/app/
> controller/
> service/
> repo/
> model/
> dto/
> config/
>
> frontend:
> src/
> components/
> api/
> pages/
> models/
> hooks/
>
> 9- Testing constrains
>
> the test should covers both happy and unhappy senarios for each of the following:
> - RealAIService
> - MockAIService
> - The Validation logic
> - Handle the agent exception
> - controller endpoints
> - service layer
>
> use:
> Junit5
> DON'T USE MOCKITO
>
> 10- Security Constraints
> - All secrets, keys and sensive information must be ignored (refrenced in gitignore)
> - All secrets, keys and sensive information must be stored in environemt variables
> - All secrets, keys and sensive information must not logged or sent over to the frontend
> - All secrets, keys and sensive information must be deleted from in-memory datastructure as fast as possible by filling it by zeros and stored temporary only in mutable datastructure like byte[]
> - No authentication or authorization required for MVP
>
> 11- Code Quality Constraints
> - no duplicate
> - no comments for what only of why only if necessary.
> - no todo
> - no fixme
> - only meaningful names for vairables, methods, classes, packages.
> - follows SOLID princibles.
> - must formate the backend using google formatter and prettier for fronend.
>
> 12- Deployment Constraints (optional)
>
> Must run fully via:
>
> mvn spring-boot:run
> npm run dev
>
>
> Must be container-ready:
>
> Provide a Dockerfile for backend
>
> Provide a Dockerfile for frontend
>
> GeneratedOutput
> ====================
> NEXT ACTION
> ====================
> Generate the PROJECT PLAN ONLY.
> Do NOT generate code.
> Stop after producing the plan.
> Await my approval.

> **Assistant:** Of course. Here is a detailed project plan based on your requirements.
>
> ========================================
> PROJECT PLAN: AI-First Code Generation System
> ========================================
>
> **1. High-Level Summary**
>
> The project is to build a full-stack application that allows a user to input software specifications, which are then used to generate code via an AI service (Gemini). The system will feature a stateless Spring Boot backend for processing and a responsive React frontend for user interaction. The architecture will be clean, modular, and testable, with a clear separation between the frontend and backend.
>
> **2. System Architecture**
>
> *   **Backend:**
>
>     *   **Framework:** Spring Boot v3+ with Java 17.
>     *   **Architecture:** Layered (Controller -> Service -> Repository).
>     *   **Database:** H2 (for `dev`/`test` profiles) and PostgreSQL (for `prod` profile) managed by Spring Data JPA/Hibernate.
>     *   **AI Integration:** Asynchronous, non-blocking API calls to the AI service using `WebClient`. An `AIService` interface will abstract the real and mock implementations, chosen via a factory pattern.
>     *   **Async Processing:** Spring's `@Async` will be used for long-running code generation tasks, with job status tracked in the database.
>
> *   **Frontend:**
>     *   **Framework:** React with Vite and TypeScript.
>     *   **Architecture:** Component-based (`pages`, `components`, `hooks`).
>     *   **Communication:** Asynchronous API calls to the backend using the `fetch` API.
>     *   **State Management:** React hooks for managing form state, polling for job status, and displaying results.
>
> *   **Project Structure:**
>
>     ```
>     /
>     ├── backend/
>     │   └── src/main/java/com/app/
>     │       ├── controller/
>     │       │   └── GenerationController.java
>     │       ├── service/
>     │       │   ├── aifactory/
>     │       │   │   ├── AIService.java
>     │       │   │   ├── AIServiceFactory.java
>     │       │   │   ├── MockAIService.java
>     │       │   │   └── RealAIService.java
>     │       │   └── GenerationService.java
>     │       ├── repo/
>     │       │   ├── GenerationJobRepository.java
>     │       │   └── GeneratedOutputRepository.java
>     │       ├── model/
>     │       │   ├── GenerationJob.java
>     │       │   ├── GeneratedOutput.java
>     │       │   └── JobStatus.java
>     │       ├── dto/
>     │       │   ├── SpecRequest.java
>     │       │   ├── JobSubmissionResponse.java
>     │       │   └── StatusResponse.java
>     │       └── config/
>     │           ├── AsyncConfig.java
>     │           └── WebClientConfig.java
>     └── frontend/
>         └── src/
>             ├── components/
>             │   ├── SpecForm.tsx
>             │   └── ResultsDisplay.tsx
>             ├── api/
>             │   └── generationAPI.ts
>             ├── pages/
>             │   └── HomePage.tsx
>             ├── models/
>             │   └── types.ts
>             └── hooks/
>                 └── useGenerationStatus.ts
>     ```
>
> **3. Data Models & API Contract**
>
> *   **Database Entities:**
>     *   `GenerationJob`: Persists the user's request and its current status.
>         *   `id` (UUID, Primary Key)
>         *   `specRequest` (Embedded `SpecRequest` object)
>         *   `status` (Enum: `PENDING`, `RUNNING`, `COMPLETE`, `FAILED`)
>         *   `createdAt`, `updatedAt` (Timestamps)
>     *   `GeneratedOutput`: Stores the result of a successful job.
>         *   `id` (UUID, Primary Key, Foreign Key to `GenerationJob`)
>         *   `backendCode`, `frontendCode`, `testCode`, `folderStructure` (TEXT fields)
>
> *   **DTOs (Data Transfer Objects):**
>     *   `SpecRequest`: User input with validation (`@NotBlank`, `@NotEmpty`).
>         *   `systemDescription`, `functionalRequirements`, `nonFunctionalRequirements`, `dataModel`, `apiDesign`, `acceptanceCriteria`, `isRealModel`.
>     *   `JobSubmissionResponse`: `{ "jobId": "uuid-string" }`
>     *   `StatusResponse`: `{ "status": "CURRENT_STATUS" }`
>
> *   **API Endpoints:**
>     *   `POST /api/generate`: Accepts `SpecRequest`, returns `JobSubmissionResponse`.
>     *   `GET /api/status/{id}`: Returns `StatusResponse`.
>     *   `GET /api/result/{id}`: Returns `GeneratedOutput`.
>
> **4. Backend Implementation Plan**
>
> *   **Task 1: Project & Entity Setup:**
>     *   Initialize a Spring Boot project with `web`, `data-jpa`, `validation`, `webflux`, `h2`, and `postgresql` starters.
>     *   Create the `GenerationJob`, `GeneratedOutput`, and `JobStatus` (Enum) entities. Define `SpecRequest` as an `@Embeddable` class.
>
> *   **Task 2: Repository and DTOs:**
>     *   Create `GenerationJobRepository` and `GeneratedOutputRepository` interfaces extending `JpaRepository`.
>     *   Create the `SpecRequest`, `JobSubmissionResponse`, and `StatusResponse` DTOs with validation annotations on `SpecRequest`.
>
> *   **Task 3: AI Service Abstraction:**
>     *   Define the `AIService` interface with a `generate` method.
>     *   Implement `MockAIService` to return a hardcoded, valid `GeneratedOutput` JSON string.
>     *   Implement `RealAIService` using `WebClient` to call the external AI endpoint. API key and URL will be injected from environment variables.
>     *   Create the `AIServiceFactory` to select the service implementation based on the `isRealModel` flag.
>
> *   **Task 4: Async Generation Service:**
>     *   Create `GenerationService`.
>     *   Implement `startGenerationJob(SpecRequest)`: Creates and saves a `GenerationJob` with `PENDING` status, then triggers the async processing method, and returns the new `jobId`.
>     *   Implement `@Async processGeneration(UUID jobId)`:
>         1.  Fetches the job, updates status to `RUNNING`.
>         2.  Uses the factory to get the correct `AIService`.
>         3.  Calls the service, handling exceptions by setting status to `FAILED`.
>         4.  On success, parses the response, saves it as a `GeneratedOutput` entity, and updates job status to `COMPLETE`.
>
> *   **Task 5: API Controller:**
>     *   Create `GenerationController` with three endpoints (`/generate`, `/status/{id}`, `/result/{id}`) that delegate calls to the `GenerationService`. The `/generate` endpoint will validate the incoming `SpecRequest` DTO.
>
> *   **Task 6: Configuration & Testing:**
>     *   Enable async support via `@EnableAsync`.
>     *   Configure `dev` and `prod` profiles in `application.properties` to switch between H2 and PostgreSQL.
>     *   Write JUnit 5 tests for the controller, service layer, and validation logic without using Mockito. Use `TestRestTemplate` for endpoint testing and WireMock to simulate the external AI API for testing `RealAIService`.
>
> **5. Frontend Implementation Plan**
>
> *   **Task 1: Project Setup:**
>     *   Initialize a React + TypeScript project using Vite.
>     *   Define data types/interfaces in `src/models/types.ts` to match the backend DTOs.
>
> *   **Task 2: API Client:**
>     *   Create an API service module (`src/api/generationAPI.ts`) with functions to call the three backend endpoints, including robust error handling.
>
> *   **Task 3: Input Form Component:**
>     *   Create a `SpecForm` component with text areas for all specification inputs and a toggle for `useRealModel`. It will manage form state and handle the submission event.
>
> *   **Task 4: Status Polling Hook:**
>     *   Create a custom hook `useGenerationStatus(jobId)` that polls the `/api/status/{id}` endpoint every 5 seconds. It will start polling when a `jobId` is provided and stop when the status is `COMPLETE` or `FAILED`.
>
> *   **Task 5: Main Page & State Orchestration:**
>     *   Create the `HomePage` to assemble the UI.
>     *   It will manage the overall state: `jobId`, `status`, `error`, and the final `GeneratedOutput`.
>     *   On form submission, it will call the API, set the `jobId`, and display a loading indicator.
>     *   It will use the `useGenerationStatus` hook to track progress.
>     *   When the job is complete, it will fetch and pass the results to the `ResultsDisplay` component.
>
> *   **Task 6: Results Display Component:**
>     *   Create a `ResultsDisplay` component that renders the `GeneratedOutput`. It will use tabs to separate backend code, frontend code, tests, and the folder structure.
>
> **6. Security & Finalization**
>
> *   **Task 1: Security:**
>     *   Update `.gitignore` to include `application.properties`, `.env*`, and other sensitive files.
>     *   Ensure all secrets are loaded from environment variables in Spring Boot and are never logged or exposed in API responses.
> *   **Task 2: Documentation:**
>     *   Create a `README.md` with setup, build, and run instructions for both backend and frontend. Document the API endpoints and environment variable requirements.
>
> ========================================
> NEXT ACTION: Await approval of this plan before proceeding with implementation.
> ========================================

> **User:** In Data models and API contract
> use A time-stamped UUID for better lookup in the b-tree index in the GenerationJob, GenereatedOutput.
>
> in Task 5: Main Page & State Orchestration:
> the frontend should format the code that displayed inside the frontend and backend tabs
>
> Update the Plan and send it back to me for approval, and wait my approval for it before proceeding with implementation

> **Assistant:** Of course. I have updated the project plan with your requested changes. Here is the revised version for your approval.
>
> ========================================
> PROJECT PLAN: AI-First Code Generation System (Revised)
> ========================================
>
> **1. High-Level Summary**
>
> The project is to build a full-stack application that allows a user to input software specifications, which are then used to generate code via an AI service (Gemini). The system will feature a stateless Spring Boot backend for processing and a responsive React frontend for user interaction. The architecture will be clean, modular, and testable, with a clear separation between the frontend and backend.
>
> **2. System Architecture**
>
> *   **Backend:**
>     *   **Framework:** Spring Boot v3+ with Java 17.
>     *   **Architecture:** Layered (Controller -> Service -> Repository).
>     *   **Database:** H2 (for `dev`/`test` profiles) and PostgreSQL (for `prod` profile) managed by Spring Data JPA/Hibernate.
>     *   **AI Integration:** Asynchronous, non-blocking API calls to the AI service using `WebClient`. An `AIService` interface will abstract the real and mock implementations, chosen via a factory pattern.
>     *   **Async Processing:** Spring's `@Async` will be used for long-running code generation tasks, with job status tracked in the database.
>
> *   **Frontend:**
>     *   **Framework:** React with Vite and TypeScript.
>     *   **Architecture:** Component-based (`pages`, `components`, `hooks`).
>     *   **Communication:** Asynchronous API calls to the backend using the `fetch` API.
>     *   **State Management:** React hooks for managing form state, polling for job status, and displaying results.
>
> *   **Project Structure:**
>
>     ```
>     /
>     ├── backend/
>     │   └── src/main/java/com/app/
>     │       ├── controller/
>     │       │   └── GenerationController.java
>     │       ├── service/
>     │       │   ├── aifactory/
>     │       │   │   ├── AIService.java
>     │       │   │   ├── AIServiceFactory.java
>     │       │   │   ├── MockAIService.java
>     │       │   │   └── RealAIService.java
>     │       │   └── GenerationService.java
>     │       ├── repo/
>     │       │   ├── GenerationJobRepository.java
>     │       │   └── GeneratedOutputRepository.java
>     │       ├── model/
>     │       │   ├── GenerationJob.java
>     │       │   ├── GeneratedOutput.java
>     │       │   └── JobStatus.java
>     │       ├── dto/
>     │       │   ├── SpecRequest.java
>     │       │   ├── JobSubmissionResponse.java
>     │       │   └── StatusResponse.java
>     │       └── config/
>     │           ├── AsyncConfig.java
>     │           └── WebClientConfig.java
>     └── frontend/
>         └── src/
>             ├── components/
>             │   ├── SpecForm.tsx
>             │   └── ResultsDisplay.tsx
>             ├── api/
>             │   └── generationAPI.ts
>             ├── pages/
>             │   └── HomePage.tsx
>             ├── models/
>             │   └── types.ts
>             └── hooks/
>                 └── useGenerationStatus.ts
>     ```
>
> **3. Data Models & API Contract**
>
> *   **Database Entities:**
>     *   `GenerationJob`: Persists the user's request and its current status.
>         *   `id` (**Time-stamped UUID**, Primary Key): Generated to be sequential to improve B-tree index performance.
>         *   `specRequest` (Embedded `SpecRequest` object)
>         *   `status` (Enum: `PENDING`, `RUNNING`, `COMPLETE`, `FAILED`)
>         *   `createdAt`, `updatedAt` (Timestamps)
>     *   `GeneratedOutput`: Stores the result of a successful job.
>         *   `id` (**Time-stamped UUID**, Primary Key, Foreign Key to `GenerationJob`)
>         *   `backendCode`, `frontendCode`, `testCode`, `folderStructure` (TEXT fields)
>
> *   **DTOs (Data Transfer Objects):**
>     *   `SpecRequest`: User input with validation (`@NotBlank`, `@NotEmpty`).
>         *   `systemDescription`, `functionalRequirements`, `nonFunctionalRequirements`, `dataModel`, `apiDesign`, `acceptanceCriteria`, `isRealModel`.
>     *   `JobSubmissionResponse`: `{ "jobId": "uuid-string" }`
>     *   `StatusResponse`: `{ "status": "CURRENT_STATUS" }`
>
> *   **API Endpoints:**
>     *   `POST /api/generate`: Accepts `SpecRequest`, returns `JobSubmissionResponse`.
>     *   `GET /api/status/{id}`: Returns `StatusResponse`.
>     *   `GET /api/result/{id}`: Returns `GeneratedOutput`.
>
> **4. Backend Implementation Plan**
>
> *   **Task 1: Project & Entity Setup:**
>     *   Initialize a Spring Boot project with `web`, `data-jpa`, `validation`, `webflux`, `h2`, and `postgresql` starters.
>     *   Create the `GenerationJob`, `GeneratedOutput`, and `JobStatus` (Enum) entities. Configure a time-stamped UUID generator for the `id` fields. Define `SpecRequest` as an `@Embeddable` class.
>
> *   **Task 2: Repository and DTOs:**
>     *   Create `GenerationJobRepository` and `GeneratedOutputRepository` interfaces extending `JpaRepository`.
>     *   Create the `SpecRequest`, `JobSubmissionResponse`, and `StatusResponse` DTOs with validation annotations on `SpecRequest`.
>
> *   **Task 3: AI Service Abstraction:**
>     *   Define the `AIService` interface with a `generate` method.
>     *   Implement `MockAIService` to return a hardcoded, valid `GeneratedOutput` JSON string.
>     *   Implement `RealAIService` using `WebClient` to call the external AI endpoint. API key and URL will be injected from environment variables.
>     *   Create the `AIServiceFactory` to select the service implementation based on the `isRealModel` flag.
>
> *   **Task 4: Async Generation Service:**
>     *   Create `GenerationService`.
>     *   Implement `startGenerationJob(SpecRequest)`: Creates and saves a `GenerationJob` with `PENDING` status, then triggers the async processing method, and returns the new `jobId`.
>     *   Implement `@Async processGeneration(UUID jobId)`:
>         1.  Fetches the job, updates status to `RUNNING`.
>         2.  Uses the factory to get the correct `AIService`.
>         3.  Calls the service, handling exceptions by setting status to `FAILED`.
>         4.  On success, parses the response, saves it as a `GeneratedOutput` entity, and updates job status to `COMPLETE`.
>
> *   **Task 5: API Controller:**
>     *   Create `GenerationController` with three endpoints (`/generate`, `/status/{id}`, `/result/{id}`) that delegate calls to the `GenerationService`. The `/generate` endpoint will validate the incoming `SpecRequest` DTO.
>
> *   **Task 6: Configuration & Testing:**
>     *   Enable async support via `@EnableAsync`.
>     *   Configure `dev` and `prod` profiles in `application.properties` to switch between H2 and PostgreSQL.
>     *   Write JUnit 5 tests for the controller, service layer, and validation logic without using Mockito. Use `TestRestTemplate` for endpoint testing and WireMock to simulate the external AI API for testing `RealAIService`.
>
> **5. Frontend Implementation Plan**
>
> *   **Task 1: Project Setup:**
>     *   Initialize a React + TypeScript project using Vite.
>     *   Define data types/interfaces in `src/models/types.ts` to match the backend DTOs.
>
> *   **Task 2: API Client:**
>     *   Create an API service module (`src/api/generationAPI.ts`) with functions to call the three backend endpoints, including robust error handling.
>
> *   **Task 3: Input Form Component:**
>     *   Create a `SpecForm` component with text areas for all specification inputs and a toggle for `useRealModel`. It will manage form state and handle the submission event.
>
> *   **Task 4: Status Polling Hook:**
>     *   Create a custom hook `useGenerationStatus(jobId)` that polls the `/api/status/{id}` endpoint every 5 seconds. It will start polling when a `jobId` is provided and stop when the status is `COMPLETE` or `FAILED`.
>
> *   **Task 5: Main Page & State Orchestration:**
>     *   Create the `HomePage` to assemble the UI.
>     *   It will manage the overall state: `jobId`, `status`, `error`, and the final `GeneratedOutput`.
>     *   On form submission, it will call the API, set the `jobId`, and display a loading indicator.
>     *   It will use the `useGenerationStatus` hook to track progress.
>     *   When the job is complete, it will fetch and pass the results to the `ResultsDisplay` component, which will handle the final rendering and formatting.
>
> *   **Task 6: Results Display Component:**
>     *   Create a `ResultsDisplay` component that renders the `GeneratedOutput`.
>     *   It will use tabs to separate backend code, frontend code, tests, and the folder structure.
>     *   **It will integrate a syntax highlighting library (e.g., `highlight.js` or `Prism.js`) to format and color the displayed code for better readability.**
>
> **6. Security & Finalization**
>
> *   **Task 1: Security:**
>     *   Update `.gitignore` to include `application.properties`, `.env*`, and other sensitive files.
>     *   Ensure all secrets are loaded from environment variables in Spring Boot and are never logged or exposed in API responses.
> *   **Task 2: Documentation:**
>     *   Create a `README.md` with setup, build, and run instructions for both backend and frontend. Document the API endpoints and environment variable requirements.
>
> ========================================
> NEXT ACTION: Await approval of this plan before proceeding with implementation.
> ========================================

> **User:** Task 4 (Updated) — Async Generation Service Enhancements
>
> Add robust resilience for the external AI call:
>
> Requirements:
>
> Timeout
>
> AI call must time out after 15 seconds (configurable via application.yml).
>
> Retry logic
>
> Retry up to 10 times.
>
> Retry only on:
>
> 429 (rate limit)
>
> 500/503 (server errors)
>
> timeouts
>
> I/O network failures
>
> Do NOT retry on:
>
> 400 (bad request)
>
> 401/403 (auth issues)
>
> logical errors from Gemini
>
> Exponential Backoff
>
> Start with 5 seconds
>
> Backoff formula: delay * 2^attempt
>
> Max delay capped at 60 seconds.
>
> Failure handling
>
> If all retries fail → mark job as FAILED
>
> Save the error message for debugging.
>
> Logging
>
> Log attempt count and reason only at DEBUG level.
>
> No sensitive data in logs.
>
> Acceptance Criteria:
>
> Code must compile.
>
> A failing AI call is retried as specified.
>
> After max retries, job transitions to FAILED.
>
> No blocking of API threads (async only).
>
> Unit tests include:
>
> retry success scenario
>
> retry exhaustion scenario
>
> non-retryable error scenario
>
> Task 5 (Updated) — Controller & Input Validation
>
> Add strict validation rules for incoming SpecRequest.
>
> Requirements:
>
> Apply max length constraints:
>
> systemDescription → max 2000 chars
>
> dataModel → max 5000 chars
>
> The controller must not call service layer if validation fails.
>
> Acceptance Criteria:
>
> All invalid inputs return 400 with descriptive errors.
>
> Valid inputs pass to service layer unchanged.
>
> Tests must cover:
>
> too-long text fields
>
> oversized arrays
>
> empty arrays
>
> mixed invalid fields
>
> apiDesign → max 5000 chars
>
> functionalRequirements → each ≤ 1000 chars, list size ≤ 50
>
> nonFunctionalRequirements → each ≤ 1000 chars, list size ≤ 50
>
> acceptanceCriteria → each ≤ 1000 chars, list size ≤ 50
>
> Add Spring Validation:
>
> @Size(max = X)
>
> @NotBlank / @NotEmpty
>
> Custom validator for list size.
>
>
> Update the plan, send it back and wait for my approval before any proceeding with the implementation

<br>

# Final Approved Project Plan

========================================
PROJECT PLAN: AI-First Code Generation System (Revised v2)
========================================

**1. High-Level Summary**

The project is to build a full-stack application that allows a user to input software specifications, which are then used to generate code via an AI service (Gemini). The system will feature a stateless, resilient Spring Boot backend for processing and a responsive React frontend for user interaction. The architecture will be clean, modular, and testable, with a clear separation between the frontend and backend.

**2. System Architecture**

*   **Backend:**
    *   **Framework:** Spring Boot v3+ with Java 17.
    *   **Architecture:** Layered (Controller -> Service -> Repository).
    *   **Database:** H2 (for `dev`/`test` profiles) and PostgreSQL (for `prod` profile) managed by Spring Data JPA/Hibernate.
    *   **AI Integration:** Asynchronous, non-blocking, and resilient API calls to the AI service using `WebClient` with advanced retry and timeout configurations. An `AIService` interface will abstract the real and mock implementations, chosen via a factory pattern.
    *   **Async Processing:** Spring's `@Async` will be used for long-running code generation tasks, with job status tracked in the database.

*   **Frontend:**
    *   **Framework:** React with Vite and TypeScript.
    *   **Architecture:** Component-based (`pages`, `components`, `hooks`).
    *   **Communication:** Asynchronous API calls to the backend using the `fetch` API.
    *   **State Management:** React hooks for managing form state, polling for job status, and displaying results.

*   **Project Structure:** (No changes from previous plan)
    ```
    /
    ├── backend/
    │   └── src/main/java/com/app/
    │       ├── controller/
    │       ├── service/
    │       ├── repo/
    │       ├── model/
    │       ├── dto/
    │       └── config/
    └── frontend/
        └── src/
            ├── components/
            ├── api/
            ├── pages/
            ├── models/
            └── hooks/
    ```

**3. Data Models & API Contract**

*   **Database Entities:**
    *   `GenerationJob`: Persists the user's request and its current status.
        *   `id` (**Time-stamped UUID**, Primary Key).
        *   `specRequest` (Embedded `SpecRequest` object).
        *   `status` (Enum: `PENDING`, `RUNNING`, `COMPLETE`, `FAILED`).
        *   `errorMessage` (String, nullable): Stores the final error message if the job fails.
        *   `createdAt`, `updatedAt` (Timestamps).
    *   `GeneratedOutput`: Stores the result of a successful job.
        *   `id` (**Time-stamped UUID**, Primary Key, Foreign Key to `GenerationJob`).
        *   `backendCode`, `frontendCode`, `testCode`, `folderStructure` (TEXT fields).

*   **DTOs (Data Transfer Objects):**
    *   `SpecRequest`: User input with strict validation.
        *   `systemDescription`: `@NotBlank`, `@Size(max = 2000)`
        *   `functionalRequirements`: `@NotEmpty`, `@Size(max = 50)`, with each element `@NotBlank @Size(max = 1000)`
        *   `nonFunctionalRequirements`: `@NotEmpty`, `@Size(max = 50)`, with each element `@NotBlank @Size(max = 1000)`
        *   `dataModel`: `@NotBlank`, `@Size(max = 5000)`
        *   `apiDesign`: `@NotBlank`, `@Size(max = 5000)`
        *   `acceptanceCriteria`: `@NotEmpty`, `@Size(max = 50)`, with each element `@NotBlank @Size(max = 1000)`
        *   `isRealModel`: `boolean`
    *   `JobSubmissionResponse`: `{ "jobId": "uuid-string" }`
    *   `StatusResponse`: `{ "status": "CURRENT_STATUS", "errorMessage": "..." }`

*   **API Endpoints:**
    *   `POST /api/generate`: Accepts `SpecRequest`, returns `JobSubmissionResponse`.
    *   `GET /api/status/{id}`: Returns `StatusResponse`.
    *   `GET /api/result/{id}`: Returns `GeneratedOutput`.

**4. Backend Implementation Plan**

*   **Task 1: Project & Entity Setup:** (No changes)
*   **Task 2: Repository and DTOs:** (No changes)
*   **Task 3: AI Service Abstraction:** (No changes)

*   **Task 4 (Updated): Async Generation Service with Resilience:**
    *   **Objective:** Implement a resilient `RealAIService` that can withstand transient failures when calling the external AI API.
    *   **Implementation:**
        1.  **Configure `WebClient`:** Create a `WebClient` bean with a request timeout of 15 seconds (configurable in `application.yml`).
        2.  **Implement Retry Logic:** Use `reactor-extra`'s `RetryBackoffSpec` to configure the retry mechanism on the `WebClient` call.
            *   **Max Retries:** 10 attempts.
            *   **Retryable Conditions:** Only retry on `WebClientResponseException` with status codes 429, 500, 503, or on exceptions like `ReadTimeoutException` and `IOException`.
            *   **Non-Retryable Conditions:** Do not retry on 4xx client errors (except 429) or any other unexpected exceptions.
        3.  **Implement Exponential Backoff:**
            *   **Initial Delay:** 5 seconds.
            *   **Formula:** `delay * 2^attempt`.
            *   **Max Delay:** Capped at 60 seconds.
        4.  **Failure Handling:** In the `processGeneration` method, if the `RealAIService` call ultimately fails after all retries, catch the final exception. Update the `GenerationJob` status to `FAILED` and persist the exception's message in the `errorMessage` field.
        5.  **Logging:** Add `DEBUG` level logging to record retry attempts, including the attempt number and the reason for the retry (e.g., "Retrying request, attempt 3/10 due to 503 Service Unavailable"). Ensure no sensitive data is logged.
    *   **Acceptance Criteria:**
        *   Unit tests verify the retry logic for a successful outcome on a subsequent attempt.
        *   Unit tests verify that the job status is set to `FAILED` after all retry attempts are exhausted.
        *   Unit tests verify that non-retryable errors (e.g., a 400 Bad Request) fail immediately without retrying.
        *   The main API thread is not blocked during the AI call.

*   **Task 5 (Updated): API Controller & Input Validation:**
    *   **Objective:** Harden the `GenerationController` to reject invalid `SpecRequest` payloads before they reach the service layer.
    *   **Implementation:**
        1.  **Apply Annotations:** Add `jakarta.validation.constraints` annotations (`@Size`, `@NotBlank`, `@NotEmpty`) directly to the fields of the `SpecRequest` DTO as specified in the "Data Models" section.
        2.  **Validate List Elements:** Use a custom validator or a library extension if necessary to enforce the `@Size(max=1000)` constraint on each `String` element within the `functionalRequirements`, `nonFunctionalRequirements`, and `acceptanceCriteria` lists.
        3.  **Enable Validation:** Annotate the `SpecRequest` parameter in the `GenerationController`'s `/api/generate` endpoint with `@Valid`. Spring Boot will automatically trigger validation and return a 400 Bad Request with a descriptive error body if validation fails.
    *   **Acceptance Criteria:**
        *   API requests with fields exceeding their `max` length constraints are rejected with a 400 status.
        *   API requests with empty strings/lists for required fields are rejected with a 400 status.
        *   API requests with oversized lists (e.g., more than 50 functional requirements) are rejected with a 400 status.
        *   A request with a mix of valid and invalid fields is rejected.
        *   A fully valid request is successfully passed to the `GenerationService`.

*   **Task 6: Configuration & Testing:** (No changes)

**5. Frontend Implementation Plan**

*   (No changes from previous plan)

**6. Security & Finalization**

*   (No changes from previous plan)

========================================
NEXT ACTION: Await approval of this plan before proceeding with implementation.
========================================
