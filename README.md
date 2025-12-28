🧠 Smart Site Task Manager – Backend
📌 Project Overview

Smart Site Task Manager ek intelligent task management backend hai jo task create hote hi automatic classification, priority detection aur suggested actions generate karta hai.

Example:
Input Task:

“Schedule urgent meeting with team today about budget allocation”

System Automatically:

Category → scheduling

Priority → high

Suggested Actions → Block calendar, Send invite, Prepare agenda

🛠 Tech Stack

Backend: Spring Boot (Java)

Database: PostgreSQL (Supabase compatible)

ORM: Hibernate / JPA

API Docs: Swagger / OpenAPI

Deployment: Render

Build Tool: Maven

Java Version: 17

🌍 Live Backend URL (Render)
https://smart-task-manager-backend-5.onrender.com


Swagger UI:

https://smart-task-manager-backend-5.onrender.com/swagger-ui.html

📡 API Endpoints
➕ Create Task (Auto Classification)

POST /api/tasks

Request:

{
  "title": "Urgent meeting with client",
  "description": "Schedule a meeting today to discuss invoice payment",
  "assignedTo": "Ranjit",
  "dueDate": "2025-12-25T10:30:00"
}


Response:

{
  "category": "scheduling",
  "priority": "high",
  "status": "pending",
  "suggestedActions": [
    "Block calendar",
    "Send invite",
    "Prepare agenda",
    "Set reminder"
  ]
}

📄 Get All Tasks (Pagination)

GET /api/tasks?page=0&size=10

🔍 Get Task By ID (with history)

GET /api/tasks/{id}

✏️ Update Task

PATCH /api/tasks/{id}

❌ Delete Task

DELETE /api/tasks/{id}

🗄 Database Schema
🧾 tasks table
id uuid (PK)
title text
description text
category text
priority text
status text
assigned_to text
due_date timestamp
extracted_entities jsonb
suggested_actions jsonb
created_at timestamp
updated_at timestamp

🕘 task_history table
id uuid (PK)
task_id uuid (FK)
action text
old_value jsonb
new_value jsonb
changed_by text
changed_at timestamp

🧠 Auto-Classification Logic
Category Detection

scheduling → meeting, schedule, call

finance → invoice, budget, payment

technical → bug, fix, error

safety → safety, hazard

general → default

Priority Detection

high → urgent, asap, today

medium → important, soon

low → default

⚙️ How to Run Locally
1️⃣ Clone Repository
git clone backend-repo-url https://github.com/Ranjitrockt/smart-task-manager-backend-.
cd backend

2️⃣ Update Database Config

application.properties

spring.datasource.url=jdbc:postgresql://localhost:5432/smartTask
spring.datasource.username=postgres
spring.datasource.password=

3️⃣ Run Application
mvn spring-boot:run

🧪 Testing

Unit tests written for classification logic

Pagination and filtering tested via Swagger

🚀 Architecture Decisions

Service layer for business logic

Repository pattern for DB access

Auto-classification at service level

PostgreSQL JSONB used for flexible fields

🔮 What I’d Improve (Given More Time)

Advanced analytics dashboard

Rate limiting & API key security

👨‍💻 Author

Ranjit Kumar
Java Backend Developer | Spring Boot | PostgreSQL | Flutter
