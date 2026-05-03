const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;
import apiClient from "./apiClient";
import type { Employee } from "../types/Employee";


export const fetchEmployees = async (): Promise<Employee[]> => {
    const response = await apiClient.get("/employees");
    return response.data;
};

export async function createEmployee(
    name: string,
    email: string,
    departmentId: number
) {
    const response = await fetch(`${API_BASE_URL}/employees`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            name,
            email,
            departmentId,
        }),
    });

    if (!response.ok) {
        throw new Error("Failed to create employee");
    }

    return response;
}

export async function updateEmployee(
    id: number,
    employeeData: {
        name: string;
        email: string;
        departmentId: number;
    }
) {
    const response = await fetch(`${API_BASE_URL}/employees/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(employeeData),
    });

    if (!response.ok) {
        throw new Error("Failed to update employee");
    }

    return response;
}

export const deleteEmployee = async (id: number) => {
    return apiClient.delete(`/employees/${id}`);
};