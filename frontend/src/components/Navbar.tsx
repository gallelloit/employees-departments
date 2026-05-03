import { Link } from "react-router-dom";
import { useAuth } from "../services/auth/AuthContext";

const Navbar = () => {
    const { isAuthenticated, logout } = useAuth();

    const cognitoDomain = "https://employees-auth-442607.auth.eu-west-3.amazoncognito.com";
    const clientId = "5lubs6v6aqdnlb85pc6o02dudd";
    const logoutRedirectUri = "http://localhost:5173/login";

    const handleLogout = () => {
        logout();

        window.location.href =
            `${cognitoDomain}/logout?` +
            `client_id=${clientId}&` +
            `logout_uri=${encodeURIComponent(logoutRedirectUri)}`;
    };

    return (
        <nav>
            <Link to="/employees">Employees</Link> |{" "}
            <Link to="/departments">Departments</Link>

            {isAuthenticated && (
                <button
                    onClick={handleLogout}
                    style={{ marginLeft: "20px" }}
                >
                    Logout
                </button>
            )}
        </nav>
    );
};

export default Navbar;