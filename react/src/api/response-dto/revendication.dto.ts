export interface RevendicationStudentInfo {
  id: number;
  firstName: string;
  lastName: string;
  matricule?: string;
}

export interface RevendicationGradeInfo {
  gradeId: number;
  score: number;
  subject?: {
    subjectId: number;
    subjectName: string;
    subjectCode: string;
  };
}

export interface RevendicationSemesterInfo {
  semesterId: number;
  semesterName: string;
}

export interface RevendicationResponse {
  revendicationId: number;
  student: RevendicationStudentInfo;
  grade: RevendicationGradeInfo;
  semester?: RevendicationSemesterInfo;
  requestedScore: number;
  description?: string;
  teacherComment?: string;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  createdDate?: string;
  lastModifiedDate?: string;
}

export interface RevendicationRequest {
  gradeId: number;
  examPeriodId: number;
  requestedScore: number;
  description: string;
}
