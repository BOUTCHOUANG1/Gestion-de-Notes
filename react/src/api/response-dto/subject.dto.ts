export interface SubjectLevelInfo {
  teachingLevelId: number;
  studentLevel: string;
}

export interface SubjectTeacher {
  teacherId: number;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
}

export interface SubjectResponse {
  subjectId: number;
  subjectName: string;
  subjectCode: string;
  credits: number;
  description?: string;
  teacher?: SubjectTeacher;
  subjectsLevel: SubjectLevelInfo[];
  studentCycle: string;
  departmentId?: number;
}

export interface SubjectRequest {
  subjectName: string;
  subjectCode: string;
  credits: number;
  description?: string;
  teacherId?: number;
  subjectsLevel: string[];
  Studentcycle: string;
  semesterId?: number;
  departmentId?: number;
}
