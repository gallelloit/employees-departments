import { useState } from "react";
import { createEmployee } from "../services/api";
import type { Department } from "../types/Department";

interface Props {
    departments: Department[];
    onEmployeeCreated: () => void;
}

function EmployeeForm({ departments, onEmployeeCreated }: Props) {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [departmentId, setDepartmentId] = useState<number>(
        departments[0]?.id || 0
    );

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        try {
            await createEmployee(name, email, departmentId);
            setName("");
            setEmail("");
            onEmployeeCreated();
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <input
                type="text"
                placeholder="Employee name"
                value={name}
                onChange={(e) => setName(e.target.value)}
                required
            />

            <input
                type="email"
                placeholder="Employee email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
            />

            <select
                value={departmentId}
                onChange={(e) => setDepartmentId(Number(e.target.value))}
            >
                {departments.map((department) => (
                    <option key={department.id} value={department.id}>
                        {department.name}
                    </option>
                ))}
            </select>

            <button type="submit">Add Employee</button>
        </form>
    );
}

export default EmployeeForm;