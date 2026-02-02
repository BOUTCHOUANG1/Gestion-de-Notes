export interface TranscriptGrade {
  subjectCode: string;
  subjectName: string;
  credits: number;
  grade: number;
  passed: boolean;
  semester: string;
  academicYear?: string;
}

export interface TranscriptSemester {
  semesterName: string;
  grades: TranscriptGrade[];
  gpa: number;
  totalCredits: number;
  earnedCredits: number;
}

export interface TranscriptResponse {
  studentId: number;
  studentName: string;
  matricule: string;
  level: string;
  cycle: string;
  speciality?: string;
  semesters: TranscriptSemester[];
  cumulativeGPA: number;
  totalCreditsEarned: number;
  totalCreditsRequired: number;
  generatedAt: string;
}
