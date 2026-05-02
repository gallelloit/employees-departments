import { useEffect, useState } from "react";
import { fetchEmployees } from "./services/api";

function App() {
    const [employees, setEmployees] = useState<any[]>([]);

    useEffect(() => {
        fetchEmployees()
            .then(setEmployees)
            .catch(console.error);
    }, []);

    return (
        <div>
            <h1>Employees Dashboard</h1>
            <ul>
                {employees.map((employee) => (
                    <li key={employee.id}>
                        {employee.name} - {employee.email}
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default App;