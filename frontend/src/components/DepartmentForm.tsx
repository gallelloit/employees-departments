import { useState } from "react";

interface Props {
    onDepartmentCreated: () => void;
}

function DepartmentForm({ onDepartmentCreated }: Props) {
    const [name, setName] = useState("");

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        try {
            const { createDepartment } = await import("../services/api");
            await createDepartment(name);
            setName("");
            onDepartmentCreated();
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <input
                type="text"
                placeholder="Department name"
                value={name}
                onChange={(e) => setName(e.target.value)}
                required
            />
            <button type="submit">Add Department</button>
        </form>
    );
}

export default DepartmentForm;