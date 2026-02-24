import { api } from '../../../store/api/apiSlice';
import { studentResDto } from '../../../api/reponse-dto/user.res.dto';

export interface CreateStudentRequest {
  firstName: string;
  lastName: string;
  email: string;
  username: string;
  password: string;
  level: string;
  phone?: string;
}

export interface UpdateStudentRequest {
  firstName?: string;
  lastName?: string;
  email?: string;
  level?: string;
}

export const studentsApi = api.injectEndpoints({
  endpoints: (builder) => ({
    getStudents: builder.query<studentResDto[], void>({
      query: () => 'admin/students',
      providesTags: (result) =>
        result
          ? [
              ...result.map(({ id }) => ({ type: 'Students' as const, id })),
              { type: 'Students', id: 'LIST' },
            ]
          : [{ type: 'Students', id: 'LIST' }],
    }),
    
    createStudent: builder.mutation<studentResDto, CreateStudentRequest>({
      query: (body) => ({
        url: 'admin/students',
        method: 'POST',
        body,
      }),
      invalidatesTags: [{ type: 'Students', id: 'LIST' }],
    }),
    
    updateStudent: builder.mutation<studentResDto, { id: number } & UpdateStudentRequest>({
      query: ({ id, ...body }) => ({
        url: `admin/student/${id}`,
        method: 'PUT',
        body,
      }),
      invalidatesTags: (_result, _error, { id }) => [
        { type: 'Students', id },
        { type: 'Students', id: 'LIST' },
      ],
    }),
    
    deleteStudent: builder.mutation<void, number>({
      query: (id) => ({
        url: `users/${id}`,
        method: 'DELETE',
      }),
      invalidatesTags: (_result, _error, id) => [
        { type: 'Students', id },
        { type: 'Students', id: 'LIST' },
      ],
    }),
  }),
});

export const { 
  useGetStudentsQuery,
  useCreateStudentMutation,
  useUpdateStudentMutation,
  useDeleteStudentMutation,
} = studentsApi;
