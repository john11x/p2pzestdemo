# ZestP2P onboarding guide

This project now uses a Spring Boot backend as the active API layer, with the React frontend as the web client.

## Current stack

- Frontend: React + TypeScript + Vite
- Active backend: Java 17 + Spring Boot
- Main backend project: [springbackend](springbackend)
- Legacy serverless backend: removed; the repo is now Spring-only

## Repository map

- [src](src) → frontend application
- [springbackend/src/main/java](springbackend/src/main/java) → Java backend code
- [springbackend/src/main/resources](springbackend/src/main/resources) → Spring configuration and app properties
- [docs](docs) → project notes and documentation

## Backend architecture

The backend is layered in the standard Spring pattern:

- controllers → HTTP endpoints
- services → business logic
- repositories → persistence access
- models → JPA entities and enums

The current Spring API is under:

- [springbackend/src/main/java/zest/springbackend/controller](springbackend/src/main/java/zest/springbackend/controller)
- [springbackend/src/main/java/zest/springbackend/service](springbackend/src/main/java/zest/springbackend/service)
- [springbackend/src/main/java/zest/springbackend/repository](springbackend/src/main/java/zest/springbackend/repository)

## Frontend integration

The frontend now points to the Spring API base URL through the environment variable:

- `VITE_API_BASE_URL`

Default value when not set:

- `http://localhost:8081/api`

The shared client lives in [src/lib/data-client.ts](src/lib/data-client.ts).

## Local startup

### 1. Start the Spring backend

From the project root:

```bash
cd springbackend
./mvnw spring-boot:run
```

### 2. Start the frontend

From the project root:

```bash
pnpm install
pnpm dev
```

## Important project rule

Do not add new legacy serverless logic to this repo. The current active backend is Spring Boot, and the repo should stay aligned with that architecture.

## Working conventions

- Keep backend logic in Spring service/repository layers.
- Keep HTTP contracts in controllers and DTOs.
- Keep frontend API calls centralized in the shared client.
- Remove unused legacy serverless artifacts when cleaning up work.
