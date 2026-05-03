import apiClient from "./apiClient";
import type { Employee } from "../types/Employee";

export const fetchEmployees = async (): Promise<Employee[]> => {
    const response = await apiClient.get("/employees");
    return response.data;
};

export const createEmployee = async (employee: Employee): Promise<Employee> => {
    const response = await apiClient.post("/employees", employee);
    return response.data;
};

export const updateEmployee = async (
    id: number,
    employee: Employee
): Promise<Employee> => {
    const response = await apiClient.put(`/employees/${id}`, employee);
    return response.data;
};

export const deleteEmployee = async (id: number): Promise<void> => {
    await apiClient.delete(`/employees/${id}`);
};