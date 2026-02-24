import { api } from '../../../store/api/apiSlice';
import { TranscriptResponse } from '../../../api/response-dto/transcript.dto';

export const transcriptApi = api.injectEndpoints({
  endpoints: (builder) => ({
    getStudentTranscript: builder.query<TranscriptResponse, void>({
      query: () => 'student/transcript',
      providesTags: ['Transcript'],
    }),
  }),
});

export const { useGetStudentTranscriptQuery } = transcriptApi;
