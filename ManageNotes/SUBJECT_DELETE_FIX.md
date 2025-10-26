# Subject Delete Issue - Fix Documentation

## Problem Description
When attempting to delete a subject via the DELETE endpoint `/api/admin/subject/{id}`, the API returns a 200 OK status with the subject data, but the subject is not actually deleted from the database and remains visible.

## Root Cause
The `Subject` entity has a one-to-many relationship with the `Grades` entity:
```java
@OneToMany(mappedBy = "subject")
private List<Grades> grades = new ArrayList<>();
```

However, this relationship did NOT have cascade delete configured. When the application attempted to delete a subject that had associated grades records, the database foreign key constraints prevented the deletion, but the operation didn't throw an explicit error - it silently failed.

## Solution Applied
Added cascade delete and orphan removal to the `@OneToMany` relationship in the `Subject` model:

**File Modified:** `src/main/java/com/university/ManageNotes/model/Subject.java`

**Change:**
```java
// Before
@OneToMany(mappedBy = "subject")
private List<Grades> grades = new ArrayList<>();

// After
@OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Grades> grades = new ArrayList<>();
```

## What This Fix Does
- **`cascade = CascadeType.ALL`**: Ensures that when a Subject is deleted, all associated Grades records are automatically deleted as well
- **`orphanRemoval = true`**: Automatically removes any Grades records that are no longer referenced by a Subject

## Impact
- ✅ Subjects can now be successfully deleted even if they have associated grades
- ✅ All related grades records will be automatically cleaned up
- ✅ Database referential integrity is maintained
- ✅ No changes needed to the service layer or controller

## Testing Steps
1. Create a subject
2. Create grades for that subject
3. Attempt to delete the subject via DELETE `/api/admin/subject/{id}`
4. Verify:
   - Status code is 200 OK
   - Response returns the subject data
   - Subject is no longer visible in the subjects list
   - Associated grades are also deleted

## Note
This is a common pattern in JPA for handling parent-child relationships. The cascade delete ensures data consistency when removing parent entities that have dependent child records.

