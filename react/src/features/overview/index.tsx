import { useAppSelector } from '../../store';
import { Card } from '../../components/Card';
import { GradeBadge } from '../../components/GradeBadge';
import { Link } from 'react-router-dom';
import {
  UserGroupIcon,
  AcademicCapIcon,
  BookOpenIcon,
  BuildingOfficeIcon,
  ChartBarIcon,
  ClipboardDocumentListIcon,
  DocumentTextIcon,
  Cog6ToothIcon,
  ArrowRightIcon,
} from '@heroicons/react/24/outline';
import { useGetStudentsQuery } from '../students/api/studentsApi';
import { useGetTeachersQuery } from '../admin/api/teachersApi';
import { useGetSubjectsQuery } from '../admin/api/subjectApi';
import { useGetDepartmentsQuery } from '../admin/api/departmentApi';
import { useGetTeacherGradesQuery, useGetTeacherSubjectsQuery } from '../teacher/api/teacherDashboardApi';
import { useGetStudentTranscriptQuery } from '../transcript/api/transcriptApi';
import { useGetStudentRevendicationsQuery } from '../revendication/api/revendicationApi';
import { Role } from '../../api/enums';

export const Overview = () => {
  const userProfile = useAppSelector((state) => state.user.profile);
  
  const { data: students, isLoading: studentsLoading } = useGetStudentsQuery(undefined, {
    skip: userProfile?.role !== Role.ADMIN,
  });
  const { data: teachers, isLoading: teachersLoading } = useGetTeachersQuery(undefined, {
    skip: userProfile?.role !== Role.ADMIN,
  });
  const { data: subjects, isLoading: subjectsLoading } = useGetSubjectsQuery(undefined, {
    skip: userProfile?.role !== Role.ADMIN,
  });
  const { data: departments, isLoading: departmentsLoading } = useGetDepartmentsQuery(undefined, {
    skip: userProfile?.role !== Role.ADMIN,
  });
  
  const { data: teacherGrades, isLoading: teacherGradesLoading } = useGetTeacherGradesQuery(undefined, {
    skip: userProfile?.role !== Role.TEACHER,
  });
  const { data: teacherSubjects, isLoading: teacherSubjectsLoading } = useGetTeacherSubjectsQuery(undefined, {
    skip: userProfile?.role !== Role.TEACHER,
  });
  
  const { data: transcript, isLoading: transcriptLoading } = useGetStudentTranscriptQuery(undefined, {
    skip: userProfile?.role !== Role.STUDENT,
  });
  const { data: revendications, isLoading: claimsLoading } = useGetStudentRevendicationsQuery(undefined, {
    skip: userProfile?.role !== Role.STUDENT,
  });

  const currentDate = new Date().toLocaleDateString('en-US', {
    weekday: 'long',
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  });

  const getGreeting = () => {
    const hour = new Date().getHours();
    if (hour < 12) return 'Good morning';
    if (hour < 18) return 'Good afternoon';
    return 'Good evening';
  };

  const renderAdminDashboard = () => {
    const loading = studentsLoading || teachersLoading || subjectsLoading || departmentsLoading;

    return (
      <>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          <Card hoverable className="p-6">
            {loading ? (
              <div className="skeleton h-24 w-full" />
            ) : (
              <>
                <div className="flex items-center justify-between mb-4">
                  <UserGroupIcon className="w-10 h-10" style={{ color: 'var(--text-on-card)' }} />
                  <span className="text-3xl font-bold font-mono" style={{ color: 'var(--text-on-card)' }}>
                    {students?.length || 0}
                  </span>
                </div>
                <h3 className="font-mono text-lg font-semibold" style={{ color: 'var(--text-on-card)' }}>Total Students</h3>
                <p className="text-sm opacity-70" style={{ color: 'var(--text-on-card)' }}>Enrolled students</p>
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
                    {teachers?.length || 0}
                  </span>
                </div>
                <h3 className="font-mono text-lg font-semibold" style={{ color: 'var(--text-on-card)' }}>Total Teachers</h3>
                <p className="text-sm opacity-70" style={{ color: 'var(--text-on-card)' }}>Active faculty</p>
              </>
            )}
          </Card>

          <Card hoverable className="p-6">
            {loading ? (
              <div className="skeleton h-24 w-full" />
            ) : (
              <>
                <div className="flex items-center justify-between mb-4">
                  <BookOpenIcon className="w-10 h-10" style={{ color: 'var(--text-on-card)' }} />
                  <span className="text-3xl font-bold font-mono" style={{ color: 'var(--text-on-card)' }}>
                    {subjects?.length || 0}
                  </span>
                </div>
                <h3 className="font-mono text-lg font-semibold" style={{ color: 'var(--text-on-card)' }}>Total Subjects</h3>
                <p className="text-sm opacity-70" style={{ color: 'var(--text-on-card)' }}>Courses offered</p>
              </>
            )}
          </Card>

          <Card hoverable className="p-6">
            {loading ? (
              <div className="skeleton h-24 w-full" />
            ) : (
              <>
                <div className="flex items-center justify-between mb-4">
                  <BuildingOfficeIcon className="w-10 h-10" style={{ color: 'var(--text-on-card)' }} />
                  <span className="text-3xl font-bold font-mono" style={{ color: 'var(--text-on-card)' }}>
                    {departments?.length || 0}
                  </span>
                </div>
                <h3 className="font-mono text-lg font-semibold" style={{ color: 'var(--text-on-card)' }}>Departments</h3>
                <p className="text-sm opacity-70" style={{ color: 'var(--text-on-card)' }}>Academic units</p>
              </>
            )}
          </Card>
        </div>

        <div className="mb-8">
          <h2 className="text-2xl font-mono font-bold mb-4">Quick Actions</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <Link to="/dashboard/admin/users">
              <Card hoverable className="p-6 cursor-pointer">
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-4">
                    <UserGroupIcon className="w-8 h-8" style={{ color: 'var(--text-on-card)' }} />
                    <div>
                      <h3 className="font-mono font-semibold" style={{ color: 'var(--text-on-card)' }}>Manage Users</h3>
                      <p className="text-sm opacity-70" style={{ color: 'var(--text-on-card)' }}>Add, edit, or remove users</p>
                    </div>
                  </div>
                  <ArrowRightIcon className="w-6 h-6" style={{ color: 'var(--text-on-card)' }} />
                </div>
              </Card>
            </Link>

            <Link to="/dashboard/admin/subjects">
              <Card hoverable className="p-6 cursor-pointer">
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-4">
                    <BookOpenIcon className="w-8 h-8" style={{ color: 'var(--text-on-card)' }} />
                    <div>
                      <h3 className="font-mono font-semibold" style={{ color: 'var(--text-on-card)' }}>Manage Subjects</h3>
                      <p className="text-sm opacity-70" style={{ color: 'var(--text-on-card)' }}>Configure courses</p>
                    </div>
                  </div>
                  <ArrowRightIcon className="w-6 h-6" style={{ color: 'var(--text-on-card)' }} />
                </div>
              </Card>
            </Link>

            <Link to="/dashboard/admin/semesters">
              <Card hoverable className="p-6 cursor-pointer">
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-4">
                    <Cog6ToothIcon className="w-8 h-8" style={{ color: 'var(--text-on-card)' }} />
                    <div>
                      <h3 className="font-mono font-semibold" style={{ color: 'var(--text-on-card)' }}>Manage Semesters</h3>
                      <p className="text-sm opacity-70" style={{ color: 'var(--text-on-card)' }}>Academic periods</p>
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

  const renderTeacherDashboard = () => {
    const loading = teacherGradesLoading || teacherSubjectsLoading;
    const studentCount = teacherGrades ? new Set(teacherGrades.map(g => g.studentId)).size : 0;
    const gradesEntered = teacherGrades?.length || 0;

    return (
      <>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          <Card hoverable className="p-6">
            {loading ? (
              <div className="skeleton h-24 w-full" />
            ) : (
              <>
                <div className="flex items-center justify-between mb-4">
                  <UserGroupIcon className="w-10 h-10" style={{ color: 'var(--text-on-card)' }} />
                  <span className="text-3xl font-bold font-mono" style={{ color: 'var(--text-on-card)' }}>
                    {studentCount}
                  </span>
                </div>
                <h3 className="font-mono text-lg font-semibold" style={{ color: 'var(--text-on-card)' }}>My Students</h3>
                <p className="text-sm opacity-70" style={{ color: 'var(--text-on-card)' }}>Across all levels</p>
              </>
            )}
          </Card>

          <Card hoverable className="p-6">
            {loading ? (
              <div className="skeleton h-24 w-full" />
            ) : (
              <>
                <div className="flex items-center justify-between mb-4">
                  <BookOpenIcon className="w-10 h-10" style={{ color: 'var(--text-on-card)' }} />
                  <span className="text-3xl font-bold font-mono" style={{ color: 'var(--text-on-card)' }}>
                    {teacherSubjects?.length || 0}
                  </span>
                </div>
                <h3 className="font-mono text-lg font-semibold" style={{ color: 'var(--text-on-card)' }}>My Subjects</h3>
                <p className="text-sm opacity-70" style={{ color: 'var(--text-on-card)' }}>Teaching assignments</p>
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
                    {gradesEntered}
                  </span>
                </div>
                <h3 className="font-mono text-lg font-semibold" style={{ color: 'var(--text-on-card)' }}>Grades Entered</h3>
                <p className="text-sm opacity-70" style={{ color: 'var(--text-on-card)' }}>Total assessments</p>
              </>
            )}
          </Card>

          <Card hoverable className="p-6">
            <div className="flex items-center justify-between mb-4">
              <ClipboardDocumentListIcon className="w-10 h-10" style={{ color: 'var(--text-on-card)' }} />
              <span className="text-3xl font-bold font-mono" style={{ color: 'var(--text-on-card)' }}>0</span>
            </div>
            <h3 className="font-mono text-lg font-semibold" style={{ color: 'var(--text-on-card)' }}>Pending Claims</h3>
            <p className="text-sm opacity-70" style={{ color: 'var(--text-on-card)' }}>Grade disputes</p>
          </Card>
        </div>

        <div className="mb-8">
          <h2 className="text-2xl font-mono font-bold mb-4">Recent Grade Entries</h2>
          <Card className="p-6">
            {loading ? (
              <div className="skeleton h-40 w-full" />
            ) : teacherGrades && teacherGrades.length > 0 ? (
              <div className="overflow-x-auto">
                <table className="table w-full">
                  <thead>
                    <tr>
                      <th className="font-mono">Student</th>
                      <th className="font-mono">Subject</th>
                      <th className="font-mono">Grade</th>
                      <th className="font-mono">Type</th>
                      <th className="font-mono">Date</th>
                    </tr>
                  </thead>
                  <tbody>
                    {teacherGrades.slice(0, 5).map((grade) => (
                      <tr key={grade.id}>
                        <td>{grade.studentName}</td>
                        <td>{grade.subjectName}</td>
                        <td><GradeBadge grade={grade.value} /></td>
                        <td>{grade.periodLabel}</td>
                        <td>{grade.createdDate ? new Date(grade.createdDate).toLocaleDateString() : 'N/A'}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            ) : (
              <p className="text-center opacity-70" style={{ color: 'var(--text-on-card)' }}>No recent grade entries</p>
            )}
          </Card>
        </div>

        <div className="mb-8">
          <h2 className="text-2xl font-mono font-bold mb-4">Quick Actions</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <Link to="/dashboard/teacher-dashboard">
              <Card hoverable className="p-6 cursor-pointer">
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-4">
                    <ChartBarIcon className="w-8 h-8" style={{ color: 'var(--text-on-card)' }} />
                    <div>
                      <h3 className="font-mono font-semibold" style={{ color: 'var(--text-on-card)' }}>My Dashboard</h3>
                      <p className="text-sm opacity-70" style={{ color: 'var(--text-on-card)' }}>Grade management</p>
                    </div>
                  </div>
                  <ArrowRightIcon className="w-6 h-6" style={{ color: 'var(--text-on-card)' }} />
                </div>
              </Card>
            </Link>

            <Link to="/dashboard/grade-claims/review">
              <Card hoverable className="p-6 cursor-pointer">
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-4">
                    <ClipboardDocumentListIcon className="w-8 h-8" style={{ color: 'var(--text-on-card)' }} />
                    <div>
                      <h3 className="font-mono font-semibold" style={{ color: 'var(--text-on-card)' }}>Review Claims</h3>
                      <p className="text-sm opacity-70" style={{ color: 'var(--text-on-card)' }}>Student disputes</p>
                    </div>
                  </div>
                  <ArrowRightIcon className="w-6 h-6" style={{ color: 'var(--text-on-card)' }} />
                </div>
              </Card>
            </Link>

            <Link to="/dashboard/profile">
              <Card hoverable className="p-6 cursor-pointer">
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-4">
                    <UserGroupIcon className="w-8 h-8" style={{ color: 'var(--text-on-card)' }} />
                    <div>
                      <h3 className="font-mono font-semibold" style={{ color: 'var(--text-on-card)' }}>My Profile</h3>
                      <p className="text-sm opacity-70" style={{ color: 'var(--text-on-card)' }}>Personal information</p>
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

  const renderStudentDashboard = () => {
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

  return (
    <div className="min-h-screen p-6">
      <div className="mb-8">
        <h1 className="text-4xl font-mono font-bold mb-2">
          {getGreeting()}, {userProfile?.firstName || 'User'}!
        </h1>
        <p className="text-lg opacity-70">{currentDate}</p>
        <p className="text-sm opacity-60 font-mono mt-1">
          Role: {userProfile?.role || 'Unknown'}
        </p>
      </div>

      {userProfile?.role === Role.ADMIN && renderAdminDashboard()}
      {userProfile?.role === Role.TEACHER && renderTeacherDashboard()}
      {userProfile?.role === Role.STUDENT && renderStudentDashboard()}
    </div>
  );
};
