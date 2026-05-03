import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../services/auth/AuthContext";

const LoginPage = () => {
    const { login } = useAuth();
    const navigate = useNavigate();

    const cognitoDomain = "https://employees-auth-442607.auth.eu-west-3.amazoncognito.com";
    const clientId = "5lubs6v6aqdnlb85pc6o02dudd";
    const redirectUri = "http://localhost:5173/login";

    const loginUrl =
        `${cognitoDomain}/login?` +
        `client_id=${clientId}&` +
        `response_type=code&` +
        `scope=openid+email+profile&` +
        `redirect_uri=${encodeURIComponent(redirectUri)}`;

    useEffect(() => {
        const code = new URLSearchParams(window.location.search).get("code");

        if (!code) return;

        const exchangeCodeForToken = async () => {
            try {
                const response = await fetch(
                    `${cognitoDomain}/oauth2/token`,
                    {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/x-www-form-urlencoded",
                        },
                        body: new URLSearchParams({
                            grant_type: "authorization_code",
                            client_id: clientId,
                            code,
                            redirect_uri: redirectUri,
                        }),
                    }
                );

                const data = await response.json();

                if (data.access_token) {
                    login(data.access_token);
                    navigate("/employees");
                } else {
                    console.error("Token exchange failed", data);
                }
            } catch (error) {
                console.error("Login error", error);
            }
        };

        exchangeCodeForToken();
    }, [login, navigate, cognitoDomain, clientId, redirectUri]);

    return (
        <section>
            <h1>Login</h1>
            <button onClick={() => (window.location.href = loginUrl)}>
                Login with Cognito
            </button>
        </section>
    );
};

export default LoginPage;