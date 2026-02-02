import { createApi } from '@reduxjs/toolkit/query/react';
import { baseQueryWithAuth } from './baseQuery';

export const api = createApi({
  reducerPath: 'api',
  baseQuery: baseQueryWithAuth,
  tagTypes: [
    'Auth',
    'User', 
    'Students',
    'Teachers',
    'Grades',
    'Departments',
    'Subjects',
    'Semesters',
    'Revendications',
    'Transcript',
  ],
  endpoints: () => ({}),
});
