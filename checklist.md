# Midterm Requirements Checklist

## Architecture
- [x] Project uses layered architecture: Controller, Service, Repository.
- [x] Controllers contain request/response handling only.
- [x] Business logic lives in the Service layer.
- [x] Repositories are Spring Data JPA repositories.

## REST Controllers
- [x] `POST` endpoints create records.
- [x] `GET` endpoints retrieve records.
- [x] `PUT` endpoints update records.
- [x] `DELETE` endpoints delete records.

## Database
- [x] Application connects to H2 using Spring Data JPA/Hibernate.
- [x] H2 console is enabled for local inspection.
- [x] Schema is generated automatically for development.

## Entities And Relationships
- [x] `User` entity exists.
- [x] `Task` entity exists.
- [x] `User` has a one-to-many relationship with `Task`.
- [x] `Task` has a many-to-one relationship with `User`.

## CRUD Operations
- [x] Users: create user.
- [x] Users: get all users.
- [x] Users: get user by ID.
- [x] Users: update user.
- [x] Users: delete user.
- [x] Tasks: create task.
- [x] Tasks: get all tasks.
- [x] Tasks: get task by ID.
- [x] Tasks: update task.
- [x] Tasks: delete task.

## Validation
- [x] Request DTOs use validation annotations.
- [x] `@NotNull` is used where required.
- [x] `@Size` is used for bounded strings.
- [x] `@Email` is used for user email.
- [x] Invalid request bodies return HTTP 400 responses.
- [x] Invalid IDs do not crash the application.

## Exception Handling
- [x] Missing records return HTTP 404.
- [x] Invalid requests return appropriate error responses.
- [x] Application does not crash on invalid input.

## DTOs
- [x] APIs do not return JPA entities directly.
- [x] User request DTO exists.
- [x] User response DTO exists.
- [x] Task request DTO exists.
- [x] Task response DTO exists.

## Swagger
- [x] Swagger UI dependency is installed.
- [x] Swagger UI opens at `/swagger-ui.html`.
- [x] All endpoints are visible and testable.
- [x] Endpoints have short descriptions.

## Submission
- [x] Project builds successfully.
- [x] Tests pass.
- [x] Application starts successfully.
- [x] Project can be zipped for submission.
