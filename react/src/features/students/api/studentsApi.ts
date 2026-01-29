import { api } from '../../../store/api/apiSlice';
import { studentResDto } from '../../../api/reponse-dto/user.res.dto';

export const studentsApi = api.injectEndpoints({
  endpoints: (builder) => ({
    getStudents: builder.query<studentResDto[], void>({
      query: () => 'students',
      providesTags: (result) =>
        result
          ? [
              ...result.map(({ id }) => ({ type: 'Students' as const, id })),
              { type: 'Students', id: 'LIST' },
            ]
          : [{ type: 'Students', id: 'LIST' }],
    }),
  }),
});

export const { useGetStudentsQuery } = studentsApi;
