import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { studentResDto, UserProfileResDto } from '../../api/reponse-dto/user.res.dto';

interface UserSliceState {
  profile: UserProfileResDto;
  students: studentResDto[];
}

const initialState: UserSliceState = {
  profile: {} as UserProfileResDto,
  students: [],
};

const slice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    loadUserProfile(state, action: PayloadAction<UserProfileResDto>) {
      state.profile = action.payload;
    },
    loadStudents(state, action: PayloadAction<studentResDto[]>) {
      state.students = action.payload;
    },
  },
});

export const userReducer = slice.reducer;
export const {
  loadUserProfile,
  loadStudents
} = slice.actions;
