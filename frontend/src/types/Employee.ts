import type {Department} from "./Department.ts";

export interface Employee {
    id: number;
    name: string;
    email: string;
    department: Department;
}