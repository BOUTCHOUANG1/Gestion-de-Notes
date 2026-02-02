export interface RevendicationResponse {
  id: number;
  gradeId: number;
  studentId: number;
  studentName?: string;
  subjectName?: string;
  subjectCode?: string;
  currentScore: number;
  requestedScore: number;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  cause: string;
  description?: string;
  periodLabel?: string;
  teacherComment?: string;
  rejectionReason?: string;
  semesterId?: number;
  createdAt?: string;
  resolvedAt?: string;
}

export interface RevendicationRequest {
  gradeId: number;
  requestedScore: number;
  cause: string;
  period: string;
  description: string;
}
