import { api } from '../../../store/api/apiSlice';
import { TeacherResponse } from '../../../api/response-dto/teacher.dto';
import { SubjectResponse } from '../../../api/response-dto/subject.dto';
import { studentResDto } from '../../../api/reponse-dto/user.res.dto';

interface GradeResponse {
  id: number;
  studentId: number;
  studentName: string;
  subjectId: number;
  subjectName: string;
  subjectCode: string;
  semesterId: number;
  semesterName: string;
  value: number;
  type: string;
  periodLabel: string;
  comments?: string;
  enteredBy: number;
  enteredByName: string;
  passed: boolean;
  creditsEarned?: number;
  createdDate?: string;
  lastModifiedDate?: string;
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
  }),
});

export const {
  useGetTeacherProfileQuery,
  useGetTeacherGradesQuery,
  useGetStudentsByTeachingLevelsQuery,
  useGetTeacherSubjectsQuery,
} = teacherDashboardApi;
