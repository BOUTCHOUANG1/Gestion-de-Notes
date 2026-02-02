import { api } from '../../../store/api/apiSlice';
import { SemesterResponse, SemesterRequest } from '../../../api/response-dto/semester.dto';

export const semesterApi = api.injectEndpoints({
  endpoints: (builder) => ({
    getSemesters: builder.query<SemesterResponse[], void>({
      query: () => 'semesters',
      providesTags: (result) =>
        result
          ? [
              ...result.map(({ id }) => ({ type: 'Semesters' as const, id })),
              { type: 'Semesters', id: 'LIST' },
            ]
          : [{ type: 'Semesters', id: 'LIST' }],
    }),
    
    createSemester: builder.mutation<SemesterResponse, SemesterRequest>({
      query: (body) => ({
        url: 'admin/semester',
        method: 'POST',
        body,
      }),
      invalidatesTags: [{ type: 'Semesters', id: 'LIST' }],
    }),
    
    updateSemester: builder.mutation<SemesterResponse, { id: number } & SemesterRequest>({
      query: ({ id, ...body }) => ({
        url: `admin/semester/${id}`,
        method: 'PUT',
        body,
      }),
      invalidatesTags: (_result, _error, { id }) => [
        { type: 'Semesters', id },
        { type: 'Semesters', id: 'LIST' },
      ],
    }),
    
    deleteSemester: builder.mutation<void, number>({
      query: (id) => ({
        url: `admin/semester/${id}`,
        method: 'DELETE',
      }),
      invalidatesTags: (_result, _error, id) => [
        { type: 'Semesters', id },
        { type: 'Semesters', id: 'LIST' },
      ],
    }),
  }),
});

export const {
  useGetSemestersQuery,
  useCreateSemesterMutation,
  useUpdateSemesterMutation,
  useDeleteSemesterMutation,
} = semesterApi;
