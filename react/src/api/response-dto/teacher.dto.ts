import { SubjectResponse } from './subject.dto';

export interface TeacherResponse {
  teacherId: number;
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  username: string;
  phoneNumber?: string;
  subjects?: SubjectResponse[];
  teachingLevel?: any[];
  role?: string;
  isActive?: boolean;
}

export interface TeacherRequest {
  firstName?: string;
  lastName?: string;
  email?: string;
}
