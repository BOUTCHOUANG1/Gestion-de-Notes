# Draft: Fix Authentication Flow Issues

## Requirements (confirmed from user)

**Core Objective**: Fix critical authentication flow by adding unified `/api/me` endpoint and fixing frontend register path.

**Specific Tasks**:
1. Create `ProfileController.java` with `GET /api/me` endpoint returning current user profile based on JWT
2. Fix frontend register path from `'auth/register'` to `'auth/admin/register'`
3. Build both backend (mvn compile) and frontend (npm run build)
4. Commit with message: `'fix: add unified /api/me endpoint and fix register path'`

## Technical Context

**Authentication Flow**:
- JWT extracted by `AuthTokenFilter` from `Authorization: Bearer <token>` header
- `UserDetailsImpl` loaded and set in `SecurityContextHolder`
- Controllers access via `@AuthenticationPrincipal UserDetailsImpl userPrincipal`

**DTO Mismatch Identified**:
- Backend `UserResponse`: uses `userId` field and `Roles` object
- Frontend `userProfileResDto`: expects `id` field and `role` string
- **Decision needed**: Fix backend DTO, fix frontend interface, or add mapper?

**Existing Mapper Bug**:
- `AuthServiceImpl` has mapper that needs fixing for the DTO mismatch

## Technical Decisions

**Controller Pattern** (from user constraints):
- Use `@AuthenticationPrincipal UserDetailsImpl` (NOT `Authentication`)
- Use `new ResponseEntity<>(data, HttpStatus.OK)` pattern
- Add `@Operation` annotation for Swagger
- Return `UserResponse` DTO

**Build Strategy**:
- Backend: `mvn compile`
- Frontend: `npm run build`
- Both must succeed before commit

## Technical Decisions (CONFIRMED)

### DTO Field Mismatch Strategy ✅
**Decision**: **Option A - Backend matches frontend using @JsonProperty**

**Implementation**:
- Modify `UserResponse.java`:
  - Keep Java field name as `userId` 
  - Add `@JsonProperty("id")` to serialize as "id" in JSON
  - Add `@JsonIgnore` to `Roles role` field
  - Add `@JsonProperty("role")` custom getter returning `role.getAppRole().name()`
- Benefits: Zero frontend changes, maintains Java conventions, safe (only 1 existing endpoint uses UserResponse)

### Frontend Files Located ✅
- **Auth API Slice**: `/react/src/features/auth/api/authApi.ts`
  - Register endpoint: `url: 'auth/register'` → needs fix to `'auth/admin/register'`
  - Profile endpoint: `query: () => 'me'` → calls `GET /api/me`
- **Profile DTO**: `/react/src/api/reponse-dto/user.res.dto.ts`
  - Expects: `{ id, firstName, lastName, email, username, role }`
- **Build Command**: `npm run build` (runs `tsc -b && vite build`)

### Service Layer Strategy
**Pending**: Need to see existing AuthService to determine if ProfileController should:
- Reuse `AuthService.getAdminResponse()` pattern
- Create new service method
- Directly map `UserDetailsImpl` to `UserResponse` in controller

## Open Questions

1. **Backend Structure**: Waiting for controller/service patterns (bg_6428ec14)
2. **Test Strategy**: Are there existing tests that need updates?

## Scope Boundaries

**INCLUDE**:
- New ProfileController with /api/me endpoint
- Frontend register path fix
- DTO field mismatch resolution
- Build verification
- Single atomic commit

**EXCLUDE**:
- Changes to existing endpoints
- Security configuration changes
- Database schema changes
- Test additions (unless existing tests break)

## Research in Progress

Waiting for agents:
- bg_6428ec14: Backend structure exploration
- bg_f3e7b821: Frontend API configuration
- bg_5e600011: Spring Boot best practices
