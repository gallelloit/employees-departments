import { useEffect, useState } from "react";
import { fetchDepartments } from "../services/departmentService";
import type { Department } from "../types/Department";
import DepartmentForm from "../components/DepartmentForm";


function DepartmentsPage() {
    const [departments, setDepartments] = useState<Department[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const loadDepartments = () => {
        fetchDepartments()
            .then(setDepartments)
            .catch(() => setError("Failed to load departments."))
            .finally(() => setLoading(false));
    };

    useEffect(() => {
        fetchDepartments()
            .then(setDepartments)
            .catch(() => setError("Failed to load departments."))
            .finally(() => setLoading(false));
    }, []);

    useEffect(() => {
        loadDepartments();
    }, []);

    if (loading) {
        return <p>Loading departments...</p>;
    }

    if (error) {
        return <p>{error}</p>;
    }

    return (
        <div>
            <h1>Departments Dashboard</h1>

            <DepartmentForm onDepartmentCreated={loadDepartments} />

            <ul>
                {departments.map((department) => (
                    <li key={department.id}>{department.name}</li>
                ))}
            </ul>
        </div>
    );
}

export default DepartmentsPage;