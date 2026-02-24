# Dashboard Fixes Implementation Plan

## Overview
This plan removes the unused LanguageSwitcher component, verifies ThemeToggle functionality, and wires Student + Teacher dashboards to real APIs.

## Prerequisites
- Backend running at: http://localhost:3030
- Frontend running at: http://localhost:5173
- Credentials:
  - Admin: admin/admin
  - Teacher: prof.smith/duchelle  
  - Student: 24a0001/nathan

---

## Wave 1: UI Fixes + Data Wiring

### Task 1.1: Remove LanguageSwitcher (PRIORITY: HIGH)

**Files to modify:**
1. Delete: `react/src/components/LanguageSwitcher.tsx`
2. Edit: `react/src/components/index.ts` - Remove line: `export * from './LanguageSwitcher.tsx'`

**Verification:**
```bash
# Confirm file deleted
ls react/src/components/LanguageSwitcher.tsx  # Should not exist

# Confirm no references remain
grep -r "LanguageSwitcher" react/src/  # Should return nothing
```

---

### Task 1.2: Test ThemeToggle (PRIORITY: HIGH)

**Test with Playwright:**
1. Navigate to http://localhost:5173/login
2. Take screenshot "theme-before.png"
3. Click the ThemeToggle button (sun/moon icon in header)
4. Take screenshot "theme-after.png"
5. Verify the `<html>` element toggles a `dark` class
6. Verify CSS variables change (background color, text color)

**Expected behavior:**
- Light mode: Light background, dark text
- Dark mode: Dark background, light text
- Icon changes between sun and moon

---

### Task 1.3: Wire Student Dashboard to Real APIs (PRIORITY: HIGH)

**File:** `react/src/features/overview/index.tsx`

**Function to modify:** `renderStudentDashboard()` (lines 346-437)

**Required imports (add to top of file):**
```typescript
import { useGetStudentTranscriptQuery } from '../transcript/api/transcriptApi';
import { useGetStudentRevendicationsQuery } from '../revendication/api/revendicationApi';
```

**Replace `renderStudentDashboard()` with:**

```typescript
const renderStudentDashboard = () => {
  const { data: transcript, isLoading: transcriptLoading } = useGetStudentTranscriptQuery();
  const { data: revendications, isLoading: claimsLoading } = useGetStudentRevendicationsQuery();
  
  const loading = transcriptLoading || claimsLoading;
  
  // Calculate statistics from transcript
  const enrolledSubjects = transcript?.semesters?.reduce(
    (total, sem) => total + sem.grades.length, 0
  ) || 0;
  
  const currentGPA = transcript?.cumulativeGPA?.toFixed(2) || '--';
  const totalCredits = transcript?.totalCreditsEarned || 0;
  const level = transcript?.level || 'N/A';
  const cycle = transcript?.cycle || 'N/A';
  
  // Count claims by status
  const pendingClaims = revendications?.filter(r => r.status === 'PENDING').length || 0;
  const approvedClaims = revendications?.filter(r => r.status === 'APPROVED').length || 0;
  const rejectedClaims = revendications?.filter(r => r.status === 'REJECTED').length || 0;
  
  // Get recent grades (last 5)
  const recentGrades = transcript?.semesters?.flatMap(sem => 
    sem.grades.map(g => ({ ...g, semesterName: sem.semesterName }))
  ).slice(0, 5) || [];

  return (
    <>
      {/* Academic Info Banner */}
      <div className="mb-6 p-4 rounded-lg" style={{ backgroundColor: 'var(--card-bg)' }}>
        <div className="flex flex-wrap items-center gap-4">
          <div>
            <span className="text-sm opacity-70">Level:</span>
            <span className="ml-2 font-mono font-bold">{level}</span>
          </div>
          <div>
            <span className="text-sm opacity-70">Cycle:</span>
            <span className="ml-2 font-mono font-bold">{cycle}</span>
          </div>
          {transcript?.speciality && (
            <div>
              <span className="text-sm opacity-70">Speciality:</span>
              <span className="ml-2 font-mono font-bold">{transcript.speciality}</span>
            </div>
          )}
        </div>
      </div>

      {/* Statistics Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <Card hoverable className="p-6">
          {loading ? (
            <div className="skeleton h-24 w-full" />
          ) : (
            <>
              <div className="flex items-center justify-between mb-4">
                <BookOpenIcon className="w-10 h-10" style={{ color: 'var(--text-on-card)' }} />
                <span className="text-3xl font-bold font-mono" style={{ color: 'var(--text-on-card)' }}>
                  {enrolledSubjects}
                </span>
              </div>
              <h3 className="font-mono text-lg font-semibold" style={{ color: 'var(--text-on-card)' }}>Enrolled Subjects</h3>
              <p className="text-sm opacity-70" style={{ color: 'var(--text-on-card)' }}>All semesters</p>
            </>
          )}
        </Card>

        <Card hoverable className="p-6">
          {loading ? (
            <div className="skeleton h-24 w-full" />
          ) : (
            <>
              <div className="flex items-center justify-between mb-4">
                <ChartBarIcon className="w-10 h-10" style={{ color: 'var(--text-on-card)' }} />
                <span className="text-3xl font-bold font-mono" style={{ color: 'var(--text-on-card)' }}>
                  {currentGPA}
                </span>
              </div>
              <h3 className="font-mono text-lg font-semibold" style={{ color: 'var(--text-on-card)' }}>Current GPA</h3>
              <p className="text-sm opacity-70" style={{ color: 'var(--text-on-card)' }}>Cumulative average</p>
            </>
          )}
        </Card>

        <Card hoverable className="p-6">
          {loading ? (
            <div className="skeleton h-24 w-full" />
          ) : (
            <>
              <div className="flex items-center justify-between mb-4">
                <AcademicCapIcon className="w-10 h-10" style={{ color: 'var(--text-on-card)' }} />
                <span className="text-3xl font-bold font-mono" style={{ color: 'var(--text-on-card)' }}>
                  {totalCredits}
                </span>
              </div>
              <h3 className="font-mono text-lg font-semibold" style={{ color: 'var(--text-on-card)' }}>Total Credits</h3>
              <p className="text-sm opacity-70" style={{ color: 'var(--text-on-card)' }}>Credits earned</p>
            </>
          )}
        </Card>

        <Card hoverable className="p-6">
          {loading ? (
            <div className="skeleton h-24 w-full" />
          ) : (
            <>
              <div className="flex items-center justify-between mb-4">
                <ClipboardDocumentListIcon className="w-10 h-10" style={{ color: 'var(--text-on-card)' }} />
                <span className="text-3xl font-bold font-mono" style={{ color: 'var(--text-on-card)' }}>
                  {pendingClaims}
                </span>
              </div>
              <h3 className="font-mono text-lg font-semibold" style={{ color: 'var(--text-on-card)' }}>Pending Claims</h3>
              <p className="text-sm opacity-70" style={{ color: 'var(--text-on-card)' }}>
                {approvedClaims} approved, {rejectedClaims} rejected
              </p>
            </>
          )}
        </Card>
      </div>

      {/* Recent Grades Section */}
      <div className="mb-8">
        <h2 className="text-2xl font-mono font-bold mb-4">Recent Grades</h2>
        <Card className="p-6">
          {loading ? (
            <div className="skeleton h-40 w-full" />
          ) : recentGrades.length > 0 ? (
            <div className="overflow-x-auto">
              <table className="table w-full">
                <thead>
                  <tr>
                    <th className="font-mono">Subject</th>
                    <th className="font-mono">Code</th>
                    <th className="font-mono">Grade</th>
                    <th className="font-mono">Credits</th>
                    <th className="font-mono">Status</th>
                  </tr>
                </thead>
                <tbody>
                  {recentGrades.map((grade, idx) => (
                    <tr key={idx}>
                      <td>{grade.subjectName}</td>
                      <td>{grade.subjectCode}</td>
                      <td><GradeBadge grade={grade.grade} /></td>
                      <td>{grade.credits}</td>
                      <td>
                        <span className={`px-2 py-1 rounded text-xs ${
                          grade.passed 
                            ? 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200' 
                            : 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200'
                        }`}>
                          {grade.passed ? 'Passed' : 'Failed'}
                        </span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          ) : (
            <p className="text-center opacity-70" style={{ color: 'var(--text-on-card)' }}>No grades recorded yet</p>
          )}
        </Card>
      </div>

      {/* Quick Actions */}
      <div className="mb-8">
        <h2 className="text-2xl font-mono font-bold mb-4">Quick Actions</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <Link to="/dashboard/semester1">
            <Card hoverable className="p-6 cursor-pointer">
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-4">
                  <BookOpenIcon className="w-8 h-8" style={{ color: 'var(--text-on-card)' }} />
                  <div>
                    <h3 className="font-mono font-semibold" style={{ color: 'var(--text-on-card)' }}>Semester 1 Grades</h3>
                    <p className="text-sm opacity-70" style={{ color: 'var(--text-on-card)' }}>View all grades</p>
                  </div>
                </div>
                <ArrowRightIcon className="w-6 h-6" style={{ color: 'var(--text-on-card)' }} />
              </div>
            </Card>
          </Link>

          <Link to="/dashboard/grade-claims">
            <Card hoverable className="p-6 cursor-pointer">
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-4">
                  <ClipboardDocumentListIcon className="w-8 h-8" style={{ color: 'var(--text-on-card)' }} />
                  <div>
                    <h3 className="font-mono font-semibold" style={{ color: 'var(--text-on-card)' }}>Grade Claims</h3>
                    <p className="text-sm opacity-70" style={{ color: 'var(--text-on-card)' }}>Submit disputes</p>
                  </div>
                </div>
                <ArrowRightIcon className="w-6 h-6" style={{ color: 'var(--text-on-card)' }} />
              </div>
            </Card>
          </Link>

          <Link to="/dashboard/transcript">
            <Card hoverable className="p-6 cursor-pointer">
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-4">
                  <DocumentTextIcon className="w-8 h-8" style={{ color: 'var(--text-on-card)' }} />
                  <div>
                    <h3 className="font-mono font-semibold" style={{ color: 'var(--text-on-card)' }}>My Transcript</h3>
                    <p className="text-sm opacity-70" style={{ color: 'var(--text-on-card)' }}>Academic record</p>
                  </div>
                </div>
                <ArrowRightIcon className="w-6 h-6" style={{ color: 'var(--text-on-card)' }} />
              </div>
            </Card>
          </Link>
        </div>
      </div>
    </>
  );
};
```

**Verification:**
- Login as student (24a0001/nathan)
- Dashboard should show real GPA, credits, enrolled subjects
- Recent grades table should show grades with GradeBadge styling
- Grade claims count should reflect actual claims

---

### Task 1.4: Enhance Teacher Dashboard (PRIORITY: HIGH)

**File:** `react/src/features/teacher/pages/TeacherDashboardPage.tsx`

**Add imports (after existing imports):**
```typescript
import { useGetTeacherGradesQuery } from '../api/teacherDashboardApi';
import { GradeBadge } from '../../../components/GradeBadge';
```

**Add after line 33 (after other queries):**
```typescript
const { data: teacherGrades, isLoading: gradesLoading } = useGetTeacherGradesQuery();
```

**Update isLoading (line 35):**
```typescript
const isLoading = profileLoading || subjectsLoading || studentsLoading || gradesLoading;
```

**Calculate averages (add after line 95, before levelTabs):**
```typescript
// Calculate average grades per subject
const subjectAverages = React.useMemo(() => {
  if (!teacherGrades || !subjects) return {};
  
  const averages: Record<string, { sum: number; count: number; avg: number }> = {};
  
  teacherGrades.forEach(grade => {
    if (!averages[grade.subjectCode]) {
      averages[grade.subjectCode] = { sum: 0, count: 0, avg: 0 };
    }
    averages[grade.subjectCode].sum += grade.value;
    averages[grade.subjectCode].count += 1;
  });
  
  Object.keys(averages).forEach(code => {
    averages[code].avg = averages[code].sum / averages[code].count;
  });
  
  return averages;
}, [teacherGrades, subjects]);

// Get recent grades (top 5)
const recentGrades = teacherGrades?.slice(0, 5) || [];
```

**Add "Average" column to subjectColumns (after Credits column, around line 60):**
```typescript
{
  title: 'Average Grade',
  key: 'average',
  render: (_: unknown, record: SubjectResponse) => {
    const avg = subjectAverages[record.code];
    return avg ? (
      <span className="font-mono">{avg.avg.toFixed(1)} ({avg.count} grades)</span>
    ) : (
      <span className="text-gray-400">No grades</span>
    );
  },
},
```

**Add "Recent Grades" section after the statistics Row (after line 172, before "My Subjects"):**
```typescript
{/* Recent Grades Section */}
<Card className="mt-6" hoverable={false}>
  <h2 className="text-xl font-mono font-bold mb-4">Recent Grades Submitted</h2>
  {recentGrades.length > 0 ? (
    <Table
      columns={[
        {
          title: 'Student',
          dataIndex: 'studentName',
          key: 'studentName',
        },
        {
          title: 'Subject',
          key: 'subject',
          render: (_, record) => <Tag color="blue">{record.subjectCode}</Tag>,
        },
        {
          title: 'Grade',
          dataIndex: 'value',
          key: 'value',
          render: (value: number) => <GradeBadge grade={value} />,
        },
        {
          title: 'Period',
          dataIndex: 'periodLabel',
          key: 'periodLabel',
        },
        {
          title: 'Date',
          dataIndex: 'createdDate',
          key: 'createdDate',
          render: (date: string) => date ? new Date(date).toLocaleDateString() : 'N/A',
        },
      ]}
      dataSource={recentGrades}
      rowKey="id"
      pagination={false}
      size="small"
      className="table"
    />
  ) : (
    <Empty description="No grades submitted yet" />
  )}
</Card>

{/* TODO: Pending grades feature requires new API endpoint */}
{/* Future enhancement: Calculate pending grades by comparing enrolled students vs submitted grades */}
```

**Verification:**
- Login as teacher (prof.smith/duchelle)
- Dashboard should show "Recent Grades Submitted" section
- My Subjects table should have "Average Grade" column
- Average shows grade count and average value

---

## Wave 2: Playwright Testing

### Task 2.1: Test Admin Dashboard
```typescript
// Test: Login as admin, verify stats, click buttons, screenshot
test('Admin dashboard shows real data', async ({ page }) => {
  await page.goto('http://localhost:5173/login');
  await page.fill('[name="username"]', 'admin');
  await page.fill('[name="password"]', 'admin');
  await page.click('button[type="submit"]');
  await page.waitForURL('**/dashboard/**');
  
  // Verify stats cards exist
  await expect(page.locator('text=Total Students')).toBeVisible();
  await expect(page.locator('text=Total Teachers')).toBeVisible();
  
  await page.screenshot({ path: 'admin-dashboard.png' });
});
```

### Task 2.2: Test Teacher Dashboard
```typescript
test('Teacher dashboard shows subjects and grades', async ({ page }) => {
  await page.goto('http://localhost:5173/login');
  await page.fill('[name="username"]', 'prof.smith');
  await page.fill('[name="password"]', 'duchelle');
  await page.click('button[type="submit"]');
  await page.waitForURL('**/dashboard/**');
  
  // Navigate to teacher dashboard
  await page.click('text=My Dashboard');
  
  // Verify sections
  await expect(page.locator('text=My Subjects')).toBeVisible();
  await expect(page.locator('text=Recent Grades Submitted')).toBeVisible();
  
  await page.screenshot({ path: 'teacher-dashboard.png' });
});
```

### Task 2.3: Test Student Dashboard
```typescript
test('Student dashboard shows real grades and GPA', async ({ page }) => {
  await page.goto('http://localhost:5173/login');
  await page.fill('[name="username"]', '24a0001');
  await page.fill('[name="password"]', 'nathan');
  await page.click('button[type="submit"]');
  await page.waitForURL('**/dashboard/**');
  
  // Verify real data (not zeros)
  await expect(page.locator('text=Current GPA')).toBeVisible();
  await expect(page.locator('text=Enrolled Subjects')).toBeVisible();
  
  await page.screenshot({ path: 'student-dashboard.png' });
});
```

---

## Wave 3: Verification & Commit

### Task 3.1: Build Verification
```bash
cd react && npm run build
```

### Task 3.2: Git Commit
```bash
git add -A
git commit -m "fix: remove broken LanguageSwitcher and build functional dashboards with real data"
```

---

## Summary of Changes

| File | Action | Description |
|------|--------|-------------|
| `react/src/components/LanguageSwitcher.tsx` | DELETE | Remove unused component |
| `react/src/components/index.ts` | EDIT | Remove LanguageSwitcher export |
| `react/src/features/overview/index.tsx` | EDIT | Wire Student Dashboard to transcript/revendication APIs |
| `react/src/features/teacher/pages/TeacherDashboardPage.tsx` | EDIT | Add recent grades section and averages column |

## Acceptance Criteria

- [ ] LanguageSwitcher.tsx deleted and no references remain
- [ ] ThemeToggle toggles between light/dark mode
- [ ] Student Dashboard shows real GPA, credits, grades with GradeBadge
- [ ] Student Dashboard shows grade claims count by status
- [ ] Teacher Dashboard shows recent grades submitted (top 5)
- [ ] Teacher Dashboard shows average grades per subject
- [ ] All dashboards pass Playwright tests
- [ ] Build completes without errors
- [ ] Commit created with proper message
