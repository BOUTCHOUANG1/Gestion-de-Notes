import { api } from '../../../store/api/apiSlice';
import { TeacherResponse, TeacherRequest } from '../../../api/response-dto/teacher.dto';

export const teachersApi = api.injectEndpoints({
  endpoints: (builder) => ({
    getTeachers: builder.query<TeacherResponse[], void>({
      query: () => 'admin/teachers',
      providesTags: (result) =>
        result
          ? [
              ...result.map(({ id }) => ({ type: 'Teachers' as const, id })),
              { type: 'Teachers', id: 'LIST' },
            ]
          : [{ type: 'Teachers', id: 'LIST' }],
    }),
    
    updateTeacher: builder.mutation<TeacherResponse, { id: number } & TeacherRequest>({
      query: ({ id, ...body }) => ({
        url: `admin/teacher/${id}`,
        method: 'PUT',
        body,
      }),
      invalidatesTags: (_result, _error, { id }) => [
        { type: 'Teachers', id },
        { type: 'Teachers', id: 'LIST' },
      ],
    }),
  }),
});

export const {
  useGetTeachersQuery,
  useUpdateTeacherMutation,
} = teachersApi;
