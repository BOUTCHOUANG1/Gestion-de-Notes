import { SubjectResponse } from './subject.dto';

export interface TeacherResponse {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  username: string;
  subjects?: SubjectResponse[];
  levels?: string[];
}

export interface TeacherRequest {
  firstName?: string;
  lastName?: string;
  email?: string;
}
