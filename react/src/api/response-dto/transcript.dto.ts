// Matches the actual backend TranscriptResponse + GradeResponse

export interface TranscriptGradeSubject {
  id: number;
  subjectName: string;
  subjectCode: string;
  credits: number;
}

export interface TranscriptGradeSemester {
  id: number;
  name: string;
  active: boolean;
}

export interface TranscriptGrade {
  gradeId: number;
  ccScore: number | null;
  tpScore: number | null;
  snScore: number | null;
  totalScore: number | null;
  hasPassed: boolean;
  gpa: number | null;
  comments: string | null;
  subject: TranscriptGradeSubject;
  semester: TranscriptGradeSemester;
  exam: string;
  createdDate: string | null;
}

export interface TranscriptResponse {
  transcriptId: number | null;
  studentFirstName: string;
  studentLastName: string;
  studentMatricule: string;
  studentLevel: { teachingLevelId: number; studentLevel: string };
  studentCycle: string;
  status: string;
  studentGrades: TranscriptGrade[];
  annualAverage: number | null;
  creditsEarned: number | null;
  totalCreditsRequired: number | null;
  semester1Credits: number | null;
  semester2Credits: number | null;
  semester1Average: number | null;
  semester2Average: number | null;
  semesterName: string | null;
  facultyName: string | null;
  academicYear: string | null;
}
