const API_BASE_URL = "http://InfraS-LB8A1-u1MB8H1N0OlS-1182114939.eu-west-3.elb.amazonaws.com";

export async function fetchEmployees() {
    const response = await fetch(`${API_BASE_URL}/employees`);
    if (!response.ok) {
        throw new Error("Failed to fetch employees");
    }
    return response.json();
}