import { api } from '../../../store/api/apiSlice';

// Types for dashboard statistics
export interface DashboardStats {
  totalStudents: number;
  totalTeachers: number;
  totalSubjects: number;
  totalDepartments: number;
  totalGrades: number;
  totalClaims: number;
  pendingClaims: number;
  activeSemesters: number;
}

export interface StudentsByLevel {
  level: string;
  count: number;
}

export interface GradeDistribution {
  range: string;
  count: number;
}

export interface RecentActivity {
  id: number;
  type: 'grade' | 'claim' | 'user';
  description: string;
  timestamp: string;
  user?: string;
}

export interface DashboardData {
  stats: DashboardStats;
  studentsByLevel: StudentsByLevel[];
  gradeDistribution: GradeDistribution[];
  recentActivity: RecentActivity[];
}

// Extend the API with dashboard endpoints
export const dashboardApi = api.injectEndpoints({
  endpoints: (builder) => ({
    getDashboardStats: builder.query<DashboardStats, void>({
      query: () => '/admin/dashboard/stats',
      providesTags: ['Students', 'Teachers', 'Subjects', 'Departments', 'Grades', 'Revendications'],
    }),
    
    getStudentsByLevel: builder.query<StudentsByLevel[], void>({
      query: () => '/admin/dashboard/students-by-level',
      providesTags: ['Students'],
    }),
    
    getGradeDistribution: builder.query<GradeDistribution[], void>({
      query: () => '/admin/dashboard/grade-distribution',
      providesTags: ['Grades'],
    }),
    
    getRecentActivity: builder.query<RecentActivity[], number | void>({
      query: (limit = 10) => `/admin/dashboard/recent-activity?limit=${limit}`,
      providesTags: ['Grades', 'Revendications', 'Students'],
    }),
    
    // Comprehensive dashboard data endpoint
    getDashboardData: builder.query<DashboardData, void>({
      query: () => '/admin/dashboard',
      providesTags: ['Students', 'Teachers', 'Subjects', 'Departments', 'Grades', 'Revendications'],
    }),
  }),
});

export const {
  useGetDashboardStatsQuery,
  useGetStudentsByLevelQuery,
  useGetGradeDistributionQuery,
  useGetRecentActivityQuery,
  useGetDashboardDataQuery,
} = dashboardApi;
