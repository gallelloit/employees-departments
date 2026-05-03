const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

export async function fetchEmployees() {
    const response = await fetch(`${API_BASE_URL}/employees`);
    if (!response.ok) {
        throw new Error("Failed to fetch employees");
    }
    return response.json();
}

export async function fetchDepartments() {
    const response = await fetch(`${API_BASE_URL}/departments`);

    if (!response.ok) {
        throw new Error("Failed to fetch departments");
    }

    return response.json();
}

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

export async function deleteEmployee(id: number) {
    const response = await fetch(`${API_BASE_URL}/employees/${id}`, {
        method: "DELETE",
    });

    if (!response.ok) {
        throw new Error("Failed to delete employee");
    }

    return response;
}