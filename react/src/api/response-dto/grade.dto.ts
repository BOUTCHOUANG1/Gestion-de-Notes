export interface GradeResponse {
  id: number;
  studentId: number;
  subjectId: number;
  subjectName?: string;
  subjectCode?: string;
  semesterId: number;
  semesterName?: string;
  value: number;
  maxValue: number;
  type: string;
  exam: string;
  periodType?: string;
  comments?: string;
  enteredBy?: number;
  enteredByName?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface GradeRequest {
  studentId: number;
  subjectId: number;
  semesterId: number;
  value: number;
  maxValue?: number;
  type: string;
  exam: string;
  comments?: string;
}
