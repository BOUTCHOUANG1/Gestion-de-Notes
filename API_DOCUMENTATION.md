# ManageNotes API Documentation

**Version:** 1.0.0  
**Base URL:** `http://localhost:3030/api`  
**Authentication:** JWT Bearer Token  
**Last Updated:** January 30, 2026

## Table of Contents

1. [Authentication](#authentication)
2. [User Profile](#user-profile)
3. [Students](#students)
4. [Error Handling](#error-handling)
5. [Frontend Integration (RTK Query)](#frontend-integration-rtk-query)

---

## Authentication

### POST /auth/login

Authenticate a user and receive a JWT token.

**Authorization:** None (Public)

**Request Body:**
```json
{
  "username": "string (required)",
  "password": "string (required)"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "id": 1,
  "username": "admin",
  "email": "admin@university.com",
  "role": "ADMIN"
}
```

**Error Responses:**
- `401 Unauthorized` - Invalid credentials
- `400 Bad Request` - Missing or invalid fields

**Example (cURL):**
```bash
curl -X POST http://localhost:3030/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

**Example (JavaScript/RTK Query):**
```typescript
const { data, error } = await login({
  username: 'admin',
  password: 'admin123'
});
```

---

### POST /auth/admin/register

Register a new user in the system. **Admin only**.

**Authorization:** Required - `ROLE_ADMIN`

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Request Body:**
```json
{
  "username": "string (required)",
  "password": "string (required)",
  "email": "string (required)",
  "firstName": "string (required)",
  "lastName": "string (required)",
  "phone": "string (optional)",
  "role": "ADMIN | TEACHER | STUDENT (required)",
  "registrationKey": "string (optional)",
  "level": "LEVEL1 | LEVEL2 | LEVEL3 | LEVEL4 | LEVEL5 (optional)"
}
```

**Response (201 Created):**
```json
{
  "message": "User registered successfully",
  "success": true
}
```

**Error Responses:**
- `403 Forbidden` - Missing ADMIN role
- `400 Bad Request` - Validation errors
- `409 Conflict` - Username or email already exists

**Example (cURL):**
```bash
curl -X POST http://localhost:3030/api/auth/admin/register \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john.doe",
    "password": "SecurePass123",
    "email": "john@university.com",
    "firstName": "John",
    "lastName": "Doe",
    "role": "STUDENT",
    "level": "LEVEL1"
  }'
```

---

### GET /auth/admin/profile

Get the current admin's profile information. **Admin only**.

**Authorization:** Required - `ROLE_ADMIN`

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response (200 OK):**
```json
{
  "userId": 1,
  "username": "admin",
  "email": "admin@university.com",
  "firstName": "John",
  "lastName": "Admin",
  "role": {
    "id": 1,
    "appRole": "ADMIN"
  }
}
```

**Error Responses:**
- `403 Forbidden` - Missing ADMIN role
- `401 Unauthorized` - Invalid/missing token

---

### POST /auth/password

Change the current user's password.

**Authorization:** Required (any authenticated user)

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Request Body:**
```json
{
  "newPassword": "string (required)"
}
```

**Response (200 OK):**
```json
{
  "message": "Password changed successfully",
  "success": true
}
```

---

### POST /auth/logout

Log out the current user.

**Authorization:** Required (any authenticated user)

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response (200 OK):**
```json
{
  "message": "User logged out successfully",
  "success": true
}
```

---

## User Profile

### GET /me

Get the currently authenticated user's profile information. **Works for all roles**.

**Authorization:** Required (any authenticated user)

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response (200 OK):**
```json
{
  "id": 13,
  "firstName": "Jane",
  "lastName": "Student",
  "email": "jane@university.com",
  "username": "jane.student",
  "role": "STUDENT"
}
```

**Notes:**
- This endpoint was added in Phase 4 to unify profile access across all roles
- The `role` field is returned as a string (not an object)
- Frontend uses this endpoint in `PrivateRoute` for authentication verification

**Error Responses:**
- `401 Unauthorized` - Invalid/missing token
- `404 Not Found` - User not found in database

**Example (JavaScript/RTK Query):**
```typescript
const { data: profile, isLoading, error } = useGetProfileQuery();
// profile = { id: 13, firstName: "Jane", ... }
```

---

## Students

### GET /admin/students

Get a list of all students with pagination and sorting. **Admin only**.

**Authorization:** Required - `ROLE_ADMIN`

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Query Parameters:**
- `pageNumber` (optional, default: 0) - Page number (0-indexed)
- `pageSize` (optional, default: 10) - Number of items per page
- `sortBy` (optional, default: "id") - Field to sort by
- `sortOrder` (optional, default: "asc") - Sort direction: "asc" or "desc"

**Response (200 OK):**
```json
[
  {
    "id": 13,
    "firstName": "Jane",
    "lastName": "Student",
    "email": "jane@university.com",
    "username": "jane.student",
    "role": "STUDENT",
    "matricule": "STU2024001",
    "level": "LEVEL1",
    "cycle": "BACHELOR",
    "speciality": "Computer Science"
  }
]
```

**Error Responses:**
- `403 Forbidden` - Missing ADMIN/TEACHER role
- `401 Unauthorized` - Invalid/missing token

**Example (cURL):**
```bash
curl -X GET "http://localhost:3030/api/admin/students?pageNumber=0&pageSize=20&sortBy=lastName&sortOrder=asc" \
  -H "Authorization: Bearer <token>"
```

**Example (JavaScript/RTK Query):**
```typescript
const { data: students } = useGetStudentsQuery({
  pageNumber: 0,
  pageSize: 20,
  sortBy: 'lastName',
  sortOrder: 'asc'
});
```

---

## Error Handling

### Standard Error Response Format

All API errors follow a consistent format:

```json
{
  "message": "Error description",
  "detail": "More detailed error information",
  "success": false
}
```

### Common HTTP Status Codes

| Code | Meaning | Description |
|------|---------|-------------|
| 200 | OK | Request succeeded |
| 201 | Created | Resource created successfully |
| 400 | Bad Request | Invalid request format or validation error |
| 401 | Unauthorized | Missing or invalid authentication token |
| 403 | Forbidden | Authenticated but lacking required role/permission |
| 404 | Not Found | Requested resource does not exist |
| 409 | Conflict | Resource already exists (e.g., duplicate username) |
| 500 | Internal Server Error | Server-side error |

### Frontend Error Handling (RTK Query)

The frontend automatically handles errors through `baseQueryWithAuth`:

**Network Errors:**
```typescript
// Displays: "Network Error - Unable to connect to the server"
if (result.error.status === 'FETCH_ERROR') {
  // User sees notification automatically
}
```

**4xx Errors:**
```typescript
// Automatically extracts error message from:
// - data.detail
// - data.message
// - data.error
// - data.title
// Falls back to: "An error occurred"
```

**Example Error Messages:**
- Login failure: "Invalid username or password"
- Missing role: "Access denied: Admin role required"
- Validation: "Email format is invalid"

---

## Frontend Integration (RTK Query)

### Setup

The frontend uses **Redux Toolkit Query (RTK Query)** for API communication.

**Base Configuration:**
```typescript
// react/src/store/api/baseQuery.ts
const baseQuery = fetchBaseQuery({
  baseUrl: import.meta.env.VITE_API_BASE_URL || 'http://localhost:3030/api',
  prepareHeaders: (headers) => {
    const token = getToken('token');
    if (token) {
      headers.set('Authorization', `Bearer ${token}`);
    }
    return headers;
  },
});
```

**Environment Variables:**
```bash
# .env.development
VITE_API_BASE_URL=http://localhost:3030/api

# .env.production
VITE_API_BASE_URL=https://api.production.com/api
```

---

### Available RTK Query Hooks

#### Authentication Hooks

**useLoginMutation**
```typescript
import { useLoginMutation } from '@/features/auth/api/authApi';

const [login, { isLoading, error }] = useLoginMutation();

const handleLogin = async () => {
  try {
    const result = await login({ username, password }).unwrap();
    // result = { token, id, username, email, role }
    // Token automatically saved to localStorage
    // User automatically redirected to /dashboard
  } catch (err) {
    // Error automatically displayed to user
  }
};
```

**useRegisterMutation**
```typescript
import { useRegisterMutation } from '@/features/auth/api/authApi';

const [register, { isLoading }] = useRegisterMutation();

await register({
  username: 'john.doe',
  password: 'SecurePass123',
  email: 'john@university.com',
  firstName: 'John',
  lastName: 'Doe',
  role: 'STUDENT',
  level: 'LEVEL1'
});
```

**useGetProfileQuery**
```typescript
import { useGetProfileQuery } from '@/features/auth/api/authApi';

const { data: profile, isLoading, error } = useGetProfileQuery();

if (isLoading) return <Spinner />;
if (error) return <div>Error loading profile</div>;

console.log(profile);
// { id: 13, firstName: "Jane", lastName: "Student", ... }
```

---

#### Student Management Hooks

**useGetStudentsQuery**
```typescript
import { useGetStudentsQuery } from '@/features/students/api/studentsApi';

const { data: students, isLoading } = useGetStudentsQuery();

students?.map(student => (
  <div key={student.id}>{student.firstName} {student.lastName}</div>
));
```

---

### Cache Invalidation

RTK Query automatically invalidates and refetches data when mutations complete:

**Tag-Based Invalidation:**
```typescript
// Login mutation invalidates 'Auth' and 'User' tags
// This triggers automatic refetch of useGetProfileQuery
useLoginMutation() // invalidatesTags: ['Auth', 'User']
useGetProfileQuery() // providesTags: [{ type: 'User', id: 'PROFILE' }]
```

**Available Tags:**
- `Auth` - Authentication state
- `User` - User profile data
- `Students` - Student list data
- `Grades` - Grade data

---

### Token Management

**Token Storage:**
```typescript
// Stored in localStorage on login
setTokens({ token: 'jwt_token_here' });

// Retrieved from sessionStorage OR localStorage
const token = getToken('token');

// Cleared on logout
clearTokens();
```

**Token Lifecycle:**
1. User logs in → Token stored in localStorage
2. Every API request → Token injected in `Authorization: Bearer <token>` header
3. User closes browser → Token persists in localStorage
4. User logs out → Token cleared from both localStorage and sessionStorage
5. Token expires (24 hours) → Backend returns 401, frontend can handle refresh

---

### Example: Complete Authentication Flow

```typescript
// 1. User submits login form
const [login] = useLoginMutation();
const result = await login({ username, password });

// 2. RTK Query automatically:
//    - Sends POST /auth/login
//    - Receives { token, id, username, role }
//    - Calls setTokens({ token }) 
//    - Dispatches markAsAuthenticated()
//    - Dispatches navigateTo('/dashboard')

// 3. Dashboard loads and fetches profile
const { data: profile } = useGetProfileQuery();

// 4. RTK Query automatically:
//    - Sends GET /me with Authorization header
//    - Caches response
//    - Updates component when data arrives

// 5. User navigates to students page
const { data: students } = useGetStudentsQuery();

// 6. User logs out
dispatch(clearTokens());
navigate('/auth');
```

---

## Migration Notes (Phases 1-4)

### What Changed in RTK Query Migration

**Before (Axios + createAsyncThunk):**
```typescript
// Old approach - manual configuration
const response = await axios.get('/api/students', {
  headers: { Authorization: `Bearer ${token}` }
});
dispatch(setStudents(response.data));
```

**After (RTK Query):**
```typescript
// New approach - automatic caching, refetching, error handling
const { data: students } = useGetStudentsQuery();
```

**Benefits:**
- ✅ Automatic token injection via `prepareHeaders`
- ✅ Automatic caching and deduplication
- ✅ Automatic refetching on window focus
- ✅ Built-in loading and error states
- ✅ Optimistic updates support
- ✅ Tag-based cache invalidation
- ✅ **137 lines of code removed** (net reduction)

---

## Security Considerations

### JWT Token Storage

**Current Implementation:**
- Tokens stored in `localStorage` (write)
- Tokens read from `sessionStorage` OR `localStorage` (fallback)

**Trade-offs:**
- ✅ Works across browser tabs
- ✅ Survives page refreshes
- ⚠️ Vulnerable to XSS attacks (if site has XSS vulnerability)
- ⚠️ Cannot use httpOnly cookies (would break mobile apps)

**Mitigation:**
- Keep JWT expiration short (24 hours)
- Implement Content Security Policy (CSP)
- Sanitize all user inputs
- Regular security audits

---

### Role-Based Access Control

**Backend:**
```java
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<MessageResponse> register(...) { }
```

**Frontend:**
```typescript
// No frontend role checks - backend is source of truth
// If user lacks permission, backend returns 403 Forbidden
```

**Best Practice:** Always enforce authorization on the backend, frontend checks are only for UX.

---

## Rate Limiting

**Current Status:** Not implemented

**Recommendations for Production:**
- Add rate limiting to `/auth/login` endpoint (prevent brute force)
- Add rate limiting to `/auth/admin/register` endpoint
- Use Spring Security filters or API Gateway

---

## CORS Configuration

**Current Status:** Should be configured for production

**Example Production CORS:**
```java
@Configuration
public class SecurityConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                    .allowedOrigins("https://app.production.com")
                    .allowedMethods("GET", "POST", "PUT", "DELETE")
                    .allowedHeaders("*")
                    .allowCredentials(true);
            }
        };
    }
}
```

---

## Monitoring and Debugging

### Backend Logging
```bash
# Enable SQL logging
logging.level.org.hibernate.SQL=DEBUG

# Enable security logging
logging.level.org.springframework.security=DEBUG
```

### Frontend Network Inspection
```typescript
// RTK Query DevTools available in Redux DevTools
// Shows all API calls, cache state, and query status
```

### Common Issues

**401 Unauthorized:**
- Check if token is present: `localStorage.getItem('token')`
- Check token expiration
- Verify token format in Authorization header

**403 Forbidden:**
- User authenticated but lacks required role
- Check @PreAuthorize annotation on backend endpoint
- Verify user's role in database

**FETCH_ERROR:**
- Backend not running
- Wrong base URL
- CORS issue

---

## Testing

### Manual Testing (cURL)
```bash
# 1. Login
TOKEN=$(curl -X POST http://localhost:3030/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' \
  | jq -r '.token')

# 2. Get profile
curl -X GET http://localhost:3030/api/me \
  -H "Authorization: Bearer $TOKEN"

# 3. Get students (Admin only)
curl -X GET http://localhost:3030/api/admin/students \
  -H "Authorization: Bearer $TOKEN"
```

### E2E Testing (Playwright)
```typescript
// tests/e2e/auth.spec.ts
test('should login successfully', async ({ page }) => {
  await page.goto('/auth');
  await page.fill('input[name="username"]', 'admin');
  await page.fill('input[name="password"]', 'admin123');
  await page.click('button[type="submit"]');
  await expect(page).toHaveURL(/\/dashboard/);
});
```

---

## Changelog

### Version 1.0.0 (January 30, 2026)

**Phase 1-3: RTK Query Migration**
- Migrated from Axios to RTK Query
- Created centralized API configuration
- Implemented automatic token injection
- Added tag-based cache invalidation

**Phase 4: Endpoint Fixes**
- ✅ Added `GET /api/me` endpoint (unified profile access)
- ✅ Fixed register endpoint path: `/auth/register` → `/auth/admin/register`
- ✅ Aligned frontend/backend DTOs: `userProfileResDto`

**Phase 5: Testing**
- ✅ Added Playwright E2E test infrastructure
- ✅ Created auth flow tests
- ✅ Created dashboard navigation tests

**Phase 6: Security**
- ✅ Added `@PreAuthorize` to admin endpoints
- ✅ Migrated base URL to environment variables
- ✅ Improved TypeScript type safety in error handling

---

## Support

**Documentation:** See project README  
**API Explorer:** http://localhost:3030/swagger-ui.html  
**OpenAPI Spec:** http://localhost:3030/api-docs

---

**Last Updated:** January 30, 2026  
**Maintained by:** K48 Development Team
