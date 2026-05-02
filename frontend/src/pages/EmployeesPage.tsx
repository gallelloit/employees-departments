import { useEffect, useState } from "react";
import { fetchEmployees } from "../services/api";
import type { Employee } from "../types/Employee";
import { fetchDepartments } from "../services/api";
import type { Department } from "../types/Department";
import EmployeeForm from "../components/EmployeeForm";


function App() {
    const [employees, setEmployees] = useState<Employee[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [departments, setDepartments] = useState<Department[]>([]);

    const loadEmployees = () => {
        fetchEmployees()
            .then(setEmployees)
            .catch(() => setError("Failed to load employees."))
            .finally(() => setLoading(false));
    };

    useEffect(() => {
        fetchEmployees()
            .then(setEmployees)
            .catch(() => setError("Failed to load employees."))
            .finally(() => setLoading(false));
    }, []);

    useEffect(() => {
        loadEmployees();
        fetchDepartments().then(setDepartments).catch(console.error);
    }, []);

    if (loading) {
        return <p>Loading employees...</p>;
    }

    if (error) {
        return <p>{error}</p>;
    }

    return (
        <section>
            <h1>Employees Dashboard</h1>

            <EmployeeForm
                departments={departments}
                onEmployeeCreated={loadEmployees}
            />

            <ul>
                {employees.map((employee) => (
                    <li key={employee.id}>
                        {employee.name} - {employee.email}
                    </li>
                ))}
            </ul>
        </section>
    );
}

export default App;