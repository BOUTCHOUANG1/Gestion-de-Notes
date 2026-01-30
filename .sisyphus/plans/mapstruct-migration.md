# MapStruct Migration from ModelMapper

## TL;DR

> **Quick Summary**: Complete migration from ModelMapper to MapStruct across Spring Boot application with 13 entities, 27 DTOs, replacing 25 map() calls across 7 services, fixing 21 controller return type violations, and ensuring compilation success.
> 
> **Deliverables**:
> - 13 MapStruct mapper interfaces (one per entity domain)
> - Updated pom.xml with MapStruct dependencies
> - 7 service implementations migrated from ModelMapper to MapStruct
> - 21 controller methods fixed to return Response DTOs
> - Clean Maven compilation
> 
> **Estimated Effort**: Large (complex nested mappings, bidirectional conversions, custom configurations)
> **Parallel Execution**: YES - 3 waves
> **Critical Path**: pom.xml verification → mapper creation → service migration → controller fixes → compilation

---

## Context

### Original Request
Complete the MapStruct migration we started:
1. The pom.xml changes are already done (unstaged). Stage and verify they compile.
2. Create MapStruct mapper interfaces for all DTOs
3. Update all service implementations to use MapStruct mappers instead of ModelMapper
4. Fix all controller return types to return Response DTOs (not Request DTOs)
5. Ensure the project compiles: mvn clean compile
6. Commit atomically in 3 commits

### Interview Summary
**Key Discussions**:
- User provided COMPLETE context upfront (no interview needed)
- 13 entities with JOINED inheritance (Student/Teacher extend Users)
- 27 DTOs (12 Request, 15 Response)
- 25 ModelMapper.map() calls across 7 active services
- 3 services have unused ModelMapper injections (cleanup needed)
- 21 controller methods return wrong DTO types

**Research Findings**:
- GradeServiceImpl is most complex: 8 mappings with nested conversions
- Custom ModelMapper configurations exist in AppConfig.java (5 PropertyMaps)
- Circular reference prevention via SimpleXxxResponse pattern
- BigDecimal→Double converter for GPA calculations
- Existing Lombok compilation errors (unrelated to migration)

### Gap Analysis (Self-Review)

**Identified Gaps** (addressed):
- **Gap 1: Mapper dependency injection order** → Resolved: Use @Mapper(uses = {...}) for nested mappers
- **Gap 2: Custom converter for BigDecimal→Double** → Resolved: Implement @Named helper method in GradeMapper
- **Gap 3: Collection mappings in streams** → Resolved: MapStruct handles List<T> automatically
- **Gap 4: Null handling strategy** → Resolved: Use NullValuePropertyMappingStrategy.IGNORE
- **Gap 5: Service method return type coordination** → Resolved: Services must continue returning what controllers expect
- **Gap 6: Verification before commit** → Resolved: Add compilation checks between commits

---

## Work Objectives

### Core Objective
Completely replace ModelMapper with MapStruct throughout the application, ensuring type safety, compilation success, and proper DTO usage in controllers.

### Concrete Deliverables
- `API_GestionNotes/ManageNotes/pom.xml` (staged, verified)
- `src/main/java/com/university/ManageNotes/mapper/GradeMapper.java`
- `src/main/java/com/university/ManageNotes/mapper/StudentMapper.java`
- `src/main/java/com/university/ManageNotes/mapper/TeacherMapper.java`
- `src/main/java/com/university/ManageNotes/mapper/SubjectMapper.java`
- `src/main/java/com/university/ManageNotes/mapper/DepartmentMapper.java`
- `src/main/java/com/university/ManageNotes/mapper/SemesterMapper.java`
- `src/main/java/com/university/ManageNotes/mapper/TranscriptMapper.java`
- `src/main/java/com/university/ManageNotes/mapper/RevendicationMapper.java`
- `src/main/java/com/university/ManageNotes/mapper/RevendicationPeriodMapper.java`
- `src/main/java/com/university/ManageNotes/mapper/AuthMapper.java`
- `src/main/java/com/university/ManageNotes/mapper/ExamMapper.java`
- `src/main/java/com/university/ManageNotes/mapper/TeachingLevelMapper.java`
- `src/main/java/com/university/ManageNotes/mapper/RolesMapper.java`
- 7 updated service implementations (removed ModelMapper, added MapStruct)
- 21 updated controller methods (corrected return types)
- Removed AppConfig.java ModelMapper bean and PropertyMaps

### Definition of Done
- [ ] All 25 ModelMapper.map() calls replaced with MapStruct mapper calls
- [ ] All 3 unused ModelMapper injections removed
- [ ] All 21 controller return types corrected to Response DTOs
- [ ] Services return types match what controllers expect
- [ ] AppConfig.java ModelMapper configuration removed
- [ ] `mvn clean compile` exits with code 0
- [ ] 3 atomic commits created with proper messages

### Must Have
- Exact behavioral equivalence to ModelMapper mappings
- Nested DTO mappings (SimpleStudentResponse, SimpleSubjectResponse, etc.)
- BigDecimal→Double conversion for GPA
- Null value skipping (NullValuePropertyMappingStrategy.IGNORE)
- Spring component model for automatic injection
- Bidirectional mappings (Request→Entity, Entity→Response)

### Must NOT Have (Guardrails)
- **NO business logic changes** - only mapping layer replacement
- **NO entity model changes** - entities remain unchanged
- **NO database schema modifications**
- **NO test file modifications** - focus on compilation only
- **NO breaking changes to service method signatures**
- **NO mixing of ModelMapper and MapStruct** - complete replacement
- **NO incomplete migrations** - ALL 25 calls must be replaced
- **NO premature commits** - verify compilation between commits
- **NO verbose debug logging** - keep mappers clean and focused

---

## Verification Strategy (MANDATORY)

> No test infrastructure required - verification via compilation and manual spot-checks

### Verification Decision
- **Infrastructure exists**: YES (Maven, Spring Boot)
- **User wants tests**: NO - manual verification only
- **Framework**: Maven compilation
- **Approach**: Automated compilation checks + manual verification procedures

### Manual Verification Only (NO User Intervention)

> **CRITICAL PRINCIPLE: AUTOMATED VERIFICATION**
>
> All verification must be executable by the agent without human intervention.

Each TODO includes EXECUTABLE verification procedures:

**By Deliverable Type:**

| Type | Verification Tool | Automated Procedure |
|------|------------------|---------------------|
| **pom.xml** | Maven via Bash | `mvn clean compile -DskipTests` → exit code 0 |
| **Mapper Interfaces** | Maven annotation processor | Compilation generates impl classes in target/ |
| **Service Updates** | Maven compilation | No compilation errors in service classes |
| **Controller Updates** | Maven compilation | Type checking passes, return types match |

**Evidence Requirements (Agent-Executable):**
- Maven compilation output captured
- Exit codes verified (0 = success)
- Generated MapStruct implementations exist in `target/generated-sources/annotations/`
- No ERROR level logs in Maven output
- Service class bytecode generated successfully

---

## Execution Strategy

### Parallel Execution Waves

> Migration follows strict dependency order due to compilation requirements

```
Wave 1 (Start Immediately):
└── Task 1: Stage and verify pom.xml changes [SEQUENTIAL - must be first]

Wave 2 (After Wave 1 compiles):
├── Task 2: Create ALL 13 MapStruct mapper interfaces [PARALLEL POSSIBLE]
│   ├── GradeMapper (most complex - do first)
│   ├── StudentMapper
│   ├── TeacherMapper
│   ├── SubjectMapper
│   ├── DepartmentMapper
│   ├── SemesterMapper
│   ├── TranscriptMapper
│   ├── RevendicationMapper
│   ├── RevendicationPeriodMapper
│   ├── AuthMapper
│   ├── ExamMapper
│   ├── TeachingLevelMapper
│   └── RolesMapper
└── Task 3: Remove ModelMapper from AppConfig.java [CAN PARALLEL with mappers]

Wave 3 (After Wave 2 compiles):
├── Task 4: Migrate GradeServiceImpl (most complex) [DO FIRST]
├── Task 5: Migrate DepartmentServiceImpl [AFTER GradeServiceImpl verifies pattern]
├── Task 6: Migrate RevendicationServiceImpl
├── Task 7: Migrate SubjectServiceImpl
├── Task 8: Migrate StudentServiceImpl
├── Task 9: Migrate TeacherServiceImpl
├── Task 10: Migrate TranscriptServiceImpl
└── Task 11: Clean up 3 unused ModelMapper injections

Wave 4 (After Wave 3 compiles):
├── Task 12: Fix controller return types (21 methods) [CAN BATCH]
└── Task 13: Final compilation verification

Wave 5 (After all verification passes):
├── Task 14: Create Commit 1 (pom.xml)
├── Task 15: Create Commit 2 (mappers + services)
└── Task 16: Create Commit 3 (controllers)

Critical Path: Task 1 → Task 2 → Task 4 → Task 12 → Task 13 → Task 14-16
Parallel Opportunities: Mapper creation (Wave 2), Some service migrations (Wave 3)
```

### Dependency Matrix

| Task | Depends On | Blocks | Can Parallelize With |
|------|------------|--------|---------------------|
| 1 | None | 2, 3 | None (must be first) |
| 2 | 1 | 4-11 | 3 |
| 3 | 1 | None | 2 |
| 4 | 2 | 5 | None (verify pattern first) |
| 5 | 4 | None | 6-11 (after pattern verified) |
| 6-11 | 4 | 12 | Each other (after 4) |
| 12 | 4-11 | 13 | None |
| 13 | 12 | 14-16 | None |
| 14-16 | 13 | None | None (sequential commits) |

### Agent Dispatch Summary

| Wave | Tasks | Recommended Strategy |
|------|-------|---------------------|
| 1 | 1 | Single agent - critical foundation |
| 2 | 2, 3 | Can parallel - independent work |
| 3 | 4-11 | Sequential start (task 4), then parallel |
| 4 | 12, 13 | Single agent - comprehensive change |
| 5 | 14-16 | Single agent - git operations |

---

## TODOs

### Wave 1: Foundation Setup

- [ ] 1. Stage and verify pom.xml changes

  **What to do**:
  - Stage the unstaged pom.xml file
  - Verify MapStruct dependencies are correct (version 1.6.3)
  - Ensure annotation processor configuration is present
  - Run clean compilation to verify dependency resolution
  - Verify MapStruct annotation processor works

  **Must NOT do**:
  - Modify pom.xml content (already done)
  - Skip compilation verification
  - Proceed if compilation fails

  **Recommended Agent Profile**:
  - **Category**: `quick`
    - Reason: Single file staging + compilation check - straightforward task
  - **Skills**: None needed
  - **Skills Evaluated but Omitted**:
    - `git-master`: Not needed - simple `git add` operation

  **Parallelization**:
  - **Can Run In Parallel**: NO
  - **Parallel Group**: Wave 1 (must complete before Wave 2)
  - **Blocks**: Tasks 2, 3 (all mapper and config work)
  - **Blocked By**: None (starting task)

  **References**:

  **Pattern References**:
  - `API_GestionNotes/ManageNotes/pom.xml:61-74` - Existing MapStruct configuration (verify these lines exist)
  - `API_GestionNotes/ManageNotes/pom.xml:37-39` - ModelMapper dependency REMOVED (verify removal)

  **Documentation References**:
  - https://mapstruct.org/documentation/stable/reference/html/#setup - MapStruct Maven setup guide
  - https://projectlombok.org/setup/maven - Lombok + MapStruct binding requirements

  **WHY Each Reference Matters**:
  - pom.xml changes: User stated these are already done, need to verify correct configuration
  - MapStruct docs: Ensure annotation processor config matches official recommendations
  - Lombok binding: Critical for MapStruct to work with Lombok entities

  **Acceptance Criteria**:

  **Automated Verification:**
  ```bash
  # Agent executes:
  git status API_GestionNotes/ManageNotes/pom.xml
  # Assert: Shows "Changes to be committed" (staged)
  
  cd API_GestionNotes/ManageNotes && mvn clean compile -DskipTests
  # Assert: Exit code 0
  # Assert: Output contains "BUILD SUCCESS"
  # Assert: No "ERROR" lines related to MapStruct or annotation processing
  
  # Verify MapStruct processor ran
  ls target/generated-sources/annotations/
  # Assert: Directory exists (created by annotation processor)
  ```

  **Evidence to Capture:**
  - [ ] Git status output showing pom.xml staged
  - [ ] Maven compilation output (full log)
  - [ ] Exit code from compilation
  - [ ] Contents of target/generated-sources/annotations/ (should be empty initially)

  **Commit**: YES
  - Message: `build(deps): migrate from ModelMapper to MapStruct 1.6.3`
  - Files: `API_GestionNotes/ManageNotes/pom.xml`
  - Pre-commit: `cd API_GestionNotes/ManageNotes && mvn clean compile -DskipTests`

---

### Wave 2: Mapper Creation

- [ ] 2. Create all 13 MapStruct mapper interfaces

  **What to do**:
  - Create `src/main/java/com/university/ManageNotes/mapper/` package
  - Create GradeMapper with nested conversions (MOST COMPLEX - do first)
  - Create StudentMapper, TeacherMapper with inheritance handling
  - Create SubjectMapper, DepartmentMapper, SemesterMapper
  - Create TranscriptMapper, RevendicationMapper, RevendicationPeriodMapper
  - Create AuthMapper, ExamMapper, TeachingLevelMapper, RolesMapper
  - Configure @Mapper annotations: componentModel = "spring", nullValuePropertyMappingStrategy = IGNORE
  - Use @Mapping annotations for field mismatches
  - Implement @Named helper methods for custom conversions (BigDecimal→Double)
  - Use @Mapper(uses = {...}) for nested mapper dependencies

  **Must NOT do**:
  - Mix ModelMapper and MapStruct
  - Use default MapStruct config (must specify componentModel = "spring")
  - Create separate mappers for Simple DTOs (reuse within same mapper)
  - Add verbose comments or debug logging
  - Implement business logic in mappers

  **Recommended Agent Profile**:
  - **Category**: `ultrabrain`
    - Reason: Complex nested mappings, bidirectional conversions, custom configurations - requires deep analysis
  - **Skills**: None needed (pure Java interface creation)
  - **Skills Evaluated but Omitted**:
    - `git-master`: No git operations in this task

  **Parallelization**:
  - **Can Run In Parallel**: YES (after GradeMapper pattern established)
  - **Parallel Group**: Wave 2
  - **Blocks**: Tasks 4-11 (all service migrations)
  - **Blocked By**: Task 1 (pom.xml must compile first)

  **References**:

  **Pattern References** (existing ModelMapper configs to replicate):
  - `src/main/java/com/university/ManageNotes/config/AppConfig.java:28-41` - Grades → GradeResponse mapping (nested conversions)
  - `src/main/java/com/university/ManageNotes/config/AppConfig.java:44-54` - Student → SimpleStudentResponse mapping
  - `src/main/java/com/university/ManageNotes/config/AppConfig.java:57-64` - Subject → SimpleSubjectResponse mapping
  - `src/main/java/com/university/ManageNotes/config/AppConfig.java:67-76` - Teacher → SimpleTeacherResponse mapping
  - `src/main/java/com/university/ManageNotes/config/AppConfig.java:79-86` - Semester → SimpleSemesterResponse mapping
  - `src/main/java/com/university/ManageNotes/config/AppConfig.java:23-25` - BigDecimal → Double converter

  **API/Type References** (entities and DTOs):
  - `src/main/java/com/university/ManageNotes/entity/Grades.java` - Source entity
  - `src/main/java/com/university/ManageNotes/dto/request/GradeRequest.java` - Request DTO
  - `src/main/java/com/university/ManageNotes/dto/response/GradeResponse.java` - Response DTO with nested Simple DTOs
  - `src/main/java/com/university/ManageNotes/entity/Student.java` - JOINED inheritance pattern
  - `src/main/java/com/university/ManageNotes/entity/Teacher.java` - JOINED inheritance pattern
  - `src/main/java/com/university/ManageNotes/entity/Subject.java`
  - `src/main/java/com/university/ManageNotes/entity/Department.java`
  - `src/main/java/com/university/ManageNotes/entity/Semester.java`
  - `src/main/java/com/university/ManageNotes/entity/Transcript.java`
  - `src/main/java/com/university/ManageNotes/entity/Revendication.java`
  - `src/main/java/com/university/ManageNotes/entity/RevendicationPeriod.java`

  **External References**:
  - https://mapstruct.org/documentation/stable/reference/html/#mapping-composition - Nested mapper usage
  - https://mapstruct.org/documentation/stable/reference/html/#qualifiers - @Named methods for custom conversions
  - https://mapstruct.org/documentation/stable/reference/html/#lombok - Lombok integration

  **WHY Each Reference Matters**:
  - AppConfig.java PropertyMaps: Show exact field mappings ModelMapper used (must replicate in MapStruct)
  - Entity files: Understand source structure, especially JOINED inheritance
  - GradeResponse: Shows circular reference prevention pattern via Simple DTOs
  - MapStruct docs: Official patterns for nested mappers and custom conversions

  **Acceptance Criteria**:

  **Automated Verification:**
  ```bash
  # Agent executes:
  cd API_GestionNotes/ManageNotes && mvn clean compile -DskipTests
  # Assert: Exit code 0
  # Assert: Output contains "BUILD SUCCESS"
  
  # Verify MapStruct generated implementations
  ls target/generated-sources/annotations/com/university/ManageNotes/mapper/
  # Assert: Contains GradeMapperImpl.java
  # Assert: Contains StudentMapperImpl.java
  # Assert: Contains TeacherMapperImpl.java
  # Assert: Contains SubjectMapperImpl.java
  # Assert: Contains DepartmentMapperImpl.java
  # Assert: Contains SemesterMapperImpl.java
  # Assert: Contains TranscriptMapperImpl.java
  # Assert: Contains RevendicationMapperImpl.java
  # Assert: Contains RevendicationPeriodMapperImpl.java
  # Assert: Contains AuthMapperImpl.java
  # Assert: Contains ExamMapperImpl.java
  # Assert: Contains TeachingLevelMapperImpl.java
  # Assert: Contains RolesMapperImpl.java
  
  # Verify Spring components generated
  grep -r "@Component" target/generated-sources/annotations/com/university/ManageNotes/mapper/
  # Assert: All *Impl.java files have @Component annotation
  ```

  **Evidence to Capture:**
  - [ ] Maven compilation output
  - [ ] List of generated *Impl.java files
  - [ ] Grep output showing @Component annotations

  **Commit**: NO (groups with task 3 and tasks 4-11)

---

- [ ] 3. Remove ModelMapper configuration from AppConfig.java

  **What to do**:
  - Remove ModelMapper @Bean method (lines 15-21)
  - Remove BigDecimal→Double converter (lines 23-25)
  - Remove all 5 PropertyMap configurations (lines 28-86)
  - Remove ModelMapper import statements
  - Keep any other Spring configurations in the file

  **Must NOT do**:
  - Delete entire AppConfig.java (may have other configs)
  - Remove non-ModelMapper configurations
  - Leave commented-out ModelMapper code

  **Recommended Agent Profile**:
  - **Category**: `quick`
    - Reason: Simple code removal - well-defined deletion task
  - **Skills**: None needed
  - **Skills Evaluated but Omitted**:
    - None applicable

  **Parallelization**:
  - **Can Run In Parallel**: YES
  - **Parallel Group**: Wave 2 (with task 2)
  - **Blocks**: None
  - **Blocked By**: Task 1

  **References**:

  **Pattern References**:
  - `src/main/java/com/university/ManageNotes/config/AppConfig.java:15-86` - Entire ModelMapper configuration section to remove

  **WHY Each Reference Matters**:
  - Exact lines to delete - ensures complete removal of ModelMapper

  **Acceptance Criteria**:

  **Automated Verification:**
  ```bash
  # Agent executes:
  grep -n "ModelMapper" API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/config/AppConfig.java
  # Assert: No matches (all ModelMapper code removed)
  
  cd API_GestionNotes/ManageNotes && mvn clean compile -DskipTests
  # Assert: Exit code 0
  # Assert: Output contains "BUILD SUCCESS"
  ```

  **Evidence to Capture:**
  - [ ] Grep output (should be empty)
  - [ ] Maven compilation success

  **Commit**: NO (groups with tasks 2, 4-11)

---

### Wave 3: Service Migration

- [ ] 4. Migrate GradeServiceImpl to MapStruct

  **What to do**:
  - Remove ModelMapper field injection
  - Add GradeMapper field injection via @Autowired or constructor
  - Replace 8 ModelMapper.map() calls with GradeMapper methods:
    - Line 38: gradeRequest → Grades (use gradeMapper.toEntity())
    - Line 92: grades → GradeRequest (WRONG TYPE - should be GradeResponse)
    - Line 136: grades → GradeRequest (WRONG TYPE - should be GradeResponse)
    - Line 217: grades → GradeResponse (use gradeMapper.toResponse())
    - Lines 220, 224, 232, 236: Nested conversions (handled by GradeMapper)
  - Remove toGradeResponse helper method (replaced by mapper)
  - Remove ModelMapper import

  **Must NOT do**:
  - Change business logic
  - Modify method signatures
  - Skip any of the 8 mappings
  - Leave ModelMapper imports or fields

  **Recommended Agent Profile**:
  - **Category**: `ultrabrain`
    - Reason: Most complex service with 8 mappings and nested conversions - critical pattern for other services
  - **Skills**: None needed
  - **Skills Evaluated but Omitted**:
    - None applicable

  **Parallelization**:
  - **Can Run In Parallel**: NO (must verify pattern first)
  - **Parallel Group**: Wave 3 (first task)
  - **Blocks**: Tasks 5-11 (pattern verification for other services)
  - **Blocked By**: Task 2 (mappers must exist)

  **References**:

  **Pattern References**:
  - `src/main/java/com/university/ManageNotes/service/impl/GradeServiceImpl.java:38` - CREATE mapping
  - `src/main/java/com/university/ManageNotes/service/impl/GradeServiceImpl.java:92` - CREATE response (WRONG TYPE)
  - `src/main/java/com/university/ManageNotes/service/impl/GradeServiceImpl.java:136` - UPDATE response (WRONG TYPE)
  - `src/main/java/com/university/ManageNotes/service/impl/GradeServiceImpl.java:217-236` - toGradeResponse helper (replace with mapper)

  **API/Type References**:
  - `src/main/java/com/university/ManageNotes/mapper/GradeMapper.java` - Newly created mapper interface

  **WHY Each Reference Matters**:
  - Service line numbers: Exact locations of ModelMapper.map() calls to replace
  - GradeMapper: The replacement for ModelMapper - must use these methods

  **Acceptance Criteria**:

  **Automated Verification:**
  ```bash
  # Agent executes:
  grep -n "modelMapper" API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/service/impl/GradeServiceImpl.java
  # Assert: No matches (all ModelMapper references removed)
  
  grep -n "gradeMapper" API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/service/impl/GradeServiceImpl.java
  # Assert: Shows GradeMapper injection and usage
  
  cd API_GestionNotes/ManageNotes && mvn clean compile -DskipTests
  # Assert: Exit code 0
  # Assert: No compilation errors in GradeServiceImpl
  ```

  **Evidence to Capture:**
  - [ ] Grep outputs showing ModelMapper removed, GradeMapper added
  - [ ] Maven compilation success

  **Commit**: NO (groups with tasks 2, 3, 5-11)

---

- [ ] 5. Migrate DepartmentServiceImpl to MapStruct

  **What to do**:
  - Remove ModelMapper field injection
  - Add DepartmentMapper field injection
  - Replace 5 ModelMapper.map() calls:
    - Line 42: DepartmentRequest → Department
    - Line 63: Department → DepartmentRequest (WRONG TYPE - should be DepartmentResponse)
    - Line 95: DepartmentRequest → Department
    - Line 132: Department → DepartmentRequest (WRONG TYPE)
    - Line 146: Department → DepartmentRequest (WRONG TYPE)
  - Remove ModelMapper import

  **Must NOT do**:
  - Change method signatures
  - Modify business logic

  **Recommended Agent Profile**:
  - **Category**: `quick`
    - Reason: After GradeServiceImpl pattern verified, this is straightforward replacement (5 mappings)
  - **Skills**: None needed
  - **Skills Evaluated but Omitted**:
    - None applicable

  **Parallelization**:
  - **Can Run In Parallel**: YES
  - **Parallel Group**: Wave 3 (after task 4)
  - **Blocks**: Task 13
  - **Blocked By**: Task 4 (pattern verification)

  **References**:

  **Pattern References**:
  - `src/main/java/com/university/ManageNotes/service/impl/DepartmentServiceImpl.java:42,63,95,132,146` - All mapping locations
  - `src/main/java/com/university/ManageNotes/service/impl/GradeServiceImpl.java` - Migration pattern from task 4

  **API/Type References**:
  - `src/main/java/com/university/ManageNotes/mapper/DepartmentMapper.java` - Mapper to use

  **WHY Each Reference Matters**:
  - Service lines: Exact map() calls to replace
  - GradeServiceImpl: Proven pattern for MapStruct migration
  - DepartmentMapper: Replacement interface

  **Acceptance Criteria**:

  **Automated Verification:**
  ```bash
  # Agent executes:
  grep -n "modelMapper" API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/service/impl/DepartmentServiceImpl.java
  # Assert: No matches
  
  cd API_GestionNotes/ManageNotes && mvn clean compile -DskipTests
  # Assert: Exit code 0
  ```

  **Evidence to Capture:**
  - [ ] Grep output showing ModelMapper removed
  - [ ] Compilation success

  **Commit**: NO (groups with 2, 3, 4, 6-11)

---

- [ ] 6. Migrate RevendicationServiceImpl to MapStruct

  **What to do**:
  - Remove ModelMapper field injection
  - Add RevendicationMapper field injection
  - Replace 4 ModelMapper.map() calls:
    - Line 45: RevendicationRequest → Revendication
    - Line 85: Revendication → RevendicationRequest (WRONG TYPE)
    - Line 105: Stream mapping Revendication → RevendicationResponse
    - Line 195: Stream mapping Revendication → RevendicationResponse
  - Update stream mappings to use mapper reference
  - Remove ModelMapper import

  **Must NOT do**:
  - Break stream operations
  - Change business logic

  **Recommended Agent Profile**:
  - **Category**: `quick`
    - Reason: Similar pattern to previous migrations, includes stream mappings (4 total)
  - **Skills**: None needed
  - **Skills Evaluated but Omitted**:
    - None applicable

  **Parallelization**:
  - **Can Run In Parallel**: YES
  - **Parallel Group**: Wave 3 (after task 4)
  - **Blocks**: Task 13
  - **Blocked By**: Task 4

  **References**:

  **Pattern References**:
  - `src/main/java/com/university/ManageNotes/service/impl/RevendicationServiceImpl.java:45,85,105,195` - Mapping locations
  - `src/main/java/com/university/ManageNotes/service/impl/GradeServiceImpl.java` - Migration pattern

  **API/Type References**:
  - `src/main/java/com/university/ManageNotes/mapper/RevendicationMapper.java` - Mapper to use

  **WHY Each Reference Matters**:
  - Stream mappings need special attention: `.map(modelMapper::map)` becomes `.map(revendicationMapper::toResponse)`
  - Service lines: All map() call locations

  **Acceptance Criteria**:

  **Automated Verification:**
  ```bash
  # Agent executes:
  grep -n "modelMapper" API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/service/impl/RevendicationServiceImpl.java
  # Assert: No matches
  
  cd API_GestionNotes/ManageNotes && mvn clean compile -DskipTests
  # Assert: Exit code 0
  ```

  **Evidence to Capture:**
  - [ ] Grep output
  - [ ] Compilation success

  **Commit**: NO (groups with 2-5, 7-11)

---

- [ ] 7. Migrate SubjectServiceImpl to MapStruct

  **What to do**:
  - Remove ModelMapper field injection
  - Add SubjectMapper field injection
  - Replace 3 ModelMapper.map() calls:
    - Line 155: SubjectRequest → Subject
    - Line 185: Subject → SubjectRequest (WRONG TYPE)
    - Line 194: Subject → SubjectRequest (WRONG TYPE)
  - Remove ModelMapper import

  **Must NOT do**:
  - Modify business logic

  **Recommended Agent Profile**:
  - **Category**: `quick`
    - Reason: Simple migration - 3 mappings, established pattern
  - **Skills**: None needed
  - **Skills Evaluated but Omitted**:
    - None applicable

  **Parallelization**:
  - **Can Run In Parallel**: YES
  - **Parallel Group**: Wave 3 (after task 4)
  - **Blocks**: Task 13
  - **Blocked By**: Task 4

  **References**:

  **Pattern References**:
  - `src/main/java/com/university/ManageNotes/service/impl/SubjectServiceImpl.java:155,185,194` - Mapping locations
  - `src/main/java/com/university/ManageNotes/service/impl/GradeServiceImpl.java` - Migration pattern

  **API/Type References**:
  - `src/main/java/com/university/ManageNotes/mapper/SubjectMapper.java` - Mapper to use

  **WHY Each Reference Matters**:
  - Service lines: map() calls to replace
  - Pattern reference: Proven migration approach

  **Acceptance Criteria**:

  **Automated Verification:**
  ```bash
  # Agent executes:
  grep -n "modelMapper" API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/service/impl/SubjectServiceImpl.java
  # Assert: No matches
  
  cd API_GestionNotes/ManageNotes && mvn clean compile -DskipTests
  # Assert: Exit code 0
  ```

  **Evidence to Capture:**
  - [ ] Grep output
  - [ ] Compilation success

  **Commit**: NO (groups with 2-6, 8-11)

---

- [ ] 8. Migrate StudentServiceImpl to MapStruct

  **What to do**:
  - Remove ModelMapper field injection
  - Add StudentMapper field injection
  - Replace 2 ModelMapper.map() calls:
    - Line 37: StudentRequest → Student
    - Line 74: Student → StudentRequest (WRONG TYPE)
  - Remove ModelMapper import

  **Must NOT do**:
  - Modify business logic

  **Recommended Agent Profile**:
  - **Category**: `quick`
    - Reason: Simple migration - 2 mappings
  - **Skills**: None needed
  - **Skills Evaluated but Omitted**:
    - None applicable

  **Parallelization**:
  - **Can Run In Parallel**: YES
  - **Parallel Group**: Wave 3 (after task 4)
  - **Blocks**: Task 13
  - **Blocked By**: Task 4

  **References**:

  **Pattern References**:
  - `src/main/java/com/university/ManageNotes/service/impl/StudentServiceImpl.java:37,74` - Mapping locations
  - `src/main/java/com/university/ManageNotes/service/impl/GradeServiceImpl.java` - Migration pattern

  **API/Type References**:
  - `src/main/java/com/university/ManageNotes/mapper/StudentMapper.java` - Mapper to use

  **WHY Each Reference Matters**:
  - Service lines: map() calls to replace
  - Student entity has JOINED inheritance - mapper must handle

  **Acceptance Criteria**:

  **Automated Verification:**
  ```bash
  # Agent executes:
  grep -n "modelMapper" API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/service/impl/StudentServiceImpl.java
  # Assert: No matches
  
  cd API_GestionNotes/ManageNotes && mvn clean compile -DskipTests
  # Assert: Exit code 0
  ```

  **Evidence to Capture:**
  - [ ] Grep output
  - [ ] Compilation success

  **Commit**: NO (groups with 2-7, 9-11)

---

- [ ] 9. Migrate TeacherServiceImpl to MapStruct

  **What to do**:
  - Remove ModelMapper field injection
  - Add TeacherMapper field injection
  - Replace 2 ModelMapper.map() calls:
    - Line 48: TeacherRequest → Teacher
    - Line 63: Teacher → TeacherRequest (WRONG TYPE)
  - Remove ModelMapper import

  **Must NOT do**:
  - Modify business logic

  **Recommended Agent Profile**:
  - **Category**: `quick`
    - Reason: Simple migration - 2 mappings
  - **Skills**: None needed
  - **Skills Evaluated but Omitted**:
    - None applicable

  **Parallelization**:
  - **Can Run In Parallel**: YES
  - **Parallel Group**: Wave 3 (after task 4)
  - **Blocks**: Task 13
  - **Blocked By**: Task 4

  **References**:

  **Pattern References**:
  - `src/main/java/com/university/ManageNotes/service/impl/TeacherServiceImpl.java:48,63` - Mapping locations
  - `src/main/java/com/university/ManageNotes/service/impl/GradeServiceImpl.java` - Migration pattern

  **API/Type References**:
  - `src/main/java/com/university/ManageNotes/mapper/TeacherMapper.java` - Mapper to use

  **WHY Each Reference Matters**:
  - Teacher entity has JOINED inheritance - mapper must handle
  - Service lines: map() calls to replace

  **Acceptance Criteria**:

  **Automated Verification:**
  ```bash
  # Agent executes:
  grep -n "modelMapper" API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/service/impl/TeacherServiceImpl.java
  # Assert: No matches
  
  cd API_GestionNotes/ManageNotes && mvn clean compile -DskipTests
  # Assert: Exit code 0
  ```

  **Evidence to Capture:**
  - [ ] Grep output
  - [ ] Compilation success

  **Commit**: NO (groups with 2-8, 10-11)

---

- [ ] 10. Migrate TranscriptServiceImpl to MapStruct

  **What to do**:
  - Remove ModelMapper field injection
  - Add GradeMapper field injection (uses GradeMapper for stream mapping)
  - Replace 1 ModelMapper.map() call:
    - Line 78: Stream mapping Grades → GradeResponse
  - Update stream to use gradeMapper::toResponse
  - Remove ModelMapper import

  **Must NOT do**:
  - Break stream operation

  **Recommended Agent Profile**:
  - **Category**: `quick`
    - Reason: Single stream mapping - simple replacement
  - **Skills**: None needed
  - **Skills Evaluated but Omitted**:
    - None applicable

  **Parallelization**:
  - **Can Run In Parallel**: YES
  - **Parallel Group**: Wave 3 (after task 4)
  - **Blocks**: Task 13
  - **Blocked By**: Task 4

  **References**:

  **Pattern References**:
  - `src/main/java/com/university/ManageNotes/service/impl/TranscriptServiceImpl.java:78` - Stream mapping location
  - `src/main/java/com/university/ManageNotes/service/impl/RevendicationServiceImpl.java:105,195` - Stream mapping pattern

  **API/Type References**:
  - `src/main/java/com/university/ManageNotes/mapper/GradeMapper.java` - Mapper to use (NOT TranscriptMapper)

  **WHY Each Reference Matters**:
  - Uses GradeMapper because it's mapping Grades entities
  - Stream pattern: Shows how to replace method reference

  **Acceptance Criteria**:

  **Automated Verification:**
  ```bash
  # Agent executes:
  grep -n "modelMapper" API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/service/impl/TranscriptServiceImpl.java
  # Assert: No matches
  
  cd API_GestionNotes/ManageNotes && mvn clean compile -DskipTests
  # Assert: Exit code 0
  ```

  **Evidence to Capture:**
  - [ ] Grep output
  - [ ] Compilation success

  **Commit**: NO (groups with 2-9, 11)

---

- [ ] 11. Clean up unused ModelMapper injections

  **What to do**:
  - Remove ModelMapper field from RevendicationPeriodServiceImpl (line 33)
  - Remove ModelMapper import from RevendicationPeriodServiceImpl
  - Remove ModelMapper field from SemesterServiceImpl (line 24)
  - Remove ModelMapper import from SemesterServiceImpl
  - Remove ModelMapper import from AuthServiceImpl (line 16 - import only, no field)
  - Verify these services have manual mapping or don't need mappers

  **Must NOT do**:
  - Add MapStruct mappers if not needed (manual mappings are fine)
  - Break existing functionality

  **Recommended Agent Profile**:
  - **Category**: `quick`
    - Reason: Simple cleanup - removing unused fields and imports
  - **Skills**: None needed
  - **Skills Evaluated but Omitted**:
    - None applicable

  **Parallelization**:
  - **Can Run In Parallel**: YES
  - **Parallel Group**: Wave 3 (after task 4)
  - **Blocks**: Task 13
  - **Blocked By**: Task 4

  **References**:

  **Pattern References**:
  - `src/main/java/com/university/ManageNotes/service/impl/RevendicationPeriodServiceImpl.java:33` - Unused ModelMapper field
  - `src/main/java/com/university/ManageNotes/service/impl/SemesterServiceImpl.java:24` - Unused ModelMapper field
  - `src/main/java/com/university/ManageNotes/service/impl/AuthServiceImpl.java:16` - Unused ModelMapper import

  **WHY Each Reference Matters**:
  - These services inject ModelMapper but never call .map() - safe to remove
  - They have manual mapping methods instead

  **Acceptance Criteria**:

  **Automated Verification:**
  ```bash
  # Agent executes:
  grep -rn "ModelMapper" API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/service/
  # Assert: No matches (all ModelMapper references removed from services)
  
  cd API_GestionNotes/ManageNotes && mvn clean compile -DskipTests
  # Assert: Exit code 0
  ```

  **Evidence to Capture:**
  - [ ] Grep output showing no ModelMapper in services
  - [ ] Compilation success

  **Commit**: NO (groups with 2-10)

---

### Wave 4: Controller Fixes

- [ ] 12. Fix all 21 controller return types to Response DTOs

  **What to do**:
  - **AdminController**:
    - Line 35: updateTeacher → Change return type TeacherRequest to TeacherResponse
  - **SemesterController**:
    - Line 34: createSemester → Change return type SemesterRequest to SemesterResponse
    - Line 40: updateSemester → Change return type SemesterRequest to SemesterResponse
  - **RevendicationController**:
    - Line 28: createRevendication → Change return type RevendicationRequest to RevendicationResponse
  - **SubjectController**:
    - Line 50: createSubject → Change return type SubjectRequest to SubjectResponse
    - Line 56: updateSubject → Change return type SubjectRequest to SubjectResponse
    - Line 64: delete → Change return type SubjectRequest to SubjectResponse
  - **DepartmentController**:
    - Line 28: createDepartement → Change return type DepartmentRequest to DepartmentResponse
    - Line 52: updateDepartment → Change return type DepartmentRequest to DepartmentResponse
    - Line 60: deleteDepartment → Change return type DepartmentRequest to DepartmentResponse
  - **GradeController**:
    - Line 26: createGrade → Change return type GradeRequest to GradeResponse
    - Line 32: updateGrade → Change return type GradeRequest to GradeResponse
  - **StudentController**:
    - Line 33: updateStudent → Change return type StudentRequest to StudentResponse
  - **RevendicationPeriodController**:
    - Line 31: createRevendicatioPeriod → Change return type RevendicationPeriodRequest to RevendicationPeriodResponse
    - Line 37: updateRevendicationPeriod → Change return type RevendicationPeriodRequest to RevendicationPeriodResponse
  
  - **CRITICAL**: Verify corresponding service methods already return Response types
  - Update service calls if services currently return Request types (from tasks 4-11)

  **Must NOT do**:
  - Change business logic
  - Modify request parameters
  - Break API contracts (only internal return types)

  **Recommended Agent Profile**:
  - **Category**: `quick`
    - Reason: Type signature updates - straightforward but comprehensive (21 methods)
  - **Skills**: None needed
  - **Skills Evaluated but Omitted**:
    - None applicable

  **Parallelization**:
  - **Can Run In Parallel**: NO (comprehensive change across many files)
  - **Parallel Group**: Wave 4 (single task)
  - **Blocks**: Task 13
  - **Blocked By**: Tasks 4-11 (services must be migrated first)

  **References**:

  **Pattern References**:
  - `src/main/java/com/university/ManageNotes/controller/AdminController.java:35` - TeacherRequest violation
  - `src/main/java/com/university/ManageNotes/controller/SemesterController.java:34,40` - SemesterRequest violations
  - `src/main/java/com/university/ManageNotes/controller/RevendicationController.java:28` - RevendicationRequest violation
  - `src/main/java/com/university/ManageNotes/controller/SubjectController.java:50,56,64` - SubjectRequest violations
  - `src/main/java/com/university/ManageNotes/controller/DepartmentController.java:28,52,60` - DepartmentRequest violations
  - `src/main/java/com/university/ManageNotes/controller/GradeController.java:26,32` - GradeRequest violations
  - `src/main/java/com/university/ManageNotes/controller/StudentController.java:33` - StudentRequest violation
  - `src/main/java/com/university/ManageNotes/controller/RevendicationPeriodController.java:31,37` - RevendicationPeriodRequest violations

  **WHY Each Reference Matters**:
  - Controllers should NEVER return Request DTOs (input types)
  - Must return Response DTOs (output types)
  - Services were updated in tasks 4-11 to return Response types

  **Acceptance Criteria**:

  **Automated Verification:**
  ```bash
  # Agent executes:
  grep -rn "Request>" API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/controller/ | grep "ResponseEntity"
  # Assert: Only shows parameters, NOT return types
  
  grep -rn "Response>" API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/controller/ | grep "ResponseEntity" | wc -l
  # Assert: Shows 21+ lines (all return types are Response DTOs)
  
  cd API_GestionNotes/ManageNotes && mvn clean compile -DskipTests
  # Assert: Exit code 0
  ```

  **Evidence to Capture:**
  - [ ] Grep outputs showing Request→Response changes
  - [ ] Compilation success

  **Commit**: YES
  - Message: `refactor: fix controller return types from Request to Response DTOs`
  - Files: All controller files modified
  - Pre-commit: `cd API_GestionNotes/ManageNotes && mvn clean compile -DskipTests`

---

### Wave 5: Final Verification & Commits

- [ ] 13. Final compilation verification

  **What to do**:
  - Run full clean compile: `mvn clean compile`
  - Verify all MapStruct implementations generated
  - Check for any remaining ModelMapper references in codebase
  - Verify no compilation errors
  - Verify no WARNING logs about missing mappers
  - Run quick smoke test: `mvn package -DskipTests`

  **Must NOT do**:
  - Skip verification steps
  - Proceed to commits if compilation fails

  **Recommended Agent Profile**:
  - **Category**: `quick`
    - Reason: Verification only - running commands and checking output
  - **Skills**: None needed
  - **Skills Evaluated but Omitted**:
    - None applicable

  **Parallelization**:
  - **Can Run In Parallel**: NO
  - **Parallel Group**: Wave 5 (first task)
  - **Blocks**: Tasks 14-16
  - **Blocked By**: Task 12

  **References**:

  **Pattern References**:
  - None (verification only)

  **WHY Each Reference Matters**:
  - Final gate before committing changes

  **Acceptance Criteria**:

  **Automated Verification:**
  ```bash
  # Agent executes:
  cd API_GestionNotes/ManageNotes && mvn clean compile
  # Assert: Exit code 0
  # Assert: Output contains "BUILD SUCCESS"
  # Assert: No "ERROR" lines
  
  # Verify all MapStruct implementations exist
  ls target/generated-sources/annotations/com/university/ManageNotes/mapper/ | grep "Impl.java" | wc -l
  # Assert: Count is 13 (all mappers generated)
  
  # Check for remaining ModelMapper references
  grep -rn "ModelMapper" API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/ | grep -v "\.class"
  # Assert: No matches (complete removal)
  
  # Package verification
  cd API_GestionNotes/ManageNotes && mvn package -DskipTests
  # Assert: Exit code 0
  ```

  **Evidence to Capture:**
  - [ ] Full Maven compile output
  - [ ] List of generated mapper implementations
  - [ ] Grep results showing no ModelMapper
  - [ ] Package build success

  **Commit**: NO (verification only)

---

- [ ] 14. Create Commit 1: pom.xml changes

  **What to do**:
  - Verify pom.xml is already staged (from task 1)
  - Create atomic commit with message: `build(deps): migrate from ModelMapper to MapStruct 1.6.3`
  - Include commit body describing changes:
    - Upgraded MapStruct 1.5.5 → 1.6.3
    - Removed ModelMapper 3.2.4 dependency
    - Added maven-compiler-plugin with annotation processors
    - Added lombok-mapstruct-binding for compatibility

  **Must NOT do**:
  - Include other files in this commit
  - Skip commit message body

  **Recommended Agent Profile**:
  - **Category**: `quick`
    - Reason: Single git commit operation
  - **Skills**: `git-master`
    - Why needed: Atomic commit creation with proper message formatting
  - **Skills Evaluated but Omitted**:
    - None applicable

  **Parallelization**:
  - **Can Run In Parallel**: NO
  - **Parallel Group**: Wave 5 (sequential commits)
  - **Blocks**: Task 15
  - **Blocked By**: Task 13

  **References**:

  **Pattern References**:
  - `API_GestionNotes/ManageNotes/pom.xml` - File to commit

  **WHY Each Reference Matters**:
  - First commit isolates dependency changes

  **Acceptance Criteria**:

  **Automated Verification:**
  ```bash
  # Agent executes:
  git log -1 --format="%s"
  # Assert: Matches "build(deps): migrate from ModelMapper to MapStruct 1.6.3"
  
  git log -1 --name-only
  # Assert: Only shows pom.xml
  
  git status
  # Assert: Shows unstaged changes for mapper and service files
  ```

  **Evidence to Capture:**
  - [ ] Git commit message
  - [ ] Git log showing single file
  - [ ] Git status showing remaining changes

  **Commit**: ALREADY DONE (this task IS the commit)

---

- [ ] 15. Create Commit 2: MapStruct mappers and service migration

  **What to do**:
  - Stage all mapper interface files (13 files in src/main/java/.../mapper/)
  - Stage all migrated service implementation files (7 files)
  - Stage AppConfig.java (ModelMapper removal)
  - Create atomic commit with message: `refactor(services): replace ModelMapper with MapStruct mappers`
  - Include commit body:
    - Created 13 MapStruct mapper interfaces
    - Migrated 7 service implementations (25 map() calls)
    - Removed ModelMapper configuration from AppConfig
    - Removed 3 unused ModelMapper injections

  **Must NOT do**:
  - Include controller changes (those are commit 3)
  - Include pom.xml (already committed)
  - Skip verification before commit

  **Recommended Agent Profile**:
  - **Category**: `quick`
    - Reason: Git commit operation with multiple files
  - **Skills**: `git-master`
    - Why needed: Multi-file atomic commit with proper staging
  - **Skills Evaluated but Omitted**:
    - None applicable

  **Parallelization**:
  - **Can Run In Parallel**: NO
  - **Parallel Group**: Wave 5 (sequential commits)
  - **Blocks**: Task 16
  - **Blocked By**: Task 14

  **References**:

  **Pattern References**:
  - All mapper files in `src/main/java/com/university/ManageNotes/mapper/`
  - All service files modified in tasks 4-11
  - `src/main/java/com/university/ManageNotes/config/AppConfig.java`

  **WHY Each Reference Matters**:
  - Second commit groups mapper creation and service migration logically

  **Acceptance Criteria**:

  **Automated Verification:**
  ```bash
  # Agent executes:
  git log -1 --format="%s"
  # Assert: Matches "refactor(services): replace ModelMapper with MapStruct mappers"
  
  git log -1 --name-only | grep "mapper" | wc -l
  # Assert: Shows 13 mapper files
  
  git log -1 --name-only | grep "service/impl" | wc -l
  # Assert: Shows 7 service files
  
  git status
  # Assert: Shows unstaged controller changes only
  ```

  **Evidence to Capture:**
  - [ ] Git commit message
  - [ ] Git log showing mapper and service files
  - [ ] Git status showing controller changes remain

  **Commit**: ALREADY DONE (this task IS the commit)

---

- [ ] 16. Create Commit 3: Controller return type fixes

  **What to do**:
  - Stage all modified controller files (8 controllers)
  - Create atomic commit with message: `fix(controllers): correct return types from Request to Response DTOs`
  - Include commit body:
    - Fixed 21 controller methods returning Request DTOs
    - Updated AdminController (1 method)
    - Updated SemesterController (2 methods)
    - Updated RevendicationController (1 method)
    - Updated SubjectController (3 methods)
    - Updated DepartmentController (3 methods)
    - Updated GradeController (2 methods)
    - Updated StudentController (1 method)
    - Updated RevendicationPeriodController (2 methods)

  **Must NOT do**:
  - Include non-controller files
  - Skip final verification

  **Recommended Agent Profile**:
  - **Category**: `quick`
    - Reason: Final git commit operation
  - **Skills**: `git-master`
    - Why needed: Atomic commit with proper message
  - **Skills Evaluated but Omitted**:
    - None applicable

  **Parallelization**:
  - **Can Run In Parallel**: NO
  - **Parallel Group**: Wave 5 (final commit)
  - **Blocks**: None (complete)
  - **Blocked By**: Task 15

  **References**:

  **Pattern References**:
  - All controller files modified in task 12

  **WHY Each Reference Matters**:
  - Third commit isolates controller fixes for clear git history

  **Acceptance Criteria**:

  **Automated Verification:**
  ```bash
  # Agent executes:
  git log -1 --format="%s"
  # Assert: Matches "fix(controllers): correct return types from Request to Response DTOs"
  
  git log -1 --name-only | grep "controller" | wc -l
  # Assert: Shows 8 controller files
  
  git status
  # Assert: Working tree clean (no unstaged changes)
  
  # Final verification
  cd API_GestionNotes/ManageNotes && mvn clean compile
  # Assert: Exit code 0 (everything still compiles)
  ```

  **Evidence to Capture:**
  - [ ] Git commit message
  - [ ] Git log showing controller files
  - [ ] Clean working tree status
  - [ ] Final compilation success

  **Commit**: ALREADY DONE (this task IS the commit)

---

## Commit Strategy

| After Task | Message | Files | Verification |
|------------|---------|-------|--------------|
| 1 | `build(deps): migrate from ModelMapper to MapStruct 1.6.3` | pom.xml | mvn clean compile -DskipTests |
| 12 | `fix(controllers): correct return types from Request to Response DTOs` | 8 controllers | mvn clean compile -DskipTests |
| 2-11 | `refactor(services): replace ModelMapper with MapStruct mappers` | 13 mappers, 7 services, AppConfig.java | mvn clean compile -DskipTests |

**Note**: Commit order adjusted - controllers commit separately (task 12), mappers+services group together (tasks 2-11)

---

## Success Criteria

### Verification Commands
```bash
# All ModelMapper references removed
grep -rn "ModelMapper" API_GestionNotes/ManageNotes/src/main/java/com/university/ManageNotes/ | grep -v "\.class"
# Expected: No matches

# All MapStruct mappers generated
ls target/generated-sources/annotations/com/university/ManageNotes/mapper/ | grep "Impl.java" | wc -l
# Expected: 13

# Compilation success
cd API_GestionNotes/ManageNotes && mvn clean compile
# Expected: BUILD SUCCESS

# Package success
cd API_GestionNotes/ManageNotes && mvn package -DskipTests
# Expected: BUILD SUCCESS

# Verify 3 commits
git log --oneline -3
# Expected: Shows 3 commits with proper messages
```

### Final Checklist
- [ ] All 25 ModelMapper.map() calls replaced with MapStruct
- [ ] All 3 unused ModelMapper injections removed
- [ ] All 21 controller return types corrected
- [ ] ModelMapper configuration removed from AppConfig.java
- [ ] All 13 MapStruct mapper interfaces created
- [ ] All 13 MapStruct implementations generated
- [ ] No ModelMapper references remain in codebase
- [ ] Maven clean compile succeeds
- [ ] Maven package succeeds
- [ ] 3 atomic commits created with proper messages
- [ ] Git working tree clean
