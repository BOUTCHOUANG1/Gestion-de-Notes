# Controller Response Pattern

## Pattern Overview
Controllers return `List<ResponseType>` while services return wrapper `ResponseType` with pagination metadata.

## Implementation

### Service Layer
Services return a single `ResponseType` object containing:
- Individual fields (for single entity operations)
- `Set<ResponseType> content` (for paginated list operations)
- Pagination metadata (pageNumber, pageSize, totalElements, totalPages, lastPage)

Example:
```java
public DepartmentResponse getAllDepartments(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
    // ... pagination logic
    DepartmentResponse response = new DepartmentResponse();
    response.setContent(departmentResponses);  // Set of individual responses
    response.setPageNumber(page.getNumber());
    response.setPageSize(page.getSize());
    response.setTotalElements(page.getTotalElements());
    response.setTotalPages(page.getTotalPages());
    response.setLastPage(page.isLast());
    return response;
}
```

### Controller Layer
Controllers extract the `content` list and return it directly to frontend:

```java
@GetMapping("/admin/department")
public ResponseEntity<List<DepartmentResponse>> getAllDepartment(...) {
    DepartmentResponse response = departmentService.getAllDepartments(pageNumber, pageSize, sortBy, sortOrder);
    return new ResponseEntity<>(new ArrayList<>(response.getContent()), HttpStatus.OK);
}
```

## Updated Controllers

### ✅ DepartmentController
- `GET /admin/department` → Returns `List<DepartmentResponse>`

### ✅ SubjectController
- `GET /admin/subjects` → Returns `List<SubjectResponse>`
- `GET /teacher/subject` → Returns `List<SubjectResponse>`
- `GET /subjects` → Returns `List<SubjectResponse>`

### ✅ AdminController
- `GET /admin/teachers` → Returns `List<TeacherResponse>`
- `GET /admin/students` → Returns `List<StudentResponse>`

### ✅ RevendicationController
- `GET /teacher/revendications` → Returns `List<RevendicationResponse>`

### ✅ TeacherController
- `GET /teacher/my-grades` → Returns `List<GradeResponse>`
- `GET /teacher/my-students` → Returns `Map<String, List<StudentResponse>>`

### ✅ StudentController
- `GET /student/{studentId}/revendications` → Returns `List<RevendicationResponse>`

### ✅ SemesterController
- `GET /semesters` → Returns `List<SemesterResponse>`

## Benefits
1. ✅ Frontend receives direct list of objects (no wrapper unwrapping needed)
2. ✅ Service layer maintains pagination metadata for internal use
3. ✅ Clean separation of concerns
4. ✅ Consistent API response format
5. ✅ Easy to consume on frontend

## Response DTO Structure
All paginated response DTOs follow this pattern:

```java
@Data
public class ExampleResponse {
    // Individual entity fields
    private Long id;
    private String name;
    // ... other fields
    
    // Pagination wrapper fields
    private Set<ExampleResponse> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private Boolean lastPage;
}
```

## Frontend Consumption
Frontend receives clean arrays:
```json
[
  { "id": 1, "name": "Item 1" },
  { "id": 2, "name": "Item 2" }
]
```

Instead of nested wrapper:
```json
{
  "content": [...],
  "pageNumber": 0,
  "pageSize": 10
}
```
