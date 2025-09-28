import { useLogin } from "@/hooks/use-login";
import { useAuthStore } from "@/store/auth-store";
import { useEffect } from "react";
import { Navigate } from "react-router-dom";

const Login = () => {
	const { user } = useAuthStore();
	const { mutate } = useLogin();

	useEffect(() => {
		if (!user) {
			mutate({ email: "basic@gmail.com", password: "12345678" });
		}
	}, [user, mutate]);

	if (user) {
		return <Navigate to='/' />;
	}

	return <div>Login Page</div>;
};

export default Login;
