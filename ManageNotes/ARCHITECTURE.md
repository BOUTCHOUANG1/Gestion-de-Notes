# Architecture Overview

## Data Flow
```
Controller → Service → Repository → Database
     ↓         ↓
   DTO ←→ Mapper ←→ Entity
```

## Layer Responsibilities

### 1. Controller Layer
- Handle HTTP requests/responses
- Validate input (using `@Valid`)
- Call service methods
- Return DTOs (never entities)

### 2. Service Layer
- Business logic
- Transaction management
- Call repositories
- Use mappers for entity-DTO conversion

### 3. Repository Layer
- Data access
- JPA queries
- Return entities

### 4. DTO Layer
- **Request DTOs**: Input validation, data transfer from client
- **Response DTOs**: Formatted output to client

### 5. Mapper Layer
- Convert between entities and DTOs
- Handle complex mappings

## Key Patterns

### Base CRUD Pattern
```java
// Service extends BaseCrudService
public class XService extends BaseCrudService<Entity, ID, RequestDTO, ResponseDTO>

// Controller extends BaseCrudController  
public class XController extends BaseCrudController<ID, RequestDTO, ResponseDTO>
```

### Proper Response Structure
```json
{
  "message": "Success message",
  "status": "SUCCESS|ERROR|WARNING",
  "data": { ... }
}
```

## Common Issues Fixed

1. **Inconsistent Service Patterns**: All services now extend BaseCrudService
2. **Missing Repository Methods**: Added department-specific queries
3. **Improper Response Mapping**: Fixed DTO conversions
4. **Redundant Code**: Eliminated duplicate methods