# ModelMapper Circular Reference Fix

## Problem
ModelMapper was creating circular references and nested data when mapping entities with bidirectional relationships (Department ↔ Subject ↔ Teacher), causing issues on the frontend.

## Solution

### 1. Updated ModelMapper Configuration (`AppConfig.java`)
- Set `MatchingStrategy.STRICT` to prevent unintended mappings
- Enabled `skipNullEnabled` to avoid null pointer issues
- Added explicit `skip()` mappings for circular reference fields:
  - Department → skip subjects and content
  - Subject → skip teacher, department, semester, content
  - Teacher → skip subjects, department, content
  - Grades → skip revendication

### 2. Created ResponseMapper Utility (`util/ResponseMapper.java`)
Manual mapping utility that provides full control over entity-to-DTO conversion:
- `toSubjectResponse()` - Maps Subject to SubjectResponse with only departmentId
- `toTeacherResponseBasic()` - Maps Teacher to TeacherResponse without nested subjects
- `toDepartmentResponse()` - Maps Department to DepartmentResponse with controlled subject nesting

### 3. Updated Service Implementations
- **DepartmentServiceImpl**: Uses ResponseMapper for clean department responses
- **SubjectServiceImpl**: Uses ResponseMapper for subject responses with teacher info
- **TeacherServiceImpl**: Uses ResponseMapper to avoid department/subject circular refs

## Benefits
✅ No circular references in JSON responses
✅ Clean, predictable API responses
✅ Frontend receives only necessary data
✅ Maintains your clean architecture pattern
✅ Full control over what data gets exposed
✅ Prevents N+1 query issues
✅ Avoids lazy loading exceptions

## Usage Pattern
```java
// In Service Implementation
private final ResponseMapper responseMapper;

// For single entity
SubjectResponse response = responseMapper.toSubjectResponse(subject);

// For collections
Set<SubjectResponse> responses = subjects.stream()
    .map(responseMapper::toSubjectResponse)
    .collect(Collectors.toSet());
```

## Key Principles Maintained
1. Response DTOs contain other Response DTOs (not entities)
2. Only expose necessary fields (departmentId instead of full Department)
3. Use Set collections to avoid duplicates
4. Consistent pagination metadata in responses
5. Separation of concerns (manual mapping for responses, ModelMapper for requests)
