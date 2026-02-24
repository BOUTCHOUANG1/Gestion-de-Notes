import { api } from '../../../store/api/apiSlice';
import { TeacherResponse } from '../../../api/response-dto/teacher.dto';
import { SubjectResponse } from '../../../api/response-dto/subject.dto';
import { studentResDto } from '../../../api/reponse-dto/user.res.dto';

export interface GradeResponse {
  gradeId: number;
  ccScore: number | null;
  tpScore: number | null;
  snScore: number | null;
  totalScore: number | null;
  maxValue: number | null;
  comments: string | null;
  student: { id: number; username: string; firstName: string; lastName: string; email: string; matricule: string };
  subject: { id: number; subjectName: string; subjectCode: string; credits: number };
  examiner: { id: number; username: string; firstName: string; lastName: string; email: string };
  semester: { id: number; name: string; active: boolean };
  exam: string;
  hasPassed: boolean;
  gpa: number | null;
  createdDate: string | null;
  lastModifiedDate: string | null;
}

export interface GradeRequest {
  studentId: number;
  subjectId: number;
  examId: number;
  semesterId: number;
  ccScore?: number;
  tpScore?: number;
  snScore?: number;
  comments?: string;
  assessmentType: string;
}

interface StudentsByLevelResponse {
  [level: string]: studentResDto[];
}

export const teacherDashboardApi = api.injectEndpoints({
  endpoints: (builder) => ({
    getTeacherProfile: builder.query<TeacherResponse, void>({
      query: () => 'teacher/profile',
      providesTags: ['User'],
    }),
    
    getTeacherGrades: builder.query<GradeResponse[], void>({
      query: () => 'teacher/my-grades',
      providesTags: ['Grades'],
    }),
    
    getStudentsByTeachingLevels: builder.query<StudentsByLevelResponse, void>({
      query: () => 'teacher/my-students',
      providesTags: ['Students'],
    }),
    
    getTeacherSubjects: builder.query<SubjectResponse[], void>({
      query: () => 'teacher/subject',
      providesTags: ['Subjects'],
    }),

    createGrade: builder.mutation<GradeResponse, GradeRequest>({
      query: (body) => ({ url: 'teacher/grade', method: 'POST', body }),
      invalidatesTags: ['Grades'],
    }),

    updateGrade: builder.mutation<GradeResponse, { gradeId: number; body: GradeRequest }>({
      query: ({ gradeId, body }) => ({ url: `teacher/grade/${gradeId}`, method: 'PUT', body }),
      invalidatesTags: ['Grades'],
    }),

    deleteGrade: builder.mutation<void, number>({
      query: (gradeId) => ({ url: `teacher/grade/${gradeId}`, method: 'DELETE' }),
      invalidatesTags: ['Grades'],
    }),
  }),
});

export const {
  useGetTeacherProfileQuery,
  useGetTeacherGradesQuery,
  useGetStudentsByTeachingLevelsQuery,
  useGetTeacherSubjectsQuery,
  useCreateGradeMutation,
  useUpdateGradeMutation,
  useDeleteGradeMutation,
} = teacherDashboardApi;
