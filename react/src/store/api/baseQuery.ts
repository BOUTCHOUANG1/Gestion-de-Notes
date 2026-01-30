import { fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import type { BaseQueryFn, FetchArgs, FetchBaseQueryError } from '@reduxjs/toolkit/query';
import { getToken } from '../../api/services/token.service';
import { triggerServerNotification, triggerClientNotification } from '../../contexts/notification/slice';

const baseQuery = fetchBaseQuery({
  baseUrl: import.meta.env.VITE_API_BASE_URL || 'http://localhost:3030/api',
  prepareHeaders: (headers) => {
    const token = getToken('token');
    if (token) {
      headers.set('Authorization', `Bearer ${token}`);
    }
    return headers;
  },
});

export const baseQueryWithAuth: BaseQueryFn<
  string | FetchArgs,
  unknown,
  FetchBaseQueryError
> = async (args, api, extraOptions) => {
  const result = await baseQuery(args, api, extraOptions);
  
  if (result.error) {
    if (result.error.status === 'FETCH_ERROR') {
      api.dispatch(triggerClientNotification({
        message: 'Network Error',
        type: 'error',
        description: 'Unable to connect to the server',
      }));
    } else if (
      typeof result.error.status === 'number' &&
      result.error.status >= 400 &&
      result.error.status <= 499
    ) {
      const data = result.error.data as Record<string, unknown>;
      const message = typeof data?.detail === 'string' ? data.detail :
        typeof data?.message === 'string' ? data.message :
        typeof data?.error === 'string' ? data.error :
        typeof data?.title === 'string' ? data.title :
        'An error occurred';
      api.dispatch(triggerServerNotification({
        message: 'Error',
        type: 'error',
        description: message,
      }));
    } else if (
      typeof result.error.status === 'number' &&
      result.error.status >= 500
    ) {
      api.dispatch(triggerServerNotification({
        message: 'Server Error',
        type: 'error',
        description: 'An internal server error occurred. Please try again later.',
      }));
    }
  }
  
  return result;
};
