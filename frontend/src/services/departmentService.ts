const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;
import apiClient from "./apiClient";
import type { Department } from "../types/Department";

export const fetchDepartments = async (): Promise<Department[]> => {
    const response = await apiClient.get("/departments");
    return response.data;
};

export async function createDepartment(name: string) {
    const response = await fetch(`${API_BASE_URL}/departments`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ name }),
    });

    if (!response.ok) {
        throw new Error("Failed to create department");
    }

    return response.json();
}

export const updateDepartment = async (id: number, department: Department) => {
    return apiClient.put(`/departments/${id}`, department);
};

export const deleteDepartment = async (id: number) => {
    return apiClient.delete(`/departments/${id}`);
};