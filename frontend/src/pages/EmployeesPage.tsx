import { useEffect, useState } from "react";
import { fetchEmployees, deleteEmployee } from "../services/api";
import type { Employee } from "../types/Employee";
import { fetchDepartments } from "../services/api";
import type { Department } from "../types/Department";
import EmployeeForm from "../components/EmployeeForm";


function App() {
    const [employees, setEmployees] = useState<Employee[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [departments, setDepartments] = useState<Department[]>([]);
    const [editingEmployee, setEditingEmployee] = useState<Employee | null>(null);

    const loadEmployees = () => {
        fetchEmployees()
            .then(setEmployees)
            .catch(() => setError("Failed to load employees."))
            .finally(() => setLoading(false));
    };

    const handleEdit = (employee: Employee) => {
        setEditingEmployee(employee);
    };

    const handleDelete = async (id: number) => {
        try {
            await deleteEmployee(id);
            loadEmployees();
        } catch (error) {
            console.error("Failed to delete employee:", error);
        }
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
                editingEmployee={editingEmployee}
                clearEditing={() => setEditingEmployee(null)}
            />

            <ul>
                {employees.map((employee) => (
                    <li key={employee.id}>
                        {employee.name} - {employee.email}
                        <button
                            onClick={() => handleEdit(employee)}
                            style={{ marginLeft: "10px" }}
                        >
                            Edit
                        </button>
                        <button
                            onClick={() => handleDelete(employee.id)}
                            style={{ marginLeft: "10px" }}
                        >
                            Delete
                        </button>
                    </li>
                ))}
            </ul>
        </section>
    );
}

export default App;