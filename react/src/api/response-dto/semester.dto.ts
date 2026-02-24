export interface SemesterResponse {
  id: number;
  semesterId: number;
  name: string;
  startDate: string;
  endDate: string;
  active: boolean;
  orderIndex?: number;
  creationDate?: string;
  lastModifiedDate?: string;
}

export interface SemesterRequest {
  name: string;
  startDate: string;
  endDate: string;
  active?: boolean;
  orderIndex?: number;
}
