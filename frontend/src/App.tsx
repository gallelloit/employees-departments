import { Routes, Route, Navigate } from "react-router-dom";
import Navbar from "./components/Navbar";
import EmployeesPage from "./pages/EmployeesPage";
import DepartmentsPage from "./pages/DepartmentsPage";
import LoginPage from "./pages/LoginPage";
import ProtectedRoute from "./services/auth/ProtectedRoute";

function App() {
    return (
        <>
            <Navbar />
            <main>
                <Routes>
                    <Route path="/login" element={<LoginPage />} />

                    <Route
                        path="/"
                        element={<Navigate to="/employees" replace />}
                    />

                    <Route
                        path="/employees"
                        element={
                            <ProtectedRoute>
                                <EmployeesPage />
                            </ProtectedRoute>
                        }
                    />

                    <Route
                        path="/departments"
                        element={
                            <ProtectedRoute>
                                <DepartmentsPage />
                            </ProtectedRoute>
                        }
                    />
                </Routes>
            </main>
        </>
    );
}

export default App;