import apiClient from "./apiClient";
import type { Department } from "../types/Department";

export const fetchDepartments = async (): Promise<Department[]> => {
    const response = await apiClient.get("/departments");
    return response.data;
};

export const createDepartment = async (
    department: Department
): Promise<Department> => {
    const response = await apiClient.post("/departments", department);
    return response.data;
};