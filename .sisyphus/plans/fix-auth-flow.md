# Fix Critical Authentication Flow Issues

## TL;DR

> **Quick Summary**: Add unified `/api/me` endpoint for frontend auth validation, fix frontend register path mismatch, resolve DTO field mapping issues between backend and frontend, and fix incomplete DTO mapper method.
> 
> **Deliverables**:
> - New `ProfileController.java` with `GET /api/me` endpoint
> - Updated `AuthService` interface and implementation with profile retrieval method
> - Fixed `UserResponse` DTO with proper JSON serialization for frontend compatibility
> - Fixed `AuthServiceImpl.getAdminResponse()` mapper to include all required fields
> - Updated frontend `authApi.ts` register endpoint path
> - Both backend (Maven) and frontend (npm) builds passing
> - Single atomic commit with all changes
> 
> **Estimated Effort**: Short (2-3 hours)
> **Parallel Execution**: YES - 2 waves
> **Critical Path**: Wave 1 (all tasks) → Wave 2 (build verification) → Wave 3 (commit)

---

## Context

### Original Request
User requested fixes for critical authentication flow issues:
1. Create `ProfileController.java` with `GET /api/me` endpoint that returns current user profile based on JWT
2. Fix frontend register path from `'auth/register'` to `'auth/admin/register'`
3. Build both backend (mvn compile) and frontend (npm run build)
4. Commit: `'fix: add unified /api/me endpoint and fix register path'`

### Interview Summary
**Key Discussions**:
- **Frontend Issue**: `PrivateRoute` component calls `useGetProfileQuery()` which fetches `GET /api/me` → returns 404 because endpoint doesn't exist
- **Path Mismatch**: Frontend calls `POST auth/register` but backend expects `POST auth/admin/register`
- **DTO Field Mismatch**: Backend returns `userId` (Long), frontend expects `id` (number)
- **Role Serialization**: Backend returns `Roles` entity object, frontend expects string
- **Incomplete Mapper**: `AuthServiceImpl.getAdminResponse()` doesn't set `userId`, `createdDate`, `lastModifiedDate`

**Research Findings**:
- **Current Backend Endpoints**:
  - ✅ `POST /api/auth/login` - Works
  - ✅ `POST /api/auth/admin/register` - Admin-only registration
  - ✅ `GET /api/auth/admin/profile` - Returns `UserResponse` (admin only)
  - ✅ `GET /api/student/profile` - Returns `StudentResponse`
  - ✅ `GET /api/teacher/profile` - Returns `TeacherResponse`
  - ❌ **MISSING**: `GET /api/me` - Unified profile endpoint
  
- **JWT Authentication Flow**:
  1. `AuthTokenFilter` extracts JWT from `Authorization: Bearer <token>` header
  2. Validates token using `JwtUtils.validateJwtToken()`
  3. Loads `UserDetailsImpl` from database
  4. Sets `Authentication` in `SecurityContextHolder`
  5. Controllers access via `@AuthenticationPrincipal UserDetailsImpl`

- **Controller Patterns in Codebase**:
  - Use `@AuthenticationPrincipal UserDetailsImpl` (NOT `Authentication` parameter)
  - Use `new ResponseEntity<>(data, HttpStatus.XXX)` (NOT `ResponseEntity.ok()`)
  - Always add `@Operation` annotation for Swagger
  - Constructor injection via `@RequiredArgsConstructor`
  - Let exceptions bubble to `GlobalExceptionHandler`

- **Test Infrastructure**:
  - Backend: Maven with `spring-boot-starter-test` dependency
  - Frontend: npm build script uses `tsc -b && vite build`
  - No dedicated test framework setup detected for unit tests
  - Manual verification will be primary QA method

### Exploration Summary
**Backend Structure Analysis**:
- Found `UserResponse.java` DTO at `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/dto/response/UserResponse.java`
- Found `AuthServiceImpl.getAdminResponse()` helper method with incomplete mapping
- Found existing controller patterns following consistent structure

**Frontend Structure Analysis**:
- RTK Query API definition in `react/src/features/auth/api/authApi.ts`
- Base URL: `http://localhost:3030/api`
- Frontend expects `userProfileResDto` interface with `id` field (not `userId`)

---

## Work Objectives

### Core Objective
Add unified user profile endpoint to resolve 404 errors in frontend authentication flow, fix path mismatch in registration, and ensure DTO compatibility between backend and frontend.

### Concrete Deliverables
- `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/controller/ProfileController.java` - New file
- `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/service/AuthService.java` - Modified (add method)
- `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/service/impl/AuthServiceImpl.java` - Modified (implement method, fix mapper)
- `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/dto/response/UserResponse.java` - Modified (add JSON annotations)
- `react/src/features/auth/api/authApi.ts` - Modified (fix path)

### Definition of Done
- [ ] `GET /api/me` endpoint exists and returns `UserResponse` DTO
- [ ] Backend compiles successfully: `cd API_GestionNotes/ManageNotes && mvn compile` → BUILD SUCCESS
- [ ] Frontend builds successfully: `cd react && npm run build` → Exit code 0
- [ ] UserResponse DTO serializes `userId` as `id` for frontend (using `@JsonProperty`)
- [ ] UserResponse DTO serializes `role` as string (e.g., "ADMIN", "TEACHER", "STUDENT")
- [ ] Register endpoint path fixed in frontend: `'auth/admin/register'`
- [ ] Changes committed with message: `'fix: add unified /api/me endpoint and fix register path'`

### Must Have
- ProfileController follows existing controller patterns exactly
- Use `@AuthenticationPrincipal UserDetailsImpl` (not `Authentication`)
- Use `new ResponseEntity<>()` pattern (not `ResponseEntity.ok()`)
- Add `@Operation` annotation for Swagger documentation
- Service method extracts user from database by ID
- Proper error handling (throw `ResourceNotFoundException` if user not found)
- JSON serialization fixes for DTO field compatibility

### Must NOT Have (Guardrails)
- Do NOT use `SecurityContextHolder.getContext().getAuthentication()` in controller
- Do NOT use `ResponseEntity.ok()` shortcut
- Do NOT add try-catch blocks in controller (let GlobalExceptionHandler handle)
- Do NOT skip Swagger `@Operation` annotation
- Do NOT change endpoint path from `/api/me` (frontend hardcoded)
- Do NOT break existing endpoints
- Do NOT modify frontend TypeScript interface (backend DTO adapts to frontend)
- Do NOT return Roles entity object directly (serialize to string)

---

## Verification Strategy

> Manual verification only - no automated test infrastructure detected.

### Test Decision
- **Infrastructure exists**: Backend has spring-boot-starter-test, but no dedicated test files for controllers/services
- **User wants tests**: NOT SPECIFIED - defaulting to manual verification
- **Framework**: Manual testing via curl/Postman
- **QA approach**: Manual verification with specific commands and expected outputs

### Manual Verification Procedures

Each TODO includes EXECUTABLE verification procedures that can be run via terminal:

**For Backend API changes** (using curl via Bash):
```bash
# Start backend server
cd API_GestionNotes/ManageNotes
mvn spring-boot:run &

# Wait for server startup
sleep 10

# Login to get JWT token
TOKEN=$(curl -s -X POST http://localhost:3030/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' \
  | jq -r '.token')

# Test /api/me endpoint
curl -s -X GET http://localhost:3030/api/me \
  -H "Authorization: Bearer $TOKEN" \
  | jq .

# Assert: Response has "id" field (not "userId")
# Assert: Response has "role" field as string (e.g., "ADMIN")
# Assert: HTTP status 200
```

**For Frontend changes** (using Bash):
```bash
# Build frontend
cd react
npm run build
# Assert: Exit code 0
# Assert: No TypeScript errors in output
```

**Evidence to Capture**:
- Terminal output from mvn compile showing BUILD SUCCESS
- Terminal output from npm run build showing successful compilation
- JSON response from `GET /api/me` showing correct field names
- Git commit SHA after successful commit

---

## Execution Strategy

### Parallel Execution Waves

> Maximize throughput by grouping independent tasks into parallel waves.
> Each wave completes before the next begins.

```
Wave 1 (Start Immediately - All Backend Changes):
├── Task 1: Fix UserResponse DTO with JSON annotations
├── Task 2: Create ProfileController.java
├── Task 3: Add getCurrentUserProfile method to AuthService interface
├── Task 4: Implement getCurrentUserProfile in AuthServiceImpl
├── Task 5: Fix AuthServiceImpl.getAdminResponse() mapper
└── Task 6: Fix frontend register endpoint path

Wave 2 (After Wave 1 - Build Verification):
├── Task 7: Build and verify backend (mvn compile)
└── Task 8: Build and verify frontend (npm run build)

Wave 3 (After Wave 2 - Atomic Commit):
└── Task 9: Git commit all changes

Critical Path: Wave 1 → Wave 2 → Wave 3
Parallel Speedup: ~60% faster than sequential (Wave 1 has 6 parallel tasks)
```

### Dependency Matrix

| Task | Depends On | Blocks | Can Parallelize With |
|------|------------|--------|---------------------|
| 1 | None | 7 | 2, 3, 4, 5, 6 |
| 2 | None | 7 | 1, 3, 4, 5, 6 |
| 3 | None | 4, 7 | 1, 2, 5, 6 |
| 4 | 3 | 7 | 1, 2, 5, 6 (must wait for 3) |
| 5 | None | 7 | 1, 2, 3, 6 |
| 6 | None | 8 | 1, 2, 3, 4, 5 |
| 7 | 1, 2, 3, 4, 5 | 9 | 8 |
| 8 | 6 | 9 | 7 |
| 9 | 7, 8 | None | None (final) |

### Agent Dispatch Summary

| Wave | Tasks | Recommended Agents |
|------|-------|-------------------|
| 1 | 1, 2, 3, 4, 5, 6 | 6 parallel `delegate_task` calls with `run_in_background=true` |
| 2 | 7, 8 | 2 parallel `delegate_task` calls with `run_in_background=true` |
| 3 | 9 | Single `delegate_task` with `load_skills=['git-master']` |

---

## TODOs

> Implementation + Verification = ONE Task. Never separate.
> EVERY task MUST have: Recommended Agent Profile + Parallelization info.

---

### Task 1: Fix UserResponse DTO with JSON Serialization Annotations

**What to do**:
- Open `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/dto/response/UserResponse.java`
- Add `@JsonProperty("id")` annotation to `userId` field to serialize as `"id"` for frontend
- Add `@JsonIgnore` to `role` field (Roles entity)
- Add new field `private String roleName;` with getter/setter
- Add `@JsonProperty("role")` annotation to `roleName` field
- Update all usages to set `roleName` from `role.getAppRole().name()`

**Must NOT do**:
- Do NOT change field name from `userId` to `id` (breaks existing backend code)
- Do NOT serialize Roles entity directly (causes circular reference issues)
- Do NOT remove `role` field entirely (existing code depends on it)

**Recommended Agent Profile**:
- **Category**: `quick`
  - Reason: Single file modification, straightforward annotation additions
- **Skills**: []
  - Reason: Standard Java/Jackson annotations, no specialized domain knowledge needed
- **Skills Evaluated but Omitted**:
  - `typescript-programmer`: Not needed (Java file only)
  - `frontend-ui-ux`: Not applicable (backend DTO)
  - `git-master`: Not needed yet (commit happens later)

**Parallelization**:
- **Can Run In Parallel**: YES
- **Parallel Group**: Wave 1 (with Tasks 2, 3, 4, 5, 6)
- **Blocks**: Task 7 (backend build)
- **Blocked By**: None (can start immediately)

**References**:

**Pattern References** (existing code to follow):
- `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/dto/response/LoginResponse.java` - Example of Jackson `@JsonProperty` usage for field aliasing
- Existing DTOs in `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/dto/response/` - Standard DTO patterns with Lombok annotations

**Type References** (entities to understand):
- `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/entity/Roles.java` - The Roles entity structure with `AppRole` enum
- `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/entity/Users.java` - User entity relationship with Roles

**Documentation References**:
- Jackson `@JsonProperty` docs: https://fasterxml.github.io/jackson-annotations/javadoc/2.9/com/fasterxml/jackson/annotation/JsonProperty.html - Field aliasing for JSON serialization
- Jackson `@JsonIgnore` docs: https://fasterxml.github.io/jackson-annotations/javadoc/2.9/com/fasterxml/jackson/annotation/JsonIgnore.html - Exclude fields from serialization

**WHY Each Reference Matters**:
- LoginResponse shows this codebase already uses `@JsonProperty` for field aliasing (pattern established)
- Roles entity understanding is critical to extract the string role name correctly (`role.getAppRole().name()`)
- Jackson annotations are the standard way to control JSON serialization in Spring Boot

**Acceptance Criteria**:

**Automated Verification** (using Bash + Maven):
```bash
# Compile backend to verify syntax
cd /home/boutchouang-nathan/SpringbootProjects/k48/gestionNotes_frontend_backend/API_GestionNotes/ManageNotes
mvn compile -q
# Assert: Exit code 0 (compilation successful)
# Assert: No compilation errors in output

# Verify annotations added (grep check)
grep -n "@JsonProperty(\"id\")" src/main/java/com/university/ManageNotes/dto/response/UserResponse.java
# Assert: Found annotation on userId field

grep -n "@JsonProperty(\"role\")" src/main/java/com/university/ManageNotes/dto/response/UserResponse.java
# Assert: Found annotation on roleName field

grep -n "private String roleName" src/main/java/com/university/ManageNotes/dto/response/UserResponse.java
# Assert: Found new field declaration
```

**Evidence to Capture**:
- Terminal output showing `mvn compile` success
- Grep output confirming annotations exist

**Commit**: NO (groups with all Wave 1 tasks into single commit in Task 9)

---

### Task 2: Create ProfileController.java

**What to do**:
- Create new file: `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/controller/ProfileController.java`
- Add package declaration, imports, and class annotations following existing controller patterns
- Use `@RestController`, `@RequestMapping("/api")`, `@RequiredArgsConstructor`, `@Tag`
- Inject `AuthService` via constructor injection (final field)
- Create `GET /me` endpoint method:
  - Use `@GetMapping("/me")` annotation
  - Add `@Operation(summary = "Get current user profile", description = "Returns profile of authenticated user based on JWT")`
  - Accept `@AuthenticationPrincipal UserDetailsImpl userPrincipal` parameter
  - Call `authService.getCurrentUserProfile(userPrincipal.getId())`
  - Return `new ResponseEntity<>(result, HttpStatus.OK)`

**Must NOT do**:
- Do NOT use `Authentication` parameter instead of `@AuthenticationPrincipal UserDetailsImpl`
- Do NOT use `ResponseEntity.ok()` shortcut (use `new ResponseEntity<>()`)
- Do NOT add try-catch blocks (exceptions bubble to GlobalExceptionHandler)
- Do NOT skip `@Operation` annotation (needed for Swagger docs)
- Do NOT change path to anything other than `/api/me` (frontend expects exact path)

**Recommended Agent Profile**:
- **Category**: `quick`
  - Reason: Single new file following established pattern, no complex logic
- **Skills**: []
  - Reason: Standard Spring Boot controller, patterns already established in codebase
- **Skills Evaluated but Omitted**:
  - `typescript-programmer`: Not applicable (Java file)
  - `frontend-ui-ux`: Not applicable (backend controller)
  - `git-master`: Not needed yet (commit in Task 9)

**Parallelization**:
- **Can Run In Parallel**: YES
- **Parallel Group**: Wave 1 (with Tasks 1, 3, 4, 5, 6)
- **Blocks**: Task 7 (backend build)
- **Blocked By**: None (can start immediately)

**References**:

**Pattern References** (existing code to follow):
- `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/controller/AuthController.java:1-50` - Standard controller structure with `@RestController`, `@RequestMapping`, `@RequiredArgsConstructor`, service injection
- `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/controller/AuthController.java:getCurrentAdmin()` - Example using `@AuthenticationPrincipal UserDetailsImpl` parameter
- `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/controller/StudentController.java:getStudents()` - Example of `new ResponseEntity<>(data, HttpStatus.OK)` return pattern

**API/Type References**:
- `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/security/service/UserDetailsImpl.java` - The UserDetailsImpl class structure (has `getId()` method)
- `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/dto/response/UserResponse.java` - Return type for the endpoint

**Documentation References**:
- Spring Security `@AuthenticationPrincipal` docs: https://docs.spring.io/spring-security/reference/servlet/integrations/mvc.html#mvc-authentication-principal - How to inject authenticated user
- Swagger `@Operation` annotation: https://docs.swagger.io/swagger-core/v2.0.0-RC3/apidocs/io/swagger/v3/oas/annotations/Operation.html - API documentation metadata

**WHY Each Reference Matters**:
- AuthController shows EXACT pattern this codebase uses for authenticated endpoints (`@AuthenticationPrincipal UserDetailsImpl`)
- StudentController demonstrates the `new ResponseEntity<>()` return pattern (NOT `ResponseEntity.ok()`)
- UserDetailsImpl shows available methods like `getId()` to extract user ID for service call

**Acceptance Criteria**:

**Automated Verification** (using Bash + Maven):
```bash
# Verify file created
test -f /home/boutchouang-nathan/SpringbootProjects/k48/gestionNotes_frontend_backend/API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/controller/ProfileController.java
echo $?
# Assert: Exit code 0 (file exists)

# Compile backend
cd /home/boutchouang-nathan/SpringbootProjects/k48/gestionNotes_frontend_backend/API_GestionNotes/ManageNotes
mvn compile -q
# Assert: Exit code 0
# Assert: No compilation errors

# Verify annotations present
grep -n "@GetMapping(\"/me\")" src/main/java/com/university/ManageNotes/controller/ProfileController.java
# Assert: Found endpoint mapping

grep -n "@AuthenticationPrincipal UserDetailsImpl" src/main/java/com/university/ManageNotes/controller/ProfileController.java
# Assert: Found correct parameter type

grep -n "@Operation" src/main/java/com/university/ManageNotes/controller/ProfileController.java
# Assert: Found Swagger annotation
```

**Evidence to Capture**:
- Terminal output showing file exists check (exit code 0)
- Grep output confirming required annotations
- Maven compile success output

**Commit**: NO (groups with Wave 1 into Task 9)

---

### Task 3: Add getCurrentUserProfile Method to AuthService Interface

**What to do**:
- Open `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/service/AuthService.java`
- Add new method signature: `UserResponse getCurrentUserProfile(Long userId);`
- Place after existing `getCurrentAdmin` method for logical grouping
- Add JavaDoc comment describing the method purpose

**Must NOT do**:
- Do NOT modify existing method signatures
- Do NOT add implementation here (interface only)
- Do NOT use different return type than `UserResponse`

**Recommended Agent Profile**:
- **Category**: `quick`
  - Reason: Single line addition to interface, trivial change
- **Skills**: []
  - Reason: Standard Java interface modification, no specialized knowledge needed
- **Skills Evaluated but Omitted**:
  - All skills omitted - too trivial to require specialized knowledge

**Parallelization**:
- **Can Run In Parallel**: YES
- **Parallel Group**: Wave 1 (with Tasks 1, 2, 5, 6)
- **Blocks**: Task 4 (implementation depends on interface), Task 7 (backend build)
- **Blocked By**: None (can start immediately)

**References**:

**Pattern References** (existing code to follow):
- `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/service/AuthService.java:1-20` - Existing interface structure and method signature patterns
- `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/service/AuthService.java:getCurrentAdmin()` - Similar method to use as template (returns UserResponse, takes parameter)

**Type References**:
- `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/dto/response/UserResponse.java` - Return type

**WHY Each Reference Matters**:
- AuthService interface shows existing method signature patterns (return types, parameter styles)
- getCurrentAdmin is similar in purpose (get user profile) - follow same naming convention and style

**Acceptance Criteria**:

**Automated Verification** (using Bash + Maven):
```bash
# Verify method signature added
grep -n "UserResponse getCurrentUserProfile(Long userId)" /home/boutchouang-nathan/SpringbootProjects/k48/gestionNotes_frontend_backend/API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/service/AuthService.java
# Assert: Found method signature

# Compile backend
cd /home/boutchouang-nathan/SpringbootProjects/k48/gestionNotes_frontend_backend/API_GestionNotes/ManageNotes
mvn compile -q
# Assert: Exit code 0 (compilation successful)
```

**Evidence to Capture**:
- Grep output showing method signature exists
- Maven compile success

**Commit**: NO (groups with Wave 1 into Task 9)

---

### Task 4: Implement getCurrentUserProfile in AuthServiceImpl

**What to do**:
- Open `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/service/impl/AuthServiceImpl.java`
- Implement `getCurrentUserProfile(Long userId)` method:
  1. Find user by ID using `userRepository.findById(userId)`
  2. Throw `ResourceNotFoundException` if not found: `.orElseThrow(() -> new ResourceNotFoundException("User", "id", userId))`
  3. Create `UserResponse` using improved `getAdminResponse(user)` helper (will be fixed in Task 5)
  4. Set `roleName` field from `user.getRole().getAppRole().name()` 
  5. Return the `UserResponse`
- Follow existing method patterns in the class (same style as `getCurrentAdmin`)

**Must NOT do**:
- Do NOT add try-catch blocks (exceptions bubble to GlobalExceptionHandler)
- Do NOT return null if user not found (throw ResourceNotFoundException)
- Do NOT skip role name extraction (frontend needs string, not Roles object)
- Do NOT serialize Roles entity directly

**Recommended Agent Profile**:
- **Category**: `quick`
  - Reason: Straightforward CRUD operation following established patterns
- **Skills**: []
  - Reason: Standard Spring Boot service implementation, patterns already exist
- **Skills Evaluated but Omitted**:
  - All skills omitted - follows existing implementation patterns closely

**Parallelization**:
- **Can Run In Parallel**: YES (after Task 3 completes)
- **Parallel Group**: Wave 1 (with Tasks 1, 2, 5, 6)
- **Blocks**: Task 7 (backend build)
- **Blocked By**: Task 3 (needs interface method signature first)

**References**:

**Pattern References** (existing code to follow):
- `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/service/impl/AuthServiceImpl.java:getCurrentAdmin()` - Similar method implementation pattern (findById, orElseThrow, return DTO)
- `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/service/impl/AuthServiceImpl.java:getAdminResponse()` - Helper method to use for DTO mapping (will be fixed in Task 5)

**API/Type References**:
- `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/repository/UserRepository.java` - Repository for database access (injected as `userRepository`)
- `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/exception/ResourceNotFoundException.java` - Exception to throw when user not found
- `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/entity/Roles.java` - Roles entity with `getAppRole()` method

**External References**:
- Spring Data JPA `Optional.orElseThrow()`: https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html#orElseThrow-java.util.function.Supplier- - Standard pattern for handling not found scenarios

**WHY Each Reference Matters**:
- getCurrentAdmin shows EXACT pattern to follow: `findById().orElseThrow()` → map to DTO → return
- getAdminResponse helper avoids duplication (DRY principle) and ensures consistent DTO mapping
- ResourceNotFoundException is the standard exception this codebase uses for not found errors

**Acceptance Criteria**:

**Automated Verification** (using Bash + Maven):
```bash
# Verify method implementation exists
grep -A 10 "public UserResponse getCurrentUserProfile(Long userId)" /home/boutchouang-nathan/SpringbootProjects/k48/gestionNotes_frontend_backend/API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/service/impl/AuthServiceImpl.java | grep "userRepository.findById"
# Assert: Found repository call

grep -A 10 "public UserResponse getCurrentUserProfile(Long userId)" /home/boutchouang-nathan/SpringbootProjects/k48/gestionNotes_frontend_backend/API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/service/impl/AuthServiceImpl.java | grep "ResourceNotFoundException"
# Assert: Found exception handling

# Compile backend
cd /home/boutchouang-nathan/SpringbootProjects/k48/gestionNotes_frontend_backend/API_GestionNotes/ManageNotes
mvn compile -q
# Assert: Exit code 0
```

**Evidence to Capture**:
- Grep output showing method implementation
- Maven compile success

**Commit**: NO (groups with Wave 1 into Task 9)

---

### Task 5: Fix AuthServiceImpl.getAdminResponse() Mapper

**What to do**:
- Open `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/service/impl/AuthServiceImpl.java`
- Locate `getAdminResponse(Users admin)` method (around lines 248-256)
- Add missing field mappings:
  - `userResponse.setUserId(admin.getId());` - Set userId from entity ID
  - `userResponse.setCreatedDate(admin.getCreatedDate());` - Set creation timestamp
  - `userResponse.setLastModifiedDate(admin.getLastModifiedDate());` - Set modification timestamp
  - `userResponse.setRoleName(admin.getRole().getAppRole().name());` - Extract role string
- Ensure all fields in `UserResponse` DTO are populated

**Must NOT do**:
- Do NOT remove existing field mappings
- Do NOT change method signature (breaks existing callers)
- Do NOT set role field to anything other than the entity (it's annotated with @JsonIgnore now)

**Recommended Agent Profile**:
- **Category**: `quick`
  - Reason: Simple mapper fix, add missing setters
- **Skills**: []
  - Reason: Standard Java getter/setter calls, no specialized knowledge
- **Skills Evaluated but Omitted**:
  - All skills omitted - trivial setter additions

**Parallelization**:
- **Can Run In Parallel**: YES
- **Parallel Group**: Wave 1 (with Tasks 1, 2, 3, 6)
- **Blocks**: Task 7 (backend build)
- **Blocked By**: None (can start immediately)

**References**:

**Pattern References** (existing code to follow):
- `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/service/impl/AuthServiceImpl.java:248-256` - Existing incomplete mapper method to fix
- Other mapper methods in service layer - Standard entity-to-DTO mapping patterns

**Type References**:
- `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/entity/Users.java` - Source entity with `getId()`, `getCreatedDate()`, `getLastModifiedDate()`, `getRole()` methods
- `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/dto/response/UserResponse.java` - Target DTO with setter methods
- `API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/entity/Roles.java` - Roles entity with `getAppRole()` returning enum

**WHY Each Reference Matters**:
- Users entity shows available getter methods to extract data
- UserResponse shows setter methods to populate (must populate ALL fields)
- Roles entity understanding critical to extract string role name via `getAppRole().name()`

**Acceptance Criteria**:

**Automated Verification** (using Bash + Maven):
```bash
# Verify mapper includes new setters
grep -A 15 "private static UserResponse getAdminResponse" /home/boutchouang-nathan/SpringbootProjects/k48/gestionNotes_frontend_backend/API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/service/impl/AuthServiceImpl.java | grep "setUserId"
# Assert: Found userId setter

grep -A 15 "private static UserResponse getAdminResponse" /home/boutchouang-nathan/SpringbootProjects/k48/gestionNotes_frontend_backend/API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/service/impl/AuthServiceImpl.java | grep "setCreatedDate"
# Assert: Found createdDate setter

grep -A 15 "private static UserResponse getAdminResponse" /home/boutchouang-nathan/SpringbootProjects/k48/gestionNotes_frontend_backend/API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/service/impl/AuthServiceImpl.java | grep "setRoleName"
# Assert: Found roleName setter

# Compile backend
cd /home/boutchouang-nathan/SpringbootProjects/k48/gestionNotes_frontend_backend/API_GestionNotes/ManageNotes
mvn compile -q
# Assert: Exit code 0
```

**Evidence to Capture**:
- Grep output showing new setters added
- Maven compile success

**Commit**: NO (groups with Wave 1 into Task 9)

---

### Task 6: Fix Frontend Register Endpoint Path

**What to do**:
- Open `react/src/features/auth/api/authApi.ts`
- Locate the `register` mutation (should be around line with `url: 'auth/register'`)
- Change URL from `'auth/register'` to `'auth/admin/register'`
- Verify no other references to old path exist

**Must NOT do**:
- Do NOT change any other endpoint paths
- Do NOT modify request/response types
- Do NOT change method from POST
- Do NOT alter mutation configuration

**Recommended Agent Profile**:
- **Category**: `quick`
  - Reason: Single line string change in config file
- **Skills**: [`typescript-programmer`]
  - `typescript-programmer`: Ensures TypeScript syntax correctness and type safety validation
- **Skills Evaluated but Omitted**:
  - `frontend-ui-ux`: Not applicable (config change, not UI)
  - `git-master`: Not needed yet (commit in Task 9)
  - `agent-browser`: Not needed (no browser testing required)

**Parallelization**:
- **Can Run In Parallel**: YES
- **Parallel Group**: Wave 1 (with Tasks 1, 2, 3, 4, 5)
- **Blocks**: Task 8 (frontend build)
- **Blocked By**: None (can start immediately)

**References**:

**Pattern References** (existing code to follow):
- `react/src/features/auth/api/authApi.ts:login` mutation - Shows correct endpoint path pattern (how RTK Query endpoints are configured)
- Other mutations in same file - Consistent URL path structure

**API/Type References**:
- `react/src/features/auth/types/dtos.ts` - RegisterReqDto type definition (verify mutation still matches type)

**External References**:
- RTK Query mutation docs: https://redux-toolkit.js.org/rtk-query/usage/mutations - Mutation configuration structure

**WHY Each Reference Matters**:
- Login mutation shows the working pattern: `url: 'auth/login'` (register should follow same structure)
- Backend AuthController defines the actual endpoint path: `/api/auth/admin/register`
- RTK Query base URL is `http://localhost:3030/api`, so mutation URL should be relative: `auth/admin/register`

**Acceptance Criteria**:

**Automated Verification** (using Bash + grep):
```bash
# Verify old path removed
! grep -n "url: 'auth/register'" /home/boutchouang-nathan/SpringbootProjects/k48/gestionNotes_frontend_backend/react/src/features/auth/api/authApi.ts
# Assert: Exit code 0 (NOT found - negation with !)

# Verify new path exists
grep -n "url: 'auth/admin/register'" /home/boutchouang-nathan/SpringbootProjects/k48/gestionNotes_frontend_backend/react/src/features/auth/api/authApi.ts
# Assert: Found new path

# Build frontend to verify TypeScript compilation
cd /home/boutchouang-nathan/SpringbootProjects/k48/gestionNotes_frontend_backend/react
npm run build
# Assert: Exit code 0
# Assert: No TypeScript errors
```

**Evidence to Capture**:
- Grep output showing path change
- npm build success output

**Commit**: NO (groups with Wave 1 into Task 9)

---

### Task 7: Build and Verify Backend

**What to do**:
- Navigate to backend directory: `cd API_GestionNotes/ManageNotes`
- Run Maven compile: `mvn compile`
- Verify BUILD SUCCESS message appears
- Check for any compilation errors or warnings
- Verify all Java files compile cleanly

**Must NOT do**:
- Do NOT skip compilation (must verify before commit)
- Do NOT ignore compilation warnings (address if critical)
- Do NOT proceed to Task 9 if build fails

**Recommended Agent Profile**:
- **Category**: `quick`
  - Reason: Single command execution, no code changes
- **Skills**: []
  - Reason: Standard Maven build, no specialized knowledge needed
- **Skills Evaluated but Omitted**:
  - All skills omitted - just running a build command

**Parallelization**:
- **Can Run In Parallel**: YES (with Task 8)
- **Parallel Group**: Wave 2 (with Task 8)
- **Blocks**: Task 9 (commit only if both builds pass)
- **Blocked By**: Tasks 1, 2, 3, 4, 5 (all backend changes must complete first)

**References**:

**Documentation References**:
- Maven lifecycle: https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html - Understanding compile phase
- Project README build instructions: `/home/boutchouang-nathan/SpringbootProjects/k48/gestionNotes_frontend_backend/README.md` - Standard build commands

**WHY Each Reference Matters**:
- Maven compile phase ensures all Java source files compile to bytecode without errors
- Project README confirms `mvn compile` is the standard verification command

**Acceptance Criteria**:

**Automated Verification** (using Bash):
```bash
# Build backend
cd /home/boutchouang-nathan/SpringbootProjects/k48/gestionNotes_frontend_backend/API_GestionNotes/ManageNotes
mvn compile
# Assert: Exit code 0
# Assert: Output contains "BUILD SUCCESS"
# Assert: No "COMPILATION ERROR" in output

# Verify ProfileController compiled
test -f target/classes/com/university/ManageNotes/controller/ProfileController.class
echo $?
# Assert: Exit code 0 (class file exists)
```

**Evidence to Capture**:
- Terminal output showing "BUILD SUCCESS"
- Exit code 0 confirmation
- Compiled .class file verification

**Commit**: NO (groups with Wave 2 into Task 9)

---

### Task 8: Build and Verify Frontend

**What to do**:
- Navigate to frontend directory: `cd react`
- Run npm build: `npm run build`
- Verify exit code 0 (successful build)
- Check for TypeScript compilation errors
- Verify dist folder created with compiled assets

**Must NOT do**:
- Do NOT skip build verification
- Do NOT ignore TypeScript errors (fix if any appear)
- Do NOT proceed to Task 9 if build fails

**Recommended Agent Profile**:
- **Category**: `quick`
  - Reason: Single command execution, verification only
- **Skills**: [`typescript-programmer`]
  - `typescript-programmer`: Can diagnose and fix any TypeScript compilation errors that might appear
- **Skills Evaluated but Omitted**:
  - `frontend-ui-ux`: Not needed (build verification, not UI changes)
  - `git-master`: Not needed yet (commit in Task 9)

**Parallelization**:
- **Can Run In Parallel**: YES (with Task 7)
- **Parallel Group**: Wave 2 (with Task 7)
- **Blocks**: Task 9 (commit only if both builds pass)
- **Blocked By**: Task 6 (frontend path fix must complete first)

**References**:

**Pattern References**:
- `react/package.json:scripts.build` - The build command definition (`tsc -b && vite build`)

**Documentation References**:
- Vite build docs: https://vitejs.dev/guide/build.html - Understanding production build process
- TypeScript compiler: https://www.typescriptlang.org/docs/handbook/compiler-options.html - Compilation flags and error handling

**WHY Each Reference Matters**:
- package.json shows the actual build command being executed (TypeScript compile + Vite bundling)
- Build success confirms no breaking changes to frontend TypeScript code

**Acceptance Criteria**:

**Automated Verification** (using Bash):
```bash
# Build frontend
cd /home/boutchouang-nathan/SpringbootProjects/k48/gestionNotes_frontend_backend/react
npm run build
# Assert: Exit code 0
# Assert: No "error TS" TypeScript errors in output
# Assert: Output contains "vite build" success message

# Verify dist folder created
test -d dist
echo $?
# Assert: Exit code 0 (dist directory exists)

# Verify compiled JavaScript exists
test -f dist/index.html
echo $?
# Assert: Exit code 0 (entry point created)
```

**Evidence to Capture**:
- Terminal output showing build success
- Exit code 0 confirmation
- Dist folder verification

**Commit**: NO (groups with Wave 2 into Task 9)

---

### Task 9: Git Commit All Changes

**What to do**:
- Verify both builds passed (Tasks 7 and 8 completed successfully)
- Stage all modified and new files:
  - `git add API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/controller/ProfileController.java` (new)
  - `git add API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/dto/response/UserResponse.java` (modified)
  - `git add API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/service/AuthService.java` (modified)
  - `git add API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/service/impl/AuthServiceImpl.java` (modified)
  - `git add react/src/features/auth/api/authApi.ts` (modified)
- Commit with message: `fix: add unified /api/me endpoint and fix register path`
- Verify commit created successfully

**Must NOT do**:
- Do NOT commit if either build failed
- Do NOT change commit message (user specified exact message)
- Do NOT commit unrelated changes
- Do NOT skip verification of staged files

**Recommended Agent Profile**:
- **Category**: `quick`
  - Reason: Standard git commit operation
- **Skills**: [`git-master`]
  - `git-master`: Ensures atomic commit best practices, proper staging, and clean commit message formatting
- **Skills Evaluated but Omitted**:
  - Other skills not applicable to git operations

**Parallelization**:
- **Can Run In Parallel**: NO
- **Parallel Group**: Wave 3 (Sequential - must run alone)
- **Blocks**: None (final task)
- **Blocked By**: Tasks 7, 8 (both builds must pass)

**References**:

**Pattern References**:
- Git commit conventions in this project (check existing commit history for patterns)

**Documentation References**:
- Conventional Commits: https://www.conventionalcommits.org/ - Commit message format (fix: prefix for bug fixes)
- Git add docs: https://git-scm.com/docs/git-add - Staging files for commit

**WHY Each Reference Matters**:
- User specified exact commit message, must use verbatim
- Atomic commit groups all related changes together (backend + frontend fixes)
- Staging only relevant files prevents committing unrelated changes

**Acceptance Criteria**:

**Automated Verification** (using Bash + git):
```bash
# Verify no uncommitted changes remain (except possibly untracked files)
cd /home/boutchouang-nathan/SpringbootProjects/k48/gestionNotes_frontend_backend
git status --short | grep -E "^M|^A" | wc -l
# Assert: Output is 0 (all changes committed)

# Verify commit message matches
git log -1 --pretty=%B
# Assert: Output is "fix: add unified /api/me endpoint and fix register path"

# Verify commit includes expected files
git show --stat --oneline | grep -E "ProfileController.java|UserResponse.java|AuthService.java|AuthServiceImpl.java|authApi.ts"
# Assert: All 5 files appear in commit

# Verify commit SHA created
git log -1 --pretty=%H
# Assert: Returns 40-character SHA hash
```

**Evidence to Capture**:
- Git status showing clean working tree
- Git log showing commit message
- Git show output listing changed files
- Commit SHA hash

**Commit**: YES - This IS the commit task
- Message: `fix: add unified /api/me endpoint and fix register path`
- Files: All files modified in Wave 1
- Pre-commit: Tasks 7 and 8 (both builds must pass)

---

## Commit Strategy

| After Task | Message | Files | Verification |
|------------|---------|-------|--------------|
| 9 (Wave 3) | `fix: add unified /api/me endpoint and fix register path` | ProfileController.java, UserResponse.java, AuthService.java, AuthServiceImpl.java, authApi.ts | mvn compile && npm run build (both must pass) |

**Note**: Single atomic commit after ALL changes complete and builds verified (Tasks 7-8).

---

## Success Criteria

### Verification Commands
```bash
# Backend build verification
cd /home/boutchouang-nathan/SpringbootProjects/k48/gestionNotes_frontend_backend/API_GestionNotes/ManageNotes
mvn compile
# Expected: BUILD SUCCESS

# Frontend build verification
cd /home/boutchouang-nathan/SpringbootProjects/k48/gestionNotes_frontend_backend/react
npm run build
# Expected: Exit code 0, no TypeScript errors

# Manual API test (after mvn spring-boot:run)
TOKEN=$(curl -s -X POST http://localhost:3030/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' \
  | jq -r '.token')

curl -s http://localhost:3030/api/me \
  -H "Authorization: Bearer $TOKEN" \
  | jq .
# Expected: JSON with "id" field (not "userId"), "role" as string
```

### Final Checklist
- [ ] All "Must Have" present:
  - ProfileController created with correct pattern
  - @AuthenticationPrincipal UserDetailsImpl used (not Authentication)
  - new ResponseEntity<>() pattern used (not ResponseEntity.ok())
  - @Operation annotation present for Swagger
  - Service method implemented with proper error handling
  - DTO fields serialize correctly for frontend
- [ ] All "Must NOT Have" absent:
  - No SecurityContextHolder usage in controller
  - No try-catch blocks in controller
  - No ResponseEntity.ok() shortcuts
  - No Roles entity serialized directly
  - No frontend interface modifications
- [ ] Backend compiles: `mvn compile` → BUILD SUCCESS
- [ ] Frontend builds: `npm run build` → Exit 0
- [ ] Commit created with exact message: `fix: add unified /api/me endpoint and fix register path`
- [ ] Git history clean and atomic (all changes in one commit)
