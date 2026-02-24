export interface DepartmentResponse {
  departmentId: number;
  departmentName: string;
  createdDate?: string;
  lastModifiedDate?: string;
}

export interface DepartmentRequest {
  departmentName: string;
}
