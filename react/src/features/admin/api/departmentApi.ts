import { api } from '../../../store/api/apiSlice';
import { DepartmentResponse, DepartmentRequest } from '../../../api/response-dto/department.dto';

export const departmentApi = api.injectEndpoints({
  endpoints: (builder) => ({
    getDepartments: builder.query<DepartmentResponse[], void>({
      query: () => 'admin/department',
      providesTags: (result) =>
        result
          ? [
              ...result.map(({ id }) => ({ type: 'Departments' as const, id })),
              { type: 'Departments', id: 'LIST' },
            ]
          : [{ type: 'Departments', id: 'LIST' }],
    }),
    
    createDepartment: builder.mutation<DepartmentResponse, DepartmentRequest>({
      query: (body) => ({
        url: 'admin/department',
        method: 'POST',
        body,
      }),
      invalidatesTags: [{ type: 'Departments', id: 'LIST' }],
    }),
    
    updateDepartment: builder.mutation<DepartmentResponse, { id: number } & DepartmentRequest>({
      query: ({ id, ...body }) => ({
        url: `admin/department/${id}`,
        method: 'PUT',
        body,
      }),
      invalidatesTags: (result, error, { id }) => [
        { type: 'Departments', id },
        { type: 'Departments', id: 'LIST' },
      ],
    }),
    
    deleteDepartment: builder.mutation<DepartmentResponse, number>({
      query: (id) => ({
        url: `admin/department/${id}`,
        method: 'DELETE',
      }),
      invalidatesTags: (result, error, id) => [
        { type: 'Departments', id },
        { type: 'Departments', id: 'LIST' },
      ],
    }),
  }),
});

export const {
  useGetDepartmentsQuery,
  useCreateDepartmentMutation,
  useUpdateDepartmentMutation,
  useDeleteDepartmentMutation,
} = departmentApi;
