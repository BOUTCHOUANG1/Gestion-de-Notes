import { api } from '../../../store/api/apiSlice';
import { LoginreqDto, RegisterReqDto } from '../../../api/request-dto/auth.req';
import { LoginResDto } from '../../../api/reponse-dto/auth.res.dto';
import { UserProfileResDto } from '../../../api/reponse-dto/user.res.dto';
import { setTokens } from '../../../api/services/token.service';
import { markAsAuthenticated } from '../slice';

export const authApi = api.injectEndpoints({
  endpoints: (builder) => ({
    login: builder.mutation<LoginResDto, LoginreqDto>({
      query: (credentials) => ({
        url: 'auth/login',
        method: 'POST',
        body: credentials,
      }),
      invalidatesTags: ['Auth', 'User'],
      async onQueryStarted(_arg, { dispatch, queryFulfilled }) {
        try {
          const { data } = await queryFulfilled;
          setTokens({ token: data.token });
          dispatch(markAsAuthenticated());
        } catch {
          // Error already handled by baseQuery
        }
      },
    }),
    
    register: builder.mutation<unknown, RegisterReqDto>({
      query: (body) => ({
        url: 'auth/admin/register',
        method: 'POST',
        body,
      }),
      invalidatesTags: ['Auth'],
    }),
    
    getProfile: builder.query<UserProfileResDto, void>({
      query: () => 'me',
      providesTags: [{ type: 'User', id: 'PROFILE' }],
    }),
  }),
});

export const { useLoginMutation, useRegisterMutation, useGetProfileQuery } = authApi;
