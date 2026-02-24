import { api } from '../../../store/api/apiSlice';
import { RevendicationResponse, RevendicationRequest } from '../../../api/response-dto/revendication.dto';

export const revendicationApi = api.injectEndpoints({
  endpoints: (builder) => ({
    createRevendication: builder.mutation<RevendicationResponse, { request: RevendicationRequest; proof?: File }>({
      query: ({ request, proof }) => {
        const formData = new FormData();
        formData.append('request', new Blob([JSON.stringify(request)], { type: 'application/json' }));
        if (proof) formData.append('proof', proof);
        return {
          url: 'student/revendication',
          method: 'POST',
          body: formData,
        };
      },
      invalidatesTags: [{ type: 'Revendications', id: 'LIST' }, { type: 'Revendications', id: 'STUDENT_LIST' }],
    }),

    getStudentRevendications: builder.query<RevendicationResponse[], void>({
      query: () => 'student/revendications',
      providesTags: [{ type: 'Revendications', id: 'STUDENT_LIST' }],
    }),
    
    getTeacherRevendications: builder.query<RevendicationResponse[], void>({
      query: () => 'teacher/revendications',
      providesTags: (result) =>
        result
          ? [
              ...result.map(({ revendicationId }) => ({ type: 'Revendications' as const, id: revendicationId })),
              { type: 'Revendications', id: 'LIST' },
            ]
          : [{ type: 'Revendications', id: 'LIST' }],
    }),

    getAdminRevendications: builder.query<RevendicationResponse[], void>({
      query: () => 'admin/revendications',
      providesTags: (result) =>
        result
          ? [
              ...result.map(({ revendicationId }) => ({ type: 'Revendications' as const, id: revendicationId })),
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
      invalidatesTags: (_result, _error, { id }) => [
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
      invalidatesTags: (_result, _error, { id }) => [
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
  useGetAdminRevendicationsQuery,
  useApproveRevendicationMutation,
  useRejectRevendicationMutation,
} = revendicationApi;
