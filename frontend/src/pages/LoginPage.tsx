import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../services/auth/AuthContext";

const LoginPage = () => {
    const { login } = useAuth();
    const navigate = useNavigate();

    const cognitoDomain = import.meta.env.VITE_COGNITO_DOMAIN;
    const clientId = import.meta.env.VITE_COGNITO_CLIENT_ID;
    const redirectUri = import.meta.env.VITE_COGNITO_REDIRECT_URI;

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
                    login(data.id_token);
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