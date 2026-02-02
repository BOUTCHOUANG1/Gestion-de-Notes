import { api } from '../../../store/api/apiSlice';
import { SubjectResponse, SubjectRequest } from '../../../api/response-dto/subject.dto';

export const subjectApi = api.injectEndpoints({
  endpoints: (builder) => ({
    getSubjects: builder.query<SubjectResponse[], void>({
      query: () => 'admin/subjects',
      providesTags: (result) =>
        result
          ? [
              ...result.map(({ id }) => ({ type: 'Subjects' as const, id })),
              { type: 'Subjects', id: 'LIST' },
            ]
          : [{ type: 'Subjects', id: 'LIST' }],
    }),
    
    getSubjectsByTeacher: builder.query<SubjectResponse[], void>({
      query: () => 'teacher/subject',
      providesTags: ['Subjects'],
    }),
    
    createSubject: builder.mutation<SubjectResponse, SubjectRequest>({
      query: (body) => ({
        url: 'admin/subject',
        method: 'POST',
        body,
      }),
      invalidatesTags: [{ type: 'Subjects', id: 'LIST' }],
    }),
    
    updateSubject: builder.mutation<SubjectResponse, { id: number } & SubjectRequest>({
      query: ({ id, ...body }) => ({
        url: `admin/subject/${id}`,
        method: 'PUT',
        body,
      }),
      invalidatesTags: (_result, _error, { id }) => [
        { type: 'Subjects', id },
        { type: 'Subjects', id: 'LIST' },
      ],
    }),
    
    deleteSubject: builder.mutation<SubjectResponse, number>({
      query: (id) => ({
        url: `admin/subject/${id}`,
        method: 'DELETE',
      }),
      invalidatesTags: (_result, _error, id) => [
        { type: 'Subjects', id },
        { type: 'Subjects', id: 'LIST' },
      ],
    }),
  }),
});

export const {
  useGetSubjectsQuery,
  useGetSubjectsByTeacherQuery,
  useCreateSubjectMutation,
  useUpdateSubjectMutation,
  useDeleteSubjectMutation,
} = subjectApi;
