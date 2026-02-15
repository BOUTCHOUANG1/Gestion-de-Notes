# ManageNotes — Gestion des Notes Universitaires

Grade management system for francophone universities following the LMD standard.  
Spring Boot 3.5.3 backend + React 18 frontend with the Echelon design system.

## Tech Stack

| Layer | Stack |
|-------|-------|
| Backend | Java 21, Spring Boot 3.5.3, Spring Security + JWT, Spring Data JPA |
| Frontend | React 18.3, TypeScript 5.8, Vite 7, Ant Design 5.26, TailwindCSS 4 |
| Database | PostgreSQL (database: `managerNotes`) |
| State | Redux Toolkit + RTK Query |
| Design | Echelon — Inter font, pill buttons, #667EEA accent |

## Grading System (LMD /20)

Grades are on a /20 scale with weighted formula:

```
Total = CC × 30% + TP × 20% + SN × 50%
```

| Score | Mention | Badge |
|-------|---------|-------|
| ≥ 16 | Très Bien | green |
| ≥ 14 | Bien | blue |
| ≥ 12 | Assez Bien | yellow |
| ≥ 10 | Passable | purple |
| < 10 | Échec | red |

## Quick Start

### Prerequisites

- Java 21+, Maven 3.9+
- Node.js 18+, npm 8+
- PostgreSQL

### 1. Database

```bash
createdb managerNotes
psql -U postgres managerNotes < db_seed_data.sql
```

### 2. Backend

```bash
cp .env.example .env
# Edit .env with your DB_PASSWORD and JWT_SECRET

cd API_GestionNotes/ManageNotes
mvn spring-boot:run
```

Runs on `http://localhost:3030`  
Swagger UI: `http://localhost:3030/swagger-ui.html`

### 3. Frontend

```bash
cd react
npm install
npm run dev
```

Runs on `http://localhost:5173`

## Default Logins

| Role | Username | Password |
|------|----------|----------|
| Admin | `admin` | `admin` |
| Teacher | `prof.smith` | `admin` |
| Student | `24a0001` | `admin` |

## Project Structure

```
API_GestionNotes/ManageNotes/     # Spring Boot backend
  src/main/java/.../
    controller/                   # REST controllers
    service/impl/                 # Business logic
    model/                        # JPA entities
    dto/                          # Request/Response DTOs
    repository/                   # Spring Data repos
    util/GradeCalculator.java     # CC/TP/SN formula

react/src/                        # React frontend
  features/
    auth/                         # Login, JWT handling
    teacher/                      # Teacher dashboard, grade entry
    admin/                        # Admin CRUD (departments, subjects, students)
    revendication/                # Grade claims
  components/
    EditableGradesTable.tsx        # Grade entry table (PV)
    GradeBadge.tsx                 # /20 scale mention badges
    Dashboard.tsx                  # Dynamic sidebar by teaching level
  store/api/                      # RTK Query (auto JWT injection)
  index.css                       # Echelon design tokens
```

## Features

- JWT auth with role-based access (Admin, Teacher, Student)
- Teacher dashboard: only sees levels they actually teach
- Dynamic sidebar generated from teacher's assigned subjects
- Grade entry with 0-20 validation
- Grade claims (revendication) workflow
- Admin CRUD for departments, subjects, students, teachers
- Echelon design system: light, minimal, Inter font, pill-shaped controls

## Environment Variables

```bash
# .env (backend)
DB_PASSWORD=your_postgres_password
JWT_SECRET=your_jwt_secret_at_least_32_characters_long
```

```bash
# react/.env.development (frontend, already configured)
VITE_API_BASE_URL=http://localhost:3030/api
```

## License

MIT
