import {Role} from "../enums";

export interface LoginreqDto {
    username: string;
    password: string;
}

export interface RegisterReqDto {
    email: string;
    username: string;
    password: string;
    firstName: string;
    lastName: string;
    phone?: string;
    role: Role;
    // Student fields
    matricule?: string;
    levelId?: number;
    speciality?: string;
    cycle?: string;
    dateOfBirth?: string;
    placeOfBirth?: string;
    // Teacher fields
    levelIds?: number[];
    departmentId?: number;
    subjectIds?: number[];
}
