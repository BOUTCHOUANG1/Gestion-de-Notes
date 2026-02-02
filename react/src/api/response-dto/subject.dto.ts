export interface SubjectResponse {
  id: number;
  name: string;
  code: string;
  credits: number;
  description?: string;
  active: boolean;
  level: string;
  cycle: string;
  teacherId?: number;
  teacherName?: string;
  departmentId?: number;
  departmentName?: string;
  semesterId?: number;
  creationDate?: string;
  lastModifiedDate?: string;
}

export interface SubjectRequest {
  name: string;
  code: string;
  credits: number;
  description?: string;
  active?: boolean;
  level: string;
  cycle: string;
  teacherId?: number;
  departmentId?: number;
  semesterId?: number;
}
