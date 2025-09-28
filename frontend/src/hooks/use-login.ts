import api from "@/lib/api";
import { AuthRequestDTO } from "@/types/auth-types";
import { useMutation } from "@tanstack/react-query";
import { AxiosResponse } from "axios";
import { toast } from "./use-toast";

const loginUser = async ({
	email,
	password,
}: AuthRequestDTO): Promise<AxiosResponse<void>> => {
	return (await api.post("/auth/login", { email, password })).data;
};

export const useLogin = () => {
	return useMutation({
		mutationFn: loginUser,
		onSuccess: ({ data }) => {
			toast({
				title: "Login Successful",
				variant: "default",
			});
		},
		onError: (err) => {
			console.error("Login failed:", err.message);
		},
	});
};
