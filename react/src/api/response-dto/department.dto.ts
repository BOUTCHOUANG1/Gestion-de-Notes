export interface DepartmentResponse {
  id: number;
  name: string;
  creationDate?: string;
  lastModifiedDate?: string;
}

export interface DepartmentRequest {
  name: string;
}
