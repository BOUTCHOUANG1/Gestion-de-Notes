import { fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import type { BaseQueryFn, FetchArgs, FetchBaseQueryError } from '@reduxjs/toolkit/query';
import { getToken } from '../../api/services/token.service';
import { triggerServerNotification, triggerClientNotification } from '../../contexts/notification/slice';

const baseQuery = fetchBaseQuery({
  baseUrl: 'http://localhost:3030/api',
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
      const data = result.error.data as any;
      const message =
        data?.detail ??
        data?.message ??
        data?.error ??
        data?.title ??
        'An error occurred';
      api.dispatch(triggerServerNotification({
        message: 'Error',
        type: 'error',
        description: message,
      }));
    }
  }
  
  return result;
};
