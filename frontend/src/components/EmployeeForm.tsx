import {useEffect, useState} from "react";
import {createEmployee, updateEmployee} from "../services/api";
import type { Department } from "../types/Department";
import type {Employee} from "../types/Employee.ts";

interface Props {
    departments: Department[];
    onEmployeeCreated: () => void;
    editingEmployee?: Employee | null;
    clearEditing?: () => void;
}

function EmployeeForm({
                          departments,
                          onEmployeeCreated,
                          editingEmployee,
                          clearEditing,
                      }: Props) {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [departmentId, setDepartmentId] = useState<number>(
        departments[0]?.id || 0
    );

    useEffect(() => {
        if (editingEmployee) {
            setName(editingEmployee.name);
            setEmail(editingEmployee.email);
            setDepartmentId(editingEmployee.department?.id || 0);
        }
    }, [editingEmployee]);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        try {
            if (editingEmployee) {
                await updateEmployee(editingEmployee.id, {
                    name,
                    email,
                    departmentId,
                });

                clearEditing?.();
            } else {
                await createEmployee(name, email, departmentId);
            }

            setName("");
            setEmail("");
            setDepartmentId(departments[0]?.id || 0);

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

            <button type="submit">{editingEmployee ? "Update Employee" : "Add Employee"}</button>
            {editingEmployee && (
                <button
                    type="button"
                    onClick={() => {
                        clearEditing?.();
                        setName("");
                        setEmail("");
                        setDepartmentId(departments[0]?.id || 0);
                    }}
                    style={{ marginLeft: "10px" }}
                >
                    Cancel
                </button>
            )}
        </form>
    );
}

export default EmployeeForm;