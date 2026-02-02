# ManageNotes - University Grade Management System

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18.3-61DAFB.svg)](https://reactjs.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.8-3178C6.svg)](https://www.typescriptlang.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17.5-blue.svg)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A comprehensive university grade management system with Spring Boot backend and React + TypeScript frontend, providing secure role-based access for administrators, teachers, and students to manage academic records, grades, and institutional data.

## 🚀 Features

### Core Functionality
- **Multi-role Authentication**: JWT-based authentication for Admin, Teacher, and Student roles
- **Grade Management**: Complete CRUD operations for student grades with validation
- **Academic Structure**: Department, subject, and semester management
- **Grade Claims**: Student-initiated grade dispute system with approval workflow
- **Reporting**: Comprehensive academic reports and transcripts
- **Grading Windows**: Time-controlled grade entry periods

### Role-Based Access Control
- **Admin**: Full system access, user management, department/subject creation
- **Teacher**: Grade entry, subject management (one subject per level), student grade viewing
- **Student**: Grade viewing, grade claim submission, transcript access

### Advanced Features
- **Real-time Validation**: Grade entry validation with grading window controls
- **Teacher Assignment Control**: One subject per teacher per academic level constraint
- **Audit Trail**: Complete tracking of grade changes and user actions
- **Bulk Operations**: Batch updates for semesters and departments
- **Data Integrity**: Foreign key constraint handling with cascade operations
- **Modern Frontend**: React 18 with TypeScript, RTK Query for state management
- **E2E Testing**: Playwright test suite for authentication and navigation flows
- **API Documentation**: Comprehensive REST API documentation + Swagger UI

## 🏗️ Architecture

### Technology Stack

**Backend:**
- **Framework**: Spring Boot 3.5.3
- **Security**: Spring Security 6 + JWT
- **Database**: PostgreSQL 17.5 with Spring Data JPA
- **Documentation**: SpringDoc OpenAPI 3 (Swagger UI)
- **Build Tool**: Maven 3.9+
- **Java Version**: OpenJDK 21

**Frontend:**
- **Framework**: React 18.3 with TypeScript 5.8
- **Build Tool**: Vite 7.0
- **State Management**: Redux Toolkit 2.8.2 + RTK Query
- **UI Library**: Ant Design 5.26
- **Styling**: TailwindCSS 4.1 + SASS 1.89
- **Routing**: React Router v7
- **Testing**: Playwright (E2E)

### Database Schema
```
Users (id, username, password, role, email, first_name, last_name)
├── Students (id, matricule, level, cycle, speciality)
├── Departments (id, name, creation_date)
├── Subjects (id, name, code, credits, level, cycle, department_id, id_teacher)
│   └── CONSTRAINT uk_teacher_level UNIQUE (id_teacher, level)
├── Semesters (id, name, start_date, end_date, active)
├── Grades (id, student_id, subject_id, semester_id, value, type, period_type)
├── GradeClaims (id, grade_id, student_id, requested_score, status, cause)
└── GradingWindows (id, name, start_date, end_date, period_type, semester_id)
```

### Frontend Architecture (RTK Query)
```
react/src/
├── store/
│   ├── api/
│   │   ├── baseQuery.ts          # JWT token injection + error handling
│   │   └── apiSlice.ts           # Central API configuration
│   └── index.ts                   # Redux store with RTK Query middleware
├── features/
│   ├── auth/
│   │   ├── api/authApi.ts        # Login, register, getProfile endpoints
│   │   └── slice.ts              # Auth state management
│   └── students/
│       └── api/studentsApi.ts    # Student management endpoints
├── components/
│   └── PrivateRoute.tsx          # Protected route wrapper
└── tests/
    └── e2e/                       # Playwright E2E tests
        ├── auth.spec.ts
        └── dashboard.spec.ts
```

## 🛠️ Installation & Setup

### Prerequisites
- **Backend**: Java 21+, PostgreSQL 17.5+, Maven 3.9+
- **Frontend**: Node.js 18+, npm 8+
- **Tools**: Git

---

### Backend Setup

#### 1. Install PostgreSQL
```bash
# Ubuntu/Debian
sudo apt update && sudo apt install postgresql postgresql-contrib

# macOS
brew install postgresql

# Windows
# Download from https://www.postgresql.org/download/windows/
```

#### 2. Create Database
```sql
CREATE DATABASE managerNotes;
CREATE USER postgres WITH PASSWORD 'nathan';
GRANT ALL PRIVILEGES ON DATABASE managerNotes TO postgres;
```

#### 3. Import Initial Schema
```bash
cd API_GestionNotes/ManageNotes
psql -h localhost -U postgres -d managerNotes -f src/main/resources/managerNotes.sql
```

#### 4. Configure Application
```bash
# Edit src/main/resources/application.properties if needed
# Default configuration:
# - Database: localhost:5432/managerNotes
# - Server Port: 3030
# - JWT Secret: Configured via environment variable
```

#### 5. Build & Run Backend
```bash
cd API_GestionNotes/ManageNotes
mvn clean install
mvn spring-boot:run
```

**Backend will start at:** `http://localhost:3030`  
**Swagger UI:** `http://localhost:3030/swagger-ui.html`  
**API Docs:** `http://localhost:3030/api-docs`

---

### Frontend Setup

#### 1. Install Dependencies
```bash
cd react
npm install
```

#### 2. Configure Environment (Optional)
```bash
# .env.development (already configured)
VITE_API_BASE_URL=http://localhost:3030/api

# .env.production (edit for production)
VITE_API_BASE_URL=https://your-production-api.com/api
```

#### 3. Run Development Server
```bash
npm run dev
```

**Frontend will start at:** `http://localhost:5173`

#### 4. Build for Production
```bash
npm run build
# Output: react/dist/
```

---

### Running Tests

**Backend Tests:**
```bash
cd API_GestionNotes/ManageNotes
mvn test
```

**Frontend E2E Tests (Playwright):**
```bash
cd react
npm test                # Run all E2E tests
npm run test:ui         # Run with Playwright UI
npm run test:report     # View test report
```

**E2E Test Coverage:**
- ✅ Login with valid credentials
- ✅ Login with invalid credentials
- ✅ Unauthenticated redirect to login
- ✅ Logout functionality
- ✅ Authentication persistence across reloads
- ✅ Dashboard navigation
- ✅ Responsive layout testing

---

## 🔐 Authentication & Security

### Default Credentials

**Admin:**
```json
{
  "username": "admin",
  "password": "admin",
  "role": "ADMIN"
}
```
**Note**: The actual password in the database is `admin`, not `admin123` as originally documented.

**Teacher:**
```json
{
  "username": "prof.johnson",
  "password": "duchelle",
  "role": "TEACHER"
}
```

**Student:**
```json
{
  "username": "STU2024001",
  "password": "nathan",
  "role": "STUDENT"
}
```

### JWT Token Usage

**Login (cURL):**
```bash
curl -X POST http://localhost:3030/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}'

# Response:
# {
#   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
#   "id": 1,
#   "username": "admin",
#   "role": "ADMIN"
# }
```

**Authenticated Request:**
```bash
curl -X GET http://localhost:3030/api/me \
  -H "Authorization: Bearer <your-jwt-token>"
```

### Frontend Authentication Flow

**RTK Query Automatic Token Injection:**
```typescript
// Token automatically injected in all API calls
const { data: profile } = useGetProfileQuery();

// Under the hood:
// GET /api/me
// Headers: { Authorization: "Bearer <token>" }
```

**Security Features:**
- ✅ JWT tokens with 24-hour expiration
- ✅ Role-based endpoint protection via `@PreAuthorize`
- ✅ Automatic token injection in all API requests
- ✅ Secure token storage (localStorage)
- ✅ Environment-based API URL configuration
- ✅ Type-safe error handling

---

## 📚 API Documentation

**Full API Documentation:** [API_DOCUMENTATION.md](./API_DOCUMENTATION.md)

### Quick Reference

**Authentication Endpoints:**
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/auth/login` | None | User login |
| POST | `/api/auth/admin/register` | Admin | Register new user |
| POST | `/api/auth/password` | Required | Change password |
| POST | `/api/auth/logout` | Required | User logout |

**Profile Endpoints:**
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/api/me` | Required | Get current user profile |
| GET | `/api/auth/admin/profile` | Admin | Get admin profile |

**Student Management:**
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/api/admin/students` | Admin | List all students |

**Frontend Hooks (RTK Query):**
```typescript
// Authentication
const [login, { isLoading }] = useLoginMutation();
const [register] = useRegisterMutation();
const { data: profile } = useGetProfileQuery();

// Students
const { data: students } = useGetStudentsQuery();
```

---

## 🔄 Recent Changes (RTK Query Migration)

### Phase 1-4: RTK Query Migration ✅

**Migration Summary:**
- Migrated from Axios + createAsyncThunk to RTK Query
- **Net code reduction: -137 lines**
- Improved caching, error handling, and developer experience

**What Changed:**

**Before (Axios):**
```typescript
// Manual token management
const response = await axios.get('/api/students', {
  headers: { Authorization: `Bearer ${getToken()}` }
});
dispatch(setStudents(response.data));
```

**After (RTK Query):**
```typescript
// Automatic caching, token injection, error handling
const { data: students } = useGetStudentsQuery();
```

**Benefits:**
- ✅ Automatic JWT token injection via `prepareHeaders`
- ✅ Automatic caching and request deduplication
- ✅ Built-in loading and error states
- ✅ Tag-based cache invalidation
- ✅ Optimistic UI updates support
- ✅ Reduced boilerplate code

**Files Created:**
- `react/src/store/api/baseQuery.ts` - JWT injection + error handling
- `react/src/store/api/apiSlice.ts` - Central API configuration
- `react/src/features/auth/api/authApi.ts` - Auth endpoints
- `react/src/features/students/api/studentsApi.ts` - Student endpoints

**Files Deleted:**
- Axios client, services, thunks (7 files)

### Phase 5: E2E Testing ✅

**Added:**
- Playwright test infrastructure
- Auth flow E2E tests (5 test cases)
- Dashboard navigation E2E tests (6 test cases)

### Phase 6: Security Improvements ✅

**Backend:**
- Added `@PreAuthorize("hasRole('ADMIN')")` to admin endpoints
- Fixed missing authorization checks

**Frontend:**
- Migrated hardcoded API URL to environment variables
- Improved TypeScript type safety (removed `as any`)
- Created `.env.development` and `.env.production` templates

---

## 🧪 Testing

### Manual Testing with cURL

**Complete Auth Flow:**
```bash
# 1. Login
TOKEN=$(curl -s -X POST http://localhost:3030/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}' \
  | jq -r '.token')

# 2. Get profile
curl -X GET http://localhost:3030/api/me \
  -H "Authorization: Bearer $TOKEN"

# 3. Get students (Admin only)
curl -X GET http://localhost:3030/api/admin/students \
  -H "Authorization: Bearer $TOKEN"
```

### Automated Testing

**Run All Tests:**
```bash
# Backend unit tests
cd API_GestionNotes/ManageNotes && mvn test

# Frontend E2E tests
cd react && npm test
```

### API Testing with Postman/Swagger

1. **Import OpenAPI spec** from `http://localhost:3030/api-docs`
2. **Use Swagger UI** at `http://localhost:3030/swagger-ui.html`
3. **Set environment variables:**
   - `baseUrl`: `http://localhost:3030/api`
   - `token`: Your JWT token from login

---

## 🏫 Academic Structure

### Student Levels
- `LEVEL1` - First Year
- `LEVEL2` - Second Year  
- `LEVEL3` - Third Year
- `LEVEL4` - Fourth Year (Master's)
- `LEVEL5` - Fifth Year (Master's)

### Academic Cycles
- `BACHELOR` - Undergraduate (Levels 1-3)
- `MASTER` - Graduate (Levels 4-5)
- `PHD` - Doctoral

### Grading Periods
- `CC_1` - Continuous Assessment 1
- `CC_2` - Continuous Assessment 2
- `SN_1` - Session Normale 1 (Midterm)
- `SN_2` - Session Normale 2 (Final)

### Semester Configuration
- **Semester 1**: September 8, 2025 - February 23, 2026
- **Semester 2**: March 15, 2026 - June 2, 2026

---

## 🔧 Configuration

### Backend Environment Variables
```bash
# Database
DB_PASSWORD=nathan
DB_URL=jdbc:postgresql://localhost:5432/managerNotes

# JWT Security
JWT_SECRET=R2RThdHu3OKhHvJ3QgUnkQsVv8Af+Jsw9A9+1oSx6nE=
JWT_EXPIRATION=86400000  # 24 hours in milliseconds

# Email (Optional)
EMAIL_USERNAME=your-email@gmail.com
EMAIL_PASSWORD=your-app-password
```

### Frontend Environment Variables
```bash
# Development (.env.development)
VITE_API_BASE_URL=http://localhost:3030/api

# Production (.env.production)
VITE_API_BASE_URL=https://api.production.com/api
```

### Application Properties (Backend)
```properties
# Server Configuration
server.port=3030

# Database Configuration
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/managerNotes}
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD:nathan}
spring.jpa.hibernate.ddl-auto=update

# Security Configuration
app.jwtSecret=${JWT_SECRET}
app.jwtExpirationMs=${JWT_EXPIRATION:86400000}

# File Upload
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

---

## 🚀 Deployment

### Production Configuration

**Backend (application-prod.properties):**
```properties
# Production Database
spring.datasource.url=jdbc:postgresql://prod-db:5432/managernotes
spring.jpa.hibernate.ddl-auto=validate

# Security
spring.security.enabled=true
logging.level.com.university.ManageNotes.security=WARN

# Performance
spring.jpa.show-sql=false
logging.level.org.hibernate.SQL=WARN
```

**Frontend (.env.production):**
```bash
VITE_API_BASE_URL=https://api.yourdomain.com/api
```

### Docker Deployment

**Backend Dockerfile:**
```dockerfile
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/ManageNotes-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 3030
ENTRYPOINT ["java","-jar","/app.jar"]
```

**Frontend Build:**
```bash
cd react
npm run build
# Serve dist/ folder with nginx or similar
```

### Database Migration
```bash
# Run Flyway migrations
cd API_GestionNotes/ManageNotes
mvn flyway:migrate

# Validate schema
mvn flyway:validate
```

---

## 🤝 Contributing

### Development Guidelines
1. Follow Spring Boot best practices for backend
2. Use functional React components and hooks for frontend
3. Implement proper error handling
4. Write tests for new features
5. Update API documentation when adding endpoints

### Code Style
**Backend:**
- Use Lombok for boilerplate reduction
- Follow RESTful API conventions
- Implement Bean Validation
- Use MapStruct for entity-DTO mapping

**Frontend:**
- Use TypeScript strict mode
- Follow React hooks best practices
- Use RTK Query for all API calls
- Write E2E tests for critical flows

### Pull Request Process
1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'feat: add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

---

## 🆘 Support & Troubleshooting

### Common Issues

**Backend: Database Connection Issues**
```bash
# Check PostgreSQL status
sudo systemctl status postgresql

# Reset database password
sudo -u postgres psql -c "ALTER USER postgres PASSWORD 'nathan';"
```

**Backend: JWT Token Expiration**
- Default expiration: 24 hours
- No refresh mechanism currently (planned for future)
- Frontend: User must re-login after expiration

**Frontend: API Connection Issues**
- Check backend is running: `curl http://localhost:3030/api/me`
- Verify environment variable: `echo $VITE_API_BASE_URL`
- Check browser console for CORS errors

**Frontend: Build Errors**
```bash
# Clear cache and reinstall
rm -rf node_modules package-lock.json
npm install
npm run build
```

**E2E Tests Failing:**
```bash
# Ensure backend is running first
cd API_GestionNotes/ManageNotes && mvn spring-boot:run

# In separate terminal
cd react && npm test
```

### Getting Help
- **Issues**: Check the [Issues](../../issues) page
- **API Docs**: Visit `/swagger-ui.html` when backend running
- **Full API Documentation**: See [API_DOCUMENTATION.md](./API_DOCUMENTATION.md)

---

## 📊 Monitoring & Logging

### Application Metrics (Backend)
```bash
# Spring Boot Actuator endpoints
curl http://localhost:3030/actuator/health
curl http://localhost:3030/actuator/info
```

### Logging Configuration (Backend)
```properties
# Detailed logging for development
logging.level.com.university.ManageNotes=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

### Frontend Debugging
```typescript
// Redux DevTools shows all RTK Query state
// - API call status
// - Cached data
// - Query invalidation
// - Error details
```

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 📈 Project Status

**Current Version:** 1.0.0  
**Last Updated:** January 30, 2026  
**Maintained by:** K48 Development Team

**Recent Milestones:**
- ✅ RTK Query Migration Complete (Phases 1-4)
- ✅ E2E Test Suite Added (Phase 5)
- ✅ Security Improvements Applied (Phase 6)
- ✅ Comprehensive Documentation (Phase 7)
- ⏳ Production Deployment (Planned)

---

## 🔗 Quick Links

- **Backend API Documentation**: [API_DOCUMENTATION.md](./API_DOCUMENTATION.md)
- **Swagger UI**: http://localhost:3030/swagger-ui.html (when backend running)
- **Frontend**: http://localhost:5173 (when dev server running)
- **OpenAPI Spec**: http://localhost:3030/api-docs

---

**Happy Coding! 🎓📚**
