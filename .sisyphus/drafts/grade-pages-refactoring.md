# Draft: Grade Pages Refactoring

## User Request Summary
Refactor 7 duplicate React/TypeScript grade page components (656 lines total) into a reusable pattern using a custom hook and shared component. Target: ~150 lines (77% reduction).

## Requirements (confirmed)
1. ✅ Create custom hook: `useGradeEditor(level, semester)` with ALL shared logic
2. ✅ Create single reusable `GradingPage` component using the hook
3. ✅ Refactor all 7 pages to use the hook (~5-10 lines each)
4. ✅ Verify build: `cd react && npm run build`
5. ✅ Create git commit with /git-master skill

## Success Criteria
- Hook extracts: state management, localStorage logic, handlers, filtering logic
- GradingPage component: reusable with props for level, semester, title, code
- Each page becomes ~5-10 lines (imports + config + component usage)
- Build succeeds with 0 errors
- Line reduction: 656 → ~150 lines (77% reduction)

## Files Affected (7 pages)
1. react/src/features/licence/licence1/index.tsx (127 lines)
2. react/src/features/licence/licence2/index.tsx (126 lines)
3. react/src/features/licence/licence3/index.tsx (126 lines)
4. react/src/features/master/master1/index.tsx (126 lines)
5. react/src/features/master/master2/index.tsx (126 lines)
6. react/src/features/student/sem1/index.tsx (19 lines - partial)
7. react/src/features/student/sem2/index.tsx (6 lines - minimal stub)

## Duplicate Patterns Identified
- Same state: isTableEditable, tableData, editedData, searchValue
- Same localStorage pattern: `${level}${semester}_tableData`
- Same handlers: handleEdit, handleConfirm, filteredTableData useMemo
- Same components: GradesHeader, EditableGradesTable
- **Differences**: title, code, localStorage key, level metadata

## Open Questions
1. **localStorage key naming**: Should we standardize the key format? Currently uses patterns like `licence2_tableData`, `master1_tableData`. Should the hook generate keys as `${level}_${semester}_tableData`?

2. **Partial implementations**: sem1 (19 lines) and sem2 (6 lines) are incomplete. Should they be completed as part of this refactoring, or just converted to the new pattern with their current state?

3. **FakeStudents data**: Currently using mock data from features/user/data. Should the hook abstract the data source, or keep it hardcoded for now?

4. **Type safety**: Should we create TypeScript types for the level/semester config (e.g., `GradePageConfig` interface)?

5. **Hook location**: Confirm placement in `react/src/hooks/useGradeEditor.ts`?

6. **Component location**: Where should the reusable `GradingPage` component live? Options:
   - `react/src/components/GradingPage.tsx` (shared components)
   - `react/src/features/grades/GradingPage.tsx` (new feature folder)
   - Other?

7. **Testing scope**: Should we include test cases for the new hook, or just verify build + manual smoke test?

## Research Findings (COMPLETED)

### Codebase Analysis (bg_cfa81172)
**Exact Duplication Pattern Found:**
- 5 complete pages (licence1/2/3, master1/2): 126-127 lines each with 93% duplication
- 2 incomplete pages (sem1/2): 19 and 6 lines (stubs)
- Total: 656 lines → Target: ~150 lines (77% reduction)

**Shared Logic (Identical Across All Pages):**
- State: `isTableEditable`, `tableData`, `editedData`, `searchValue`
- Handlers: `handleEdit()`, `handleConfirm()`
- Filtering: `filteredTableData` via useMemo
- useEffect: sync editedData when entering edit mode
- Components: `<GradesHeader />` + `<EditableGradesTable />`

**Unique Per Page (Must Be Parameterized):**
- Title: "L1", "L2", "L3", "M1", "M2"
- Subject code: MATH101, MATH201, MATH301, MATH401
- localStorage keys: 'licence2_tableData', 'licence3_tableData', 'master1_tableData', 'master2_tableData'
- Licence1 special case: uses FakeStudents, no localStorage

**Component Props Discovered:**
- `EditableGradesTable` expects: extraColumns, isEditable, data, onGradesChange, onEdit, onConfirm, isDataEditable, setIsDataEditable, onSearch
- `GradesHeader` expects: period, topic?, code?, level, NC, CANT, title?, totalStudents?

**Existing Hook Pattern (from usePageTitle.tsx):**
```typescript
export const usePageTitle = (key: string) => {
  const { setPageTitle } = useContext(PageTitleContext)
  useEffect(() => {
    setPageTitle?.call(this, key)
  }, [key, setPageTitle]);
}
```

**Type Locations:**
- Student type: `src/api/reponse-dto/user.res.dto.ts` (studentResDto)
- Barrel exports: Project uses extensive index.ts barrels (components, hooks, features)

### React Hooks Best Practices (bg_e09a0ef7)
**Key Patterns to Apply:**
1. **Return Object (>3 values)**: Return structured object with state, handlers, computed values
2. **localStorage Pattern**: Use lazy initializer + JSON error handling + SSR check
3. **TypeScript Generics**: Use interfaces for params and return types
4. **Memoization**: useMemo for computed values, useCallback for stable handlers
5. **Composition**: Build from smaller hooks (can compose useLocalStorage if needed)

**Recommended Structure:**
```typescript
interface UseGradeEditorParams {
  level: string;
  semester: string;
  storageKey?: string;
  initialData?: Student[];
}

interface UseGradeEditorReturn {
  // State
  tableData: Student[];
  editedData: Student[];
  isTableEditable: boolean;
  searchValue: string;
  // Computed
  filteredTableData: Student[];
  // Handlers
  handleEdit: () => void;
  handleConfirm: () => void;
  setSearchValue: (value: string) => void;
  setIsTableEditable: (value: boolean) => void;
  setEditedData: (data: Student[]) => void;
}
```

### TypeScript Build Config (bg_2d33c176)
**Build Command:** `npm run build` → runs `tsc -b && vite build`
- tsc -b: TypeScript project references check (strict mode ON)
- vite build: Production bundle

**Verification Command:** `cd react && npm run build`

**Type Safety:**
- noEmit: true (type checking only, Vite handles bundling)
- strict: true (all strict checks enabled)
- Uses project references (tsconfig.json → tsconfig.app.json + tsconfig.node.json)

**Barrel Export Pattern:**
- Project uses index.ts extensively
- New hook: Add to `src/hooks/index.ts` barrel
- New component: Add to `src/components/index.ts` barrel
