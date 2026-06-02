# Student Registration System

Professional full-stack **Student Registration System** aligned with your final project requirements: student CRUD, search, photo upload, login/registration, and **Admin** / **Guest** roles with a responsive Bootstrap UI.

## Admin interface

After login, a **sidebar layout** provides:

| Page | Admin | Guest |
|------|-------|-------|
| Dashboard | Yes | Yes |
| Students | Full CRUD | View + search |
| Add Student | Yes | — |
| Reports | Yes | — |
| Users | Yes | — |
| Profile | Yes | Yes |
| Settings | Yes | Yes |
| Help | Yes | Yes |

## Requirements coverage

| Requirement | Implementation |
|-------------|----------------|
| `student` table: id, name, address, phone, email, photo | JPA `Student` entity + photo file storage |
| Save / Update / Delete / Search / Display all | REST API + Angular UI |
| Login & Registration | `/api/auth/login`, `/api/auth/register` |
| Admin & Guest roles | `ADMIN` (full CRUD), `GUEST` (read + search) |
| Responsive design | Bootstrap 5 (mobile cards + desktop table) |
| Oracle database | `database/schema-oracle.sql` + `oracle` Spring profile |

## Tech stack

- **Backend:** Java 21, Spring Boot 3.4, Spring Security, JWT, JPA
- **Frontend:** Angular 19, Bootstrap 5, Bootstrap Icons
- **Database:** H2 (default, Oracle-compatible mode) or Oracle 11g+

## Quick start

### Prerequisites

- Java 21+
- Node.js 20+
- Maven (or use `backend/mvnw.cmd` after wrapper is restored)

### 1. Backend

```powershell
$env:JAVA_HOME="C:\Program Files\Eclipse Adoptium\jdk-17.0.15.6-hotspot"
cd backend
.\mvnw.cmd spring-boot:run
```

If port 8080 is busy:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--server.port=8082"
```

Then set the same port in `frontend/proxy.conf.json`.

API: `http://localhost:8080` (or your chosen port)  
Swagger: `http://localhost:8080/swagger-ui.html`  
H2 console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:studentdb`)

### 2. Frontend

```powershell
cd frontend
npm install
npm start
```

App: `http://localhost:4200` — API calls are proxied to the backend via `proxy.conf.json` (default target: `http://localhost:8082`).

If the backend runs on another port, edit `frontend/proxy.conf.json` and restart `npm start`.

### Demo accounts

| Role  | Username | Password  |
|-------|----------|-----------|
| Admin | `admin`  | `admin123` |
| Guest | `guest`  | `guest123` |

## Database (3 tables)

| Table | Purpose |
|-------|---------|
| `STUDENT` | Assignment data: id, name, address, phone, email, photo |
| `APP_USER` | Login / register with `ADMIN` or `GUEST` role |
| `USER_SETTINGS` | Per-user preferences (Settings page) |

## Live API (dynamic pages)

| Page | API |
|------|-----|
| Dashboard | `GET /api/dashboard/stats` |
| Students | `GET/POST/PUT/DELETE /api/students` |
| Reports | `GET /api/reports/summary` |
| Users | `GET /api/users` |
| Profile | `GET /api/auth/me` |
| Settings | `GET/PUT /api/settings/me` |
| Help | `GET /api/system/info` |

See `database/schema-oracle.sql`. Optional sample students: `database/seed-oracle.sql`.

## Oracle setup

1. Run `database/schema-oracle.sql` in Oracle SQL Developer.
2. (Optional) Run `database/seed-oracle.sql` for sample students.
3. Start backend with Oracle profile (creates demo users `admin` / `guest` if tables are empty):

```powershell
$env:DB_USERNAME="student_app"
$env:DB_PASSWORD="student_app"
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=oracle
```

## API overview

```http
POST   /api/auth/login
POST   /api/auth/register
GET    /api/students?q=searchTerm
GET    /api/students/{id}
POST   /api/students          (multipart: student JSON + photo)
PUT    /api/students/{id}     (Admin only)
DELETE /api/students/{id}     (Admin only)
GET    /api/photos/{filename}
```

## Project structure

```text
School_management/
├── backend/          # Spring Boot API
├── frontend/         # Angular SPA
├── database/         # Oracle DDL
└── README.md
```

## License

MIT
