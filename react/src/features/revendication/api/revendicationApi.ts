import { api } from '../../../store/api/apiSlice';
import { RevendicationResponse, RevendicationRequest } from '../../../api/response-dto/revendication.dto';

export const revendicationApi = api.injectEndpoints({
  endpoints: (builder) => ({
    createRevendication: builder.mutation<RevendicationResponse, RevendicationRequest>({
      query: (body) => ({
        url: 'student/revendication',
        method: 'POST',
        body,
      }),
      invalidatesTags: [{ type: 'Revendications', id: 'LIST' }],
    }),
    
    getStudentRevendications: builder.query<RevendicationResponse[], void>({
      query: () => 'student/revendications',
      providesTags: (result) =>
        result
          ? [
              ...result.map(({ id }) => ({ type: 'Revendications' as const, id })),
              { type: 'Revendications', id: 'STUDENT_LIST' },
            ]
          : [{ type: 'Revendications', id: 'STUDENT_LIST' }],
    }),
    
    getTeacherRevendications: builder.query<RevendicationResponse[], void>({
      query: () => 'teacher/revendications',
      providesTags: (result) =>
        result
          ? [
              ...result.map(({ id }) => ({ type: 'Revendications' as const, id })),
              { type: 'Revendications', id: 'LIST' },
            ]
          : [{ type: 'Revendications', id: 'LIST' }],
    }),
    
    approveRevendication: builder.mutation<void, { id: number; comment?: string }>({
      query: ({ id, comment }) => ({
        url: `teacher/revendication/${id}/approve`,
        method: 'POST',
        params: comment ? { comment } : undefined,
      }),
      invalidatesTags: (result, error, { id }) => [
        { type: 'Revendications', id },
        { type: 'Revendications', id: 'LIST' },
        { type: 'Grades', id: 'LIST' },
      ],
    }),
    
    rejectRevendication: builder.mutation<void, { id: number; reason?: string }>({
      query: ({ id, reason }) => ({
        url: `teacher/revendication/${id}/reject`,
        method: 'POST',
        params: reason ? { reason } : undefined,
      }),
      invalidatesTags: (result, error, { id }) => [
        { type: 'Revendications', id },
        { type: 'Revendications', id: 'LIST' },
      ],
    }),
  }),
});

export const {
  useCreateRevendicationMutation,
  useGetStudentRevendicationsQuery,
  useGetTeacherRevendicationsQuery,
  useApproveRevendicationMutation,
  useRejectRevendicationMutation,
} = revendicationApi;
