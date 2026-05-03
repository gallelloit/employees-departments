import { Link } from "react-router-dom";

function Navbar() {
    return (
        <nav>
            <ul style={{ display: "flex", gap: "2rem", listStyle: "none", margin: 0 }}>
                <li>
                    <Link to="/employees">Employees</Link>
                </li>
                <li>
                    <Link to="/departments">Departments</Link>
                </li>
            </ul>
        </nav>
    );
}

export default Navbar;