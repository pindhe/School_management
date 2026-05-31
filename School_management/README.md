# School Management System (SMS)

A full‑stack School Management System built with **Java 17 + Spring Boot 3** (REST API),
**MySQL / H2** (database) and **Angular 21 + Angular Material** (web client).

It implements role‑based access control (RBAC) for **Admin**, **Teacher**, **Student** and
**Parent**, with JWT authentication, academic configuration, attendance, grading, timetables
and announcements.

---

## Architecture

```
SMS/
├── backend/     Spring Boot REST API (Java 17, Maven wrapper)
└── frontend/    Angular 21 single-page application (Angular Material)
```

- **Backend**: layered architecture — `domain` (JPA entities/enums) → `repository` →
  `service` → `controller`, plus `security` (JWT), `config` and `exception` packages.
- **Frontend**: standalone components, signals, lazy‑loaded feature routes, functional
  HTTP interceptors (JWT + global error handling) and route guards (auth + role).
- **Auth**: stateless JWT. The token is issued on login and sent as `Authorization: Bearer`.

---

## Prerequisites

| Tool | Version used | Notes |
|------|--------------|-------|
| JDK  | 17 (Adoptium) | `JAVA_HOME` must point to a JDK 17 |
| Node | 22.x | ships with npm 10 |
| MySQL | 8.x | **optional** — only for the `mysql` profile |

Maven is **not** required — the project uses the Maven wrapper (`mvnw` / `mvnw.cmd`).

---

## Running the backend

```bash
cd backend

# Windows (PowerShell)
$env:JAVA_HOME="C:\Program Files\Eclipse Adoptium\jdk-17.0.15.6-hotspot"
.\mvnw.cmd spring-boot:run

# macOS / Linux
./mvnw spring-boot:run
```

The API starts on **http://localhost:8081** (changed from 8080 to avoid a local port clash;
override with `SERVER_PORT`).

### Database profiles

- **`dev` (default)** — in‑memory **H2**, auto‑created and seeded on every start.
  No database installation needed. H2 console: `http://localhost:8081/h2-console`
  (JDBC URL `jdbc:h2:mem:smsdb`, user `sa`, empty password).
- **`mysql`** — persistent MySQL. Run with:

  ```bash
  ./mvnw spring-boot:run -Dspring-boot.run.profiles=mysql
  ```

  Configure via env vars (defaults shown): `DB_HOST=localhost`, `DB_PORT=3306`,
  `DB_NAME=sms_db`, `DB_USERNAME=root`, `DB_PASSWORD=root`. The schema is created
  automatically (`createDatabaseIfNotExist=true`, `ddl-auto=update`).

### API documentation

Swagger UI: **http://localhost:8081/swagger-ui.html**
OpenAPI spec: `http://localhost:8081/v3/api-docs`

### Seeded accounts

On first start the app seeds roles, an admin and a small demo dataset
(one academic year, three subjects, a class, a teacher, a student and a parent):

| Role    | Username  | Password      |
|---------|-----------|---------------|
| Admin   | `admin`   | `Admin@123`   |
| Teacher | `teacher` | `Teacher@123` |
| Student | `student` | `Student@123` |
| Parent  | `parent`  | `Parent@123`  |

New users created without an explicit password default to `Welcome@123`.

> Override the admin credentials and JWT secret in production via the environment
> variables `APP_JWT_SECRET`, `app.seed.admin-username`, `app.seed.admin-password`.

---

## Running the frontend

```bash
cd frontend
npm install        # first time only
npm start          # ng serve -> http://localhost:4200
```

The client expects the API at `http://localhost:8081/api` (see
`src/environments/environment.ts`). CORS is pre‑configured on the backend for
`http://localhost:4200`.

Production build:

```bash
npm run build      # outputs to frontend/dist/frontend
```

---

## Feature overview by role

| Area | Admin | Teacher | Student | Parent |
|------|:-----:|:-------:|:-------:|:------:|
| Dashboard | ✓ | ✓ | ✓ | ✓ |
| User management (CRUD, link parent, enroll) | ✓ | | | |
| Academic setup (years, subjects, classes, assignments) | ✓ | | | |
| Mark attendance | ✓ | ✓ | | |
| View attendance | ✓ | ✓ | own | children |
| Record grades | ✓ | ✓ | | |
| View grades | ✓ | ✓ | own | children |
| Timetable | ✓ | ✓ (own) | class | class |
| Announcements (publish) | ✓ | ✓ | | |
| Announcements (view) | ✓ | ✓ | ✓ | ✓ |
| Profile + password change | ✓ | ✓ | ✓ | ✓ |

---

## Key REST endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/auth/login` | Authenticate, returns JWT |
| GET/PUT | `/api/auth/me` | Current profile / update |
| POST | `/api/auth/change-password` | Change own password |
| CRUD | `/api/users/teachers\|students\|parents` | User management (Admin) |
| POST | `/api/users/link-parent` | Link a parent to a student |
| CRUD | `/api/academic/years\|subjects\|classes` | Academic configuration |
| POST | `/api/academic/assignments` | Assign teacher → class/subject |
| POST | `/api/academic/enroll` | Enroll student into class |
| POST/GET | `/api/academic/timetables` | Timetable management/view |
| POST | `/api/attendance/mark` | Mark daily attendance |
| GET | `/api/attendance/student/{id}` `/me` | View attendance |
| POST | `/api/grades` | Record a grade |
| GET | `/api/grades/student/{id}` `/me` | View grades |
| GET/POST | `/api/announcements` `/feed` | Announcements |

All endpoints except `/api/auth/**`, Swagger and the H2 console require a valid JWT,
and most are further restricted by role via `@PreAuthorize`.

---

## Tech stack

**Backend:** Spring Boot 3.5, Spring Web, Spring Data JPA (Hibernate), Spring Security,
JJWT, Bean Validation, springdoc‑openapi, Lombok, HikariCP.

**Frontend:** Angular 21, Angular Material (Material 3), RxJS, TypeScript, SCSS.
