# Draft: MapStruct Migration from ModelMapper

## Requirements (confirmed)

### Primary Objective
Complete migration from ModelMapper to MapStruct across the entire Spring Boot application.

### User-Specified Tasks
1. Stage and verify pom.xml changes (already made, unstaged)
2. Create MapStruct mapper interfaces for all DTOs
3. Update all service implementations to use MapStruct mappers instead of ModelMapper
4. Fix all controller return types to return Response DTOs (not Request DTOs)
5. Ensure project compiles: `mvn clean compile`
6. Commit atomically in 3 commits

### Scope Boundaries
- **INCLUDE**: All 25 ModelMapper.map() calls across 7 services
- **INCLUDE**: 21 controller methods with wrong return types
- **INCLUDE**: Custom mapping configurations (5 PropertyMaps in AppConfig.java)
- **INCLUDE**: Nested DTO mappings (SimpleStudentResponse, SimpleSubjectResponse, etc.)
- **EXCLUDE**: Business logic changes
- **EXCLUDE**: Entity model changes
- **EXCLUDE**: Database schema modifications

## Technical Decisions

### MapStruct Strategy
- **Component Model**: Spring (@Mapper(componentModel = "spring"))
- **Injection Strategy**: Field injection (matches existing @Autowired pattern)
- **Null Handling**: NullValuePropertyMappingStrategy.IGNORE (matches ModelMapper skipNull)
- **Bidirectional Mappings**: Both Request→Entity and Entity→Response

### Mapper Organization
- **One mapper interface per entity domain** (e.g., GradeMapper, StudentMapper)
- **Centralized helper mappers** for nested conversions
- **Proper @Mapping annotations** for field name mismatches

### Custom Mapping Requirements
From AppConfig.java analysis:
1. BigDecimal → Double converter (for GPA calculations)
2. Nested object mappings (avoid circular references)
3. Collection mappings (List<Grades> → List<GradeResponse>)

## Research Findings

### Codebase Analysis
- **Entities**: 13 total (JOINED inheritance for Student/Teacher)
- **DTOs**: 27 total (12 Request, 15 Response)
- **ModelMapper Usage**: 25 map() calls across 7 services
- **Unused Injections**: 3 services have ModelMapper but don't use it

### Critical Patterns Identified
1. **GradeServiceImpl** is the most complex (8 mappings, nested conversions)
2. **Circular Reference Prevention**: SimpleXxxResponse pattern
3. **Stream Mappings**: Used in RevendicationServiceImpl, TranscriptServiceImpl
4. **Manual Mappings**: RevendicationPeriodServiceImpl, SemesterServiceImpl already have some

### Controller Return Type Violations (21 methods)
Controllers returning Request DTOs instead of Response DTOs - services must be updated to return correct types.

## Open Questions
- None (user provided complete context)

## Commit Strategy (User-Specified)
1. Commit 1: pom.xml changes + verification
2. Commit 2: MapStruct mapper creation + service migration
3. Commit 3: Controller return type fixes

## Test Strategy Decision
- **Infrastructure exists**: YES (Maven, Spring Boot Test)
- **User wants tests**: NOT SPECIFIED - assume manual verification
- **Verification approach**: Compilation success + manual spot-checks
