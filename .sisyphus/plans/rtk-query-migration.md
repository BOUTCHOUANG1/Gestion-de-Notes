# RTK Query Migration Plan

## TL;DR

> **Quick Summary**: Migrate from Axios + createAsyncThunk to RTK Query for all API calls, with JWT authentication, automatic token injection, centralized error handling, and cache invalidation. Replace 4 existing thunks with RTK Query endpoints while preserving existing Redux slices.
> 
> **Deliverables**:
> - `react/src/store/api/baseQuery.ts` - Base query with JWT + error handling
> - `react/src/store/api/apiSlice.ts` - Main API slice with tag types
> - `react/src/features/auth/api/authApi.ts` - Auth endpoints (login/register/profile)
> - `react/src/features/students/api/studentsApi.ts` - Students CRUD endpoints
> - `react/src/store/index.ts` - Updated with RTK Query middleware + RESET preservation
> - Successful build with 0 TypeScript errors
> 
> **Estimated Effort**: Medium (5 new files, 1 modification, ~250 lines total)
> **Parallel Execution**: YES - 3 waves
> **Critical Path**: Wave 1 (baseQuery + apiSlice) → Wave 2 (auth/students APIs) → Wave 3 (store integration + verification)

---

## Context

### Original Request
User requested detailed RTK Query implementation plan to replace Axios + createAsyncThunk pattern with RTK Query's modern data fetching approach. The migration includes:
1. JWT token injection in all API calls
2. Centralized error handling with notification dispatch
3. Cache invalidation with tag-based system
4. Auth flow with automatic token storage
5. Preserve existing Redux state management (RESET action must keep RTK cache)

### Background Research Completed
User has already completed 5 comprehensive research agents:
1. **RTK Query Official Docs** - Auth patterns, token injection, cache invalidation, optimistic updates
2. **GitHub Production Examples** - LessPass (mutex pattern), OAuth Boilerplate (cookies), MusicFun (full token flow)
3. **RTK Query CRUD Patterns** - University grade system examples with tag-based invalidation
4. **Existing API Architecture** - 4 axios calls to replace: `POST auth/login`, `POST auth/register`, `GET me`, `GET students`
5. **Redux Store Structure** - configureStore with 4 slices, RESET action clears state on logout

### Current Architecture
**Redux Store** (`react/src/store/index.ts`):
```typescript
{
  navigation: { path: string | null },
  auth: { isAuthenticated: boolean | null, tokenExpiresIn: number },
  notification: { notification: NotificationType | null, source: 'CLIENT' | 'SERVER' },
  user: { profile: userProfileResDto, students: studentResDto[] }
}
```

**Thunks to Replace**:
- `processLogin` - Calls authService.login(), stores token, dispatches markAsAuthenticated + navigateTo
- `processSignOut` - Clears token, dispatches RESET
- `fetchUserProfile` - Calls userService.getProfile(), dispatches loadUserProfile
- `fetchStudents` - Calls userService.getStudents(), returns data

**Token Management** (`react/src/api/services/token.service.ts`):
- `getToken()` - Retrieves from sessionStorage or localStorage
- `setTokens({ token })` - Stores in both storages
- `clearTokens()` - Removes from both storages

**Error Handling** (notification slice):
- `triggerServerNotification(payload)` - Server-side errors (HTTP 4xx/5xx)
- `triggerClientNotification(payload)` - Client-side errors (network, validation)

### Gap Analysis

**Identified Gaps** (addressed in plan):

1. **RTK Query Package Installation**
   - Status: @reduxjs/toolkit@2.8.2 already installed (includes RTK Query built-in)
   - Resolution: No installation needed, RTK Query exports from '@reduxjs/toolkit/query'

2. **TypeScript Type Definitions**
   - Gap: User provided DTO names but not actual type definitions
   - Resolution: Plan includes explicit imports with placeholder note - executor must verify actual paths

3. **API Base URL Configuration**
   - Gap: Hardcoded vs environment variable decision
   - Resolution: Default to `http://localhost:3030/api`, documented as TODO for production

4. **RESET Action Scope Creep Risk**
   - Gap: Current RESET clears ALL state (`state = {}`), which would wipe RTK Query cache
   - Resolution: Explicit guardrail - modify RESET to preserve `api` reducer path

5. **Error Message Extraction Pattern**
   - Gap: No specification for how to extract error messages from API responses
   - Resolution: Implemented generic pattern with fallback: `error?.data?.message || error?.data?.error || 'An error occurred'`

6. **Cache Invalidation Strategy**
   - Gap: No specification for when to invalidate vs optimistic update
   - Resolution: Simple invalidation-only approach (no optimistic updates for V1)

7. **Logout Endpoint Missing**
   - Gap: User mentioned `processSignOut` thunk but no backend `/auth/logout` endpoint
   - Resolution: Clarified that logout is client-only (clearTokens + RESET dispatch)

---

## Work Objectives

### Core Objective
Replace all Axios-based API calls with RTK Query endpoints, implementing JWT authentication, automatic token injection, centralized error handling, and tag-based cache invalidation while preserving existing Redux slice functionality.

### Concrete Deliverables
- `react/src/store/api/baseQuery.ts` - Exports `baseQueryWithAuth` (fetchBaseQuery wrapper)
- `react/src/store/api/apiSlice.ts` - Exports `api` (createApi instance with tagTypes)
- `react/src/features/auth/api/authApi.ts` - Exports `useLoginMutation`, `useRegisterMutation`, `useGetProfileQuery`
- `react/src/features/students/api/studentsApi.ts` - Exports `useGetStudentsQuery`
- `react/src/store/index.ts` - Modified to include RTK Query reducer + middleware + setupListeners
- Build output: `npm run build` completes with 0 errors

### Definition of Done
- [ ] All 4 new API files created with proper exports
- [ ] Store updated with RTK Query integration
- [ ] RESET action preserves RTK Query cache
- [ ] Build succeeds: `npm run build` exits with code 0
- [ ] Git commit created with message: `feat(api): migrate to RTK Query from Axios`
- [ ] No TypeScript compilation errors
- [ ] All RTK Query hooks exported and ready for component consumption

### Must Have
- JWT token automatic injection via `prepareHeaders`
- Error responses dispatch to notification slice
- Auth mutations call `setTokens` and dispatch `markAsAuthenticated` in `onQueryStarted`
- Tag-based cache invalidation (`Auth`, `User`, `Students` tags)
- RESET action modified to preserve `[api.reducerPath]` in state
- setupListeners for refetchOnFocus/refetchOnReconnect

### Must NOT Have (Guardrails)
- ❌ **No optimistic updates** - V1 uses simple invalidation only (avoid premature complexity)
- ❌ **No component refactoring** - Keep existing thunk calls in components (separate migration task)
- ❌ **No removal of old Axios services** - Leave deprecated files in place until verified working
- ❌ **No custom retry logic** - Use RTK Query defaults (avoid over-engineering)
- ❌ **No transformation layers** - Return API responses as-is (no selectFromResult yet)
- ❌ **No WebSocket integration** - HTTP only (avoid scope creep)
- ❌ **No request debouncing** - RTK Query handles deduplication automatically
- ❌ **No hard-coded credentials** - Never put test tokens/passwords in code

---

## Verification Strategy

### Test Decision
- **Infrastructure exists**: NO
- **User wants tests**: Manual-only (no test setup for this task)
- **Framework**: none
- **Verification approach**: Build success + manual verification procedures

### Automated Verification Only (NO User Intervention)

Each TODO includes EXECUTABLE verification procedures that agents can run directly:

**Build Verification** (using Bash):
```bash
# Agent runs from react/ directory:
npm run build
# Assert: Exit code 0
# Assert: Output contains "built in" (successful Vite build)
# Assert: No "error TS" messages in output
```

**Type Check Verification** (using Bash):
```bash
# Agent runs from react/ directory:
npx tsc --noEmit
# Assert: Exit code 0
# Assert: No "error TS" in output
```

**Import Verification** (using Bash node):
```bash
# Agent verifies exports are accessible:
cd react/
node -e "
import('./src/store/api/apiSlice.ts').then(m => {
  console.log('apiSlice exports:', Object.keys(m));
}).catch(e => console.error('Import failed:', e.message));
"
# Assert: Output contains "api"
```

**Evidence to Capture:**
- [ ] Terminal output from `npm run build` showing success
- [ ] Terminal output from `npx tsc --noEmit` showing 0 errors
- [ ] Git commit SHA after successful commit

---

## Execution Strategy

### Parallel Execution Waves

> Maximize throughput by grouping independent tasks into parallel waves.
> Each wave completes before the next begins.

```
Wave 1 (Start Immediately - Foundation):
├── Task 1: Create baseQuery.ts [no dependencies]
└── Task 2: Create apiSlice.ts [no dependencies]

Wave 2 (After Wave 1 - API Endpoints):
├── Task 3: Create authApi.ts [depends: 1, 2]
└── Task 4: Create studentsApi.ts [depends: 1, 2]

Wave 3 (After Wave 2 - Integration):
├── Task 5: Update store/index.ts [depends: 2, 3, 4]
├── Task 6: Verify build [depends: 5]
└── Task 7: Git commit [depends: 6]

Critical Path: Task 2 → Task 3 → Task 5 → Task 6 → Task 7
Parallel Speedup: ~30% faster than sequential (Wave 1 + Wave 2 parallelization)
```

### Dependency Matrix

| Task | Depends On | Blocks | Can Parallelize With |
|------|------------|--------|---------------------|
| 1. baseQuery.ts | None | 3, 4 | 2 |
| 2. apiSlice.ts | None | 3, 4, 5 | 1 |
| 3. authApi.ts | 1, 2 | 5 | 4 |
| 4. studentsApi.ts | 1, 2 | 5 | 3 |
| 5. Update store | 2, 3, 4 | 6 | None |
| 6. Verify build | 5 | 7 | None |
| 7. Git commit | 6 | None | None |

### Agent Dispatch Summary

| Wave | Tasks | Recommended Agents | Run in Background |
|------|-------|-------------------|-------------------|
| 1 | 1, 2 | `category="quick"` (simple file creation) | YES (parallel) |
| 2 | 3, 4 | `category="unspecified-low"` (moderate logic) | YES (parallel) |
| 3 | 5, 6, 7 | `category="unspecified-low"` (integration + verification) | NO (sequential) |

---

## Task Dependency Graph

| Task | Depends On | Reason |
|------|------------|--------|
| Task 1: baseQuery.ts | None | Foundation - standalone implementation |
| Task 2: apiSlice.ts | None | Foundation - standalone implementation |
| Task 3: authApi.ts | Task 1, Task 2 | Imports `baseQueryWithAuth` and `api` |
| Task 4: studentsApi.ts | Task 1, Task 2 | Imports `api` (not baseQuery directly) |
| Task 5: Update store/index.ts | Task 2, Task 3, Task 4 | Imports `api` reducer and middleware |
| Task 6: Verify build | Task 5 | Build must include all new code |
| Task 7: Git commit | Task 6 | Only commit after verification passes |

---

## Parallel Execution Graph

```
Wave 1 (Start immediately):
├── Task 1: Create baseQuery.ts with JWT injection + error handling (no dependencies)
└── Task 2: Create apiSlice.ts with tagTypes configuration (no dependencies)

Wave 2 (After Wave 1 completes):
├── Task 3: Create authApi.ts with login/register/profile endpoints (depends: Task 1, Task 2)
└── Task 4: Create studentsApi.ts with CRUD endpoints (depends: Task 2)

Wave 3 (After Wave 2 completes):
└── Task 5: Update store/index.ts to integrate RTK Query (depends: Task 2, Task 3, Task 4)
└── Task 6: Verify build with npm run build (depends: Task 5)
└── Task 7: Create git commit (depends: Task 6)

Critical Path: Task 2 → Task 3 → Task 5 → Task 6 → Task 7
Estimated Parallel Speedup: 30% faster than sequential execution
```

---

## TODOs

### Task 1: Create baseQuery.ts - Base Query with JWT + Error Handling

**What to do**:
- Create `react/src/store/api/baseQuery.ts`
- Import `fetchBaseQuery`, `FetchArgs`, `FetchBaseQueryError`, `BaseQueryFn` from `@reduxjs/toolkit/query`
- Import `getToken` from `../../api/services/token.service`
- Import `triggerServerNotification`, `triggerClientNotification` from `../../contexts/notification/slice`
- Create `baseQuery` using `fetchBaseQuery({ baseUrl: 'http://localhost:3030/api', prepareHeaders })`
- Implement `prepareHeaders` to inject JWT token from `getToken()` into `Authorization: Bearer {token}` header
- Create `baseQueryWithAuth` wrapper that calls `baseQuery` and dispatches error notifications
- Error handling logic:
  - If `result.error.status === 'FETCH_ERROR'` → dispatch `triggerClientNotification({ message: 'Network error', type: 'error' })`
  - If `result.error.status >= 400 && result.error.status <= 499` → dispatch `triggerServerNotification({ description: extractedMessage, type: 'error' })`
  - Extract message: `result.error.data?.message || result.error.data?.error || 'An error occurred'`
- Export `baseQueryWithAuth` as default export

**Must NOT do**:
- ❌ Do NOT add retry logic (use defaults)
- ❌ Do NOT add request/response interceptors beyond error handling
- ❌ Do NOT hard-code the base URL in multiple places (single source of truth)
- ❌ Do NOT add authentication refresh logic yet (out of scope for V1)

**Delegation Recommendation**:
- **Category**: `quick` - Simple file creation with straightforward wrapper pattern (~50 lines)
- **Skills**: None needed - standard RTK Query pattern from official docs
- **Reason**: This is boilerplate code following documented patterns, no complex logic or UI considerations

**Skills Evaluation**:
- ✅ NONE INCLUDED: Standard TypeScript file creation, no specialized domain knowledge required
- ❌ OMITTED `typescript-programmer`: Overkill for simple wrapper pattern
- ❌ OMITTED `frontend-ui-ux`: No UI/UX considerations in API layer

**Parallelization**:
- **Can Run In Parallel**: YES
- **Parallel Group**: Wave 1 (with Task 2)
- **Blocks**: Task 3 (authApi), Task 4 (studentsApi)
- **Blocked By**: None (starting point)

**References**:

**Pattern References**:
- Research doc: RTK Query Official Docs - Base query pattern with `prepareHeaders` and error handling wrapper
- Research doc: GitHub Production Examples - LessPass mutex pattern for token injection
- `react/src/api/services/token.service.ts` - `getToken()` function signature and usage
- `react/src/contexts/notification/slice.ts:22-37` - `triggerServerNotification` and `triggerClientNotification` action creators

**Type References**:
- `@reduxjs/toolkit/query` exports: `fetchBaseQuery`, `BaseQueryFn`, `FetchArgs`, `FetchBaseQueryError`
- `react/src/contexts/notification/context.ts` - `NotificationType` interface (for payload type)

**Documentation References**:
- RTK Query docs: https://redux-toolkit.js.org/rtk-query/api/fetchBaseQuery - prepareHeaders usage
- RTK Query docs: https://redux-toolkit.js.org/rtk-query/usage/error-handling - BaseQueryFn error handling pattern

**WHY Each Reference Matters**:
- `token.service.ts`: Provides the `getToken()` function that baseQuery must call in prepareHeaders
- `notification/slice.ts`: Defines the exact action creators that error handler must dispatch
- Official docs: Show the exact TypeScript signature for BaseQueryFn wrapper pattern

**Acceptance Criteria**:

**Automated Verification** (using Bash):
```bash
# Agent runs from react/ directory:
npx tsc --noEmit
# Assert: Exit code 0
# Assert: No "error TS" messages related to baseQuery.ts

# Verify exports are accessible:
grep -q "export.*baseQueryWithAuth" src/store/api/baseQuery.ts
# Assert: Exit code 0 (export statement exists)
```

**Evidence to Capture**:
- [ ] Terminal output from `npx tsc --noEmit` showing 0 errors
- [ ] File content: `cat src/store/api/baseQuery.ts | head -20` (first 20 lines showing imports and structure)

**Commit**: NO (grouped with Task 7)

---

### Task 2: Create apiSlice.ts - Main API with Tag Types

**What to do**:
- Create `react/src/store/api/apiSlice.ts`
- Import `createApi` from `@reduxjs/toolkit/query`
- Import `baseQueryWithAuth` from `./baseQuery`
- Create API using `createApi` with:
  - `reducerPath: 'api'`
  - `baseQuery: baseQueryWithAuth`
  - `tagTypes: ['Auth', 'User', 'Students', 'Grades']` (future-proof for grades)
  - `endpoints: () => ({})` (empty - injected by feature APIs)
- Export `api` as default export
- Export `api.reducerPath` and `api.reducer` and `api.middleware` as named exports (for store integration)

**Must NOT do**:
- ❌ Do NOT define any endpoints here (endpoints belong in feature-specific files)
- ❌ Do NOT add more than 4-5 tag types (avoid premature tag explosion)
- ❌ Do NOT configure refetchOnMountOrArgChange here (use defaults)
- ❌ Do NOT add keepUnusedDataFor config yet (use default 60s)

**Delegation Recommendation**:
- **Category**: `quick` - Minimal configuration file (~15 lines)
- **Skills**: None needed - standard RTK Query setup
- **Reason**: Pure configuration, no logic, follows documented createApi pattern

**Skills Evaluation**:
- ✅ NONE INCLUDED: Trivial configuration file
- ❌ OMITTED all skills: No specialized knowledge required for createApi boilerplate

**Parallelization**:
- **Can Run In Parallel**: YES
- **Parallel Group**: Wave 1 (with Task 1)
- **Blocks**: Task 3 (authApi), Task 4 (studentsApi), Task 5 (store integration)
- **Blocked By**: None (starting point)

**References**:

**Pattern References**:
- Research doc: RTK Query Official Docs - createApi pattern with tagTypes
- Research doc: RTK Query CRUD Patterns - Tag-based invalidation examples

**Type References**:
- `@reduxjs/toolkit/query` exports: `createApi`

**Documentation References**:
- RTK Query docs: https://redux-toolkit.js.org/rtk-query/api/createApi - Full createApi options
- RTK Query docs: https://redux-toolkit.js.org/rtk-query/usage/automated-refetching - Tag types and invalidation

**WHY Each Reference Matters**:
- Official docs: Show the minimal createApi setup required for tag-based invalidation
- CRUD patterns research: Demonstrate how tagTypes array enables cross-endpoint invalidation

**Acceptance Criteria**:

**Automated Verification** (using Bash):
```bash
# Agent runs from react/ directory:
npx tsc --noEmit
# Assert: Exit code 0
# Assert: No "error TS" messages related to apiSlice.ts

# Verify exports:
grep -q "export.*api" src/store/api/apiSlice.ts
# Assert: Exit code 0
```

**Evidence to Capture**:
- [ ] Terminal output from `npx tsc --noEmit` showing 0 errors
- [ ] File content: `cat src/store/api/apiSlice.ts` (entire file - should be ~15 lines)

**Commit**: NO (grouped with Task 7)

---

### Task 3: Create authApi.ts - Auth Endpoints (Login/Register/Profile)

**What to do**:
- Create `react/src/features/auth/api/authApi.ts`
- Import `api` from `../../../store/api/apiSlice`
- Import DTO types: Find and import `LoginreqDto`, `RegisterReqDto`, `LoginResDto`, `userProfileResDto` (likely from `src/api/dto` or `src/types`)
- Import token service: `setTokens` from `../../../api/services/token.service`
- Import Redux actions: `markAsAuthenticated` from `../slice` and `navigateTo` from `../../navigation/slice`
- Create `authApi` using `api.injectEndpoints({ endpoints: (builder) => ({ ... }) })`
- Define 3 endpoints:
  1. **login** - `builder.mutation<LoginResDto, LoginreqDto>`
     - `query: (credentials) => ({ url: 'auth/login', method: 'POST', body: credentials })`
     - `invalidatesTags: ['Auth', 'User']`
     - `onQueryStarted`: await queryFulfilled, call `setTokens({ token: data.token })`, dispatch `markAsAuthenticated()`, dispatch `navigateTo('/dashboard')`
  2. **register** - `builder.mutation<LoginResDto, RegisterReqDto>`
     - `query: (data) => ({ url: 'auth/register', method: 'POST', body: data })`
     - `invalidatesTags: ['Auth']`
     - `onQueryStarted`: same as login (register returns token too)
  3. **getProfile** - `builder.query<userProfileResDto, void>`
     - `query: () => 'me'`
     - `providesTags: [{ type: 'User', id: 'PROFILE' }]`
- Export generated hooks: `export const { useLoginMutation, useRegisterMutation, useGetProfileQuery } = authApi`

**Must NOT do**:
- ❌ Do NOT add password hashing/encryption client-side (backend responsibility)
- ❌ Do NOT add form validation logic (belongs in components)
- ❌ Do NOT store password in Redux state (security risk)
- ❌ Do NOT add token refresh logic yet (out of scope for V1)
- ❌ Do NOT add logout endpoint (logout is client-only: clearTokens + RESET)

**Delegation Recommendation**:
- **Category**: `unspecified-low` - Moderate complexity with onQueryStarted side effects (~80 lines)
- **Skills**: None needed - standard RTK Query mutation/query pattern
- **Reason**: Involves multiple imports and side effect handling, but follows documented patterns

**Skills Evaluation**:
- ✅ NONE INCLUDED: Well-documented RTK Query pattern
- ❌ OMITTED `typescript-programmer`: Pattern is straightforward from research docs
- ❌ OMITTED `frontend-ui-ux`: No UI logic in API layer

**Parallelization**:
- **Can Run In Parallel**: YES
- **Parallel Group**: Wave 2 (with Task 4)
- **Blocks**: Task 5 (store integration)
- **Blocked By**: Task 1 (baseQuery), Task 2 (apiSlice)

**References**:

**Pattern References**:
- Research doc: GitHub Production Examples - MusicFun full token flow with onQueryStarted
- Research doc: RTK Query Official Docs - Mutation with side effects pattern
- `react/src/features/auth/slice.ts:17-28` - `markAsAuthenticated` action (must dispatch this)
- `react/src/features/navigation/slice.ts` - `navigateTo` action (likely similar pattern)
- `react/src/api/services/token.service.ts:8-12` - `setTokens` function signature

**Type References**:
- Find DTO types in codebase (likely `src/api/dto` or `src/types`):
  - `LoginreqDto` (likely has `username: string, password: string`)
  - `RegisterReqDto` (likely extends LoginreqDto with `email: string, firstName: string, lastName: string`)
  - `LoginResDto` (likely has `token: string`)
  - `userProfileResDto` (mentioned in user slice, find actual path)

**API References**:
- Backend endpoints: `POST /api/auth/login`, `POST /api/auth/register`, `GET /api/me`

**Documentation References**:
- RTK Query docs: https://redux-toolkit.js.org/rtk-query/usage/mutations - onQueryStarted lifecycle

**WHY Each Reference Matters**:
- `auth/slice.ts`: Provides the exact action creator to dispatch after successful login
- `token.service.ts`: Shows the exact function signature for storing tokens
- MusicFun example: Demonstrates the complete onQueryStarted pattern with try/catch
- Navigation slice: Provides the navigateTo action for post-login redirect

**Acceptance Criteria**:

**Automated Verification** (using Bash):
```bash
# Agent runs from react/ directory:
npx tsc --noEmit
# Assert: Exit code 0
# Assert: No "error TS" messages related to authApi.ts

# Verify hook exports:
grep -q "useLoginMutation" src/features/auth/api/authApi.ts
grep -q "useRegisterMutation" src/features/auth/api/authApi.ts
grep -q "useGetProfileQuery" src/features/auth/api/authApi.ts
# Assert: All exit code 0
```

**Evidence to Capture**:
- [ ] Terminal output from `npx tsc --noEmit` showing 0 errors
- [ ] File content: `grep "export" src/features/auth/api/authApi.ts` (verify exports)

**Commit**: NO (grouped with Task 7)

---

### Task 4: Create studentsApi.ts - Students CRUD Endpoints

**What to do**:
- Create `react/src/features/students/api/studentsApi.ts`
- Import `api` from `../../../store/api/apiSlice`
- Import DTO type: Find and import `studentResDto` (mentioned in user slice, likely from `src/api/dto` or `src/types`)
- Create `studentsApi` using `api.injectEndpoints({ endpoints: (builder) => ({ ... }) })`
- Define 1 endpoint:
  1. **getStudents** - `builder.query<studentResDto[], void>`
     - `query: () => 'students'`
     - `providesTags: (result) => result ? [...result.map(({ id }) => ({ type: 'Students' as const, id })), { type: 'Students', id: 'LIST' }] : [{ type: 'Students', id: 'LIST' }]`
     - This provides both individual student tags and a LIST tag for efficient invalidation
- Export generated hooks: `export const { useGetStudentsQuery } = studentsApi`

**Must NOT do**:
- ❌ Do NOT add CREATE/UPDATE/DELETE student mutations yet (GET only for V1)
- ❌ Do NOT add pagination logic (simple list fetch)
- ❌ Do NOT add filtering/sorting (backend responsibility)
- ❌ Do NOT add optimistic updates (invalidation only)

**Delegation Recommendation**:
- **Category**: `quick` - Simple query endpoint (~30 lines)
- **Skills**: None needed - standard RTK Query list pattern
- **Reason**: Single query endpoint with tag generation, straightforward implementation

**Skills Evaluation**:
- ✅ NONE INCLUDED: Standard list query pattern
- ❌ OMITTED all skills: No specialized knowledge needed

**Parallelization**:
- **Can Run In Parallel**: YES
- **Parallel Group**: Wave 2 (with Task 3)
- **Blocks**: Task 5 (store integration)
- **Blocked By**: Task 2 (apiSlice)

**References**:

**Pattern References**:
- Research doc: RTK Query CRUD Patterns - Tag generation for lists with individual IDs
- `react/src/features/user/slices.ts` - Mentions `students: studentResDto[]` (find actual DTO path)

**Type References**:
- Find `studentResDto` in codebase (likely has `id: number`, `name: string`, `level: string`, etc.)

**API References**:
- Backend endpoint: `GET /api/students`

**Documentation References**:
- RTK Query docs: https://redux-toolkit.js.org/rtk-query/usage/automated-refetching - Providing tags for lists

**WHY Each Reference Matters**:
- CRUD patterns research: Shows the exact providesTags pattern for list + individual IDs
- User slice: Confirms the DTO name and structure expectations

**Acceptance Criteria**:

**Automated Verification** (using Bash):
```bash
# Agent runs from react/ directory:
npx tsc --noEmit
# Assert: Exit code 0
# Assert: No "error TS" messages related to studentsApi.ts

# Verify hook export:
grep -q "useGetStudentsQuery" src/features/students/api/studentsApi.ts
# Assert: Exit code 0
```

**Evidence to Capture**:
- [ ] Terminal output from `npx tsc --noEmit` showing 0 errors
- [ ] File content: `cat src/features/students/api/studentsApi.ts` (entire file - should be ~30 lines)

**Commit**: NO (grouped with Task 7)

---

### Task 5: Update store/index.ts - Integrate RTK Query Middleware + RESET Preservation

**What to do**:
- Modify `react/src/store/index.ts`
- Add imports:
  - Import `api` from `./api/apiSlice`
  - Import `setupListeners` from `@reduxjs/toolkit/query`
- Update `combinedReducer`:
  - Add `[api.reducerPath]: api.reducer` to the object passed to `combineReducers`
- Update `createStore` function:
  - Modify `configureStore` to add middleware:
    ```typescript
    middleware: (getDefaultMiddleware) =>
      getDefaultMiddleware().concat(api.middleware)
    ```
  - After creating store, call `setupListeners(store.dispatch)` before returning
- **CRITICAL**: Modify `rootReducer` function:
  - Change RESET logic from `state = {}` to:
    ```typescript
    if (action.type === 'RESET') {
      const { [api.reducerPath]: apiCache, ...rest } = state;
      state = { [api.reducerPath]: apiCache }; // Preserve RTK Query cache
    }
    ```
  - This ensures logout doesn't wipe cached API data

**Must NOT do**:
- ❌ Do NOT remove existing reducers (navigation, auth, notification, user must stay)
- ❌ Do NOT modify existing slice imports or logic
- ❌ Do NOT add custom middleware (only RTK Query middleware)
- ❌ Do NOT change store type exports (RootState, AppDispatch, hooks)

**Delegation Recommendation**:
- **Category**: `unspecified-low` - Careful modification of critical store file (~70 lines total)
- **Skills**: None needed - standard RTK Query integration pattern
- **Reason**: Requires precision to preserve existing logic while adding new middleware

**Skills Evaluation**:
- ✅ NONE INCLUDED: Well-documented store integration pattern
- ❌ OMITTED `typescript-programmer`: Pattern is in official RTK Query docs

**Parallelization**:
- **Can Run In Parallel**: NO
- **Parallel Group**: Wave 3 (sequential)
- **Blocks**: Task 6 (build verification)
- **Blocked By**: Task 2 (apiSlice), Task 3 (authApi), Task 4 (studentsApi)

**References**:

**Pattern References**:
- Research doc: RTK Query Official Docs - Store setup with middleware and setupListeners
- Research doc: RESET preservation pattern from user's context
- `react/src/store/index.ts:1-41` - Existing store structure (MUST preserve)

**Type References**:
- Existing: `RootState`, `AppDispatch`, `useAppDispatch`, `useAppSelector` (do NOT modify)

**Documentation References**:
- RTK Query docs: https://redux-toolkit.js.org/rtk-query/api/setupListeners - refetchOnFocus/refetchOnReconnect
- RTK Query docs: https://redux-toolkit.js.org/rtk-query/overview - Store integration example

**WHY Each Reference Matters**:
- Existing store file: Shows exact structure that must be preserved (reducer combineReducers, rootReducer RESET logic)
- setupListeners docs: Explains why this call is necessary for automatic refetching
- Store integration docs: Provide the exact middleware concatenation pattern

**Acceptance Criteria**:

**Automated Verification** (using Bash):
```bash
# Agent runs from react/ directory:
npx tsc --noEmit
# Assert: Exit code 0
# Assert: No "error TS" messages related to index.ts

# Verify middleware setup:
grep -q "api.middleware" src/store/index.ts
# Assert: Exit code 0

# Verify setupListeners:
grep -q "setupListeners" src/store/index.ts
# Assert: Exit code 0

# Verify RESET preservation:
grep -q "api.reducerPath" src/store/index.ts | grep -q "apiCache"
# Assert: Exit code 0
```

**Evidence to Capture**:
- [ ] Terminal output from `npx tsc --noEmit` showing 0 errors
- [ ] File content: `grep -A5 "RESET" src/store/index.ts` (show RESET logic modification)

**Commit**: NO (grouped with Task 7)

---

### Task 6: Verify Build - Run npm run build

**What to do**:
- Execute `npm run build` from `react/` directory
- Capture full output to verify success
- Check for any TypeScript errors or warnings
- Verify Vite build completes successfully

**Must NOT do**:
- ❌ Do NOT ignore TypeScript errors and proceed (must fix before commit)
- ❌ Do NOT skip type checking (ensure `tsc -b` runs in build script)
- ❌ Do NOT commit if build fails

**Delegation Recommendation**:
- **Category**: `quick` - Simple command execution and output verification
- **Skills**: None needed - standard build verification
- **Reason**: Single command execution with success/fail check

**Skills Evaluation**:
- ✅ NONE INCLUDED: Trivial command execution
- ❌ OMITTED all skills: No specialized knowledge required

**Parallelization**:
- **Can Run In Parallel**: NO
- **Parallel Group**: Wave 3 (sequential after Task 5)
- **Blocks**: Task 7 (git commit)
- **Blocked By**: Task 5 (store integration)

**References**:

**Command References**:
- `react/package.json:8` - Build script: `"build": "tsc -b && vite build"`

**Acceptance Criteria**:

**Automated Verification** (using Bash):
```bash
# Agent runs from react/ directory:
npm run build
# Assert: Exit code 0
# Assert: Output contains "built in" (Vite success message)
# Assert: Output does NOT contain "error TS" (no TypeScript errors)
# Assert: dist/ directory created with build artifacts
```

**Evidence to Capture**:
- [ ] Terminal output from `npm run build` (full output, last 50 lines minimum)
- [ ] Exit code: `echo $?` → 0
- [ ] Directory listing: `ls -lh dist/` (show build artifacts)

**Commit**: NO (grouped with Task 7)

---

### Task 7: Git Commit - Commit RTK Query Migration

**What to do**:
- Stage all new and modified files:
  - `git add react/src/store/api/baseQuery.ts`
  - `git add react/src/store/api/apiSlice.ts`
  - `git add react/src/features/auth/api/authApi.ts`
  - `git add react/src/features/students/api/studentsApi.ts`
  - `git add react/src/store/index.ts`
- Create commit with message: `feat(api): migrate to RTK Query from Axios`
- Extended commit body:
  ```
  - Add base query with JWT injection and error handling
  - Create main API slice with tag-based invalidation
  - Implement auth endpoints (login/register/profile)
  - Implement students query endpoint
  - Integrate RTK Query middleware and setupListeners
  - Preserve RTK Query cache in RESET action
  
  BREAKING: Old thunks deprecated but not removed (separate migration)
  ```
- Verify commit created: `git log -1 --oneline`

**Must NOT do**:
- ❌ Do NOT commit if Task 6 (build) failed
- ❌ Do NOT include unrelated file changes
- ❌ Do NOT remove old Axios service files yet (separate cleanup task)
- ❌ Do NOT use `--no-verify` to skip pre-commit hooks

**Delegation Recommendation**:
- **Category**: `quick` - Standard git commit operation
- **Skills**: `git-master` - Atomic commit with proper message format
- **Reason**: Git operations should use git-master skill for best practices

**Skills Evaluation**:
- ✅ INCLUDED `git-master`: Ensures atomic commit with proper conventional commit format
- ❌ OMITTED others: Only git expertise needed

**Parallelization**:
- **Can Run In Parallel**: NO
- **Parallel Group**: Wave 3 (sequential after Task 6)
- **Blocks**: None (final task)
- **Blocked By**: Task 6 (build verification)

**References**:

**Pattern References**:
- Conventional Commits: https://www.conventionalcommits.org/ - `feat(scope): description` format

**Acceptance Criteria**:

**Automated Verification** (using Bash):
```bash
# Agent runs from project root:
git log -1 --oneline
# Assert: Output contains "feat(api): migrate to RTK Query"

git show --name-only --oneline HEAD
# Assert: Shows 5 files changed (4 new + 1 modified)

git diff HEAD~1 HEAD --stat
# Assert: Shows insertions for new files, modifications for index.ts
```

**Evidence to Capture**:
- [ ] Terminal output from `git log -1 --pretty=fuller` (full commit details)
- [ ] Terminal output from `git show --stat HEAD` (changed files summary)
- [ ] Commit SHA: `git rev-parse HEAD`

**Commit**: YES
- Message: `feat(api): migrate to RTK Query from Axios`
- Files: `react/src/store/api/baseQuery.ts`, `react/src/store/api/apiSlice.ts`, `react/src/features/auth/api/authApi.ts`, `react/src/features/students/api/studentsApi.ts`, `react/src/store/index.ts`
- Pre-commit: `npm run build` (already verified in Task 6)

---

## Commit Strategy

| After Task | Message | Files | Verification |
|------------|---------|-------|--------------|
| 7 | `feat(api): migrate to RTK Query from Axios` | baseQuery.ts, apiSlice.ts, authApi.ts, studentsApi.ts, index.ts | npm run build (Task 6) |

**Single Atomic Commit**: All changes grouped into one commit because they form a cohesive migration unit. Splitting would leave codebase in broken state.

---

## Success Criteria

### Verification Commands

**Build Verification**:
```bash
cd react/
npm run build
# Expected: Exit code 0
# Expected: Output contains "built in" and "dist/index.html"
# Expected: No "error TS" messages
```

**Type Check Verification**:
```bash
cd react/
npx tsc --noEmit
# Expected: Exit code 0
# Expected: No output (silence = success)
```

**Import Verification** (optional - can test in browser console):
```javascript
// After dev server starts: npm run dev
import { useLoginMutation } from './features/auth/api/authApi';
import { useGetStudentsQuery } from './features/students/api/studentsApi';
import { api } from './store/api/apiSlice';
// All imports should resolve without errors
```

### Final Checklist

- [ ] All 4 new API files created (`baseQuery.ts`, `apiSlice.ts`, `authApi.ts`, `studentsApi.ts`)
- [ ] Store updated with RTK Query reducer, middleware, and setupListeners
- [ ] RESET action modified to preserve RTK Query cache
- [ ] Build succeeds: `npm run build` exits with code 0
- [ ] Type check passes: `npx tsc --noEmit` exits with code 0
- [ ] Git commit created with SHA
- [ ] All RTK Query hooks exported: `useLoginMutation`, `useRegisterMutation`, `useGetProfileQuery`, `useGetStudentsQuery`
- [ ] No errors in browser console when importing hooks (dev server test)
- [ ] Existing Redux slices (auth, navigation, notification, user) remain unchanged
- [ ] Token service functions unchanged (getToken, setTokens, clearTokens)
- [ ] Old Axios service files untouched (deferred to component migration task)

---

## Next Steps (Out of Scope)

After this plan is executed, the following tasks remain (separate work plans):

1. **Component Migration** - Replace thunk calls with RTK Query hooks in components
2. **Axios Service Removal** - Delete deprecated auth.service.ts and user.service.ts
3. **CRUD Completion** - Add CREATE/UPDATE/DELETE mutations for students
4. **Error Boundary** - Add React error boundary for RTK Query errors
5. **Loading States** - Implement skeleton loaders with RTK Query isLoading states
6. **Cache Configuration** - Tune keepUnusedDataFor and refetchOnMountOrArgChange
7. **Token Refresh** - Implement JWT refresh token flow

These are intentionally excluded to keep this migration focused and atomic.
