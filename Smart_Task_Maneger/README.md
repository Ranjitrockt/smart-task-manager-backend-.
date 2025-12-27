# Smart Task Manager

A smart task management system that automatically classifies and prioritizes tasks based on their content.

## Features

*   **Task Management**: Create, read, update, and delete tasks.
*   **Auto-Classification**: Automatically categorizes tasks (Scheduling, Finance, Technical, Safety, General) based on keywords.
*   **Auto-Prioritization**: Automatically assigns priority (High, Medium, Low) based on urgency keywords.
*   **Task History**: Tracks all changes made to a task.
*   **Filtering & Pagination**: Efficiently list tasks with filters and pagination.

## Tech Stack

*   **Backend**: Java, Spring Boot
*   **Database**: PostgreSQL
*   **Build Tool**: Maven

## Setup Instructions

1.  **Prerequisites**:
    *   Java 17 or higher
    *   Maven
    *   PostgreSQL

2.  **Database Setup**:
    *   Create a PostgreSQL database named `smartTask`.
    *   Update `src/main/resources/application.properties` with your database credentials.

3.  **Run the Application**:
    ```bash
    mvn spring-boot:run
    ```

## API Documentation

### Create Task
`POST /api/tasks`

Request:
```json
{
  "title": "Fix critical bug",
  "description": "Urgent fix needed for production issue"
}
```

Response:
```json
{
  "id": "...",
  "title": "Fix critical bug",
  "category": "technical",
  "priority": "high",
  "status": "pending",
  ...
}
```

### Get All Tasks
`GET /api/tasks?page=0&size=10&status=pending`

### Get Task Details
`GET /api/tasks/{id}`

### Update Task
`PUT /api/tasks/{id}`

### Delete Task
`DELETE /api/tasks/{id}`

### Get Task History
`GET /api/tasks/{id}/history`
