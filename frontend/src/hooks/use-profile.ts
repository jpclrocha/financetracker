import api from "@/lib/api";
import { CustomerResponseDTO } from "@/types/customer-types";
import { useQuery } from "@tanstack/react-query";

export function useProfile() {
	return useQuery({
		queryKey: ["profile"],
		queryFn: async () => {
			const res = await api.get("/auth/profile");
			return res.data as CustomerResponseDTO;
		},
		retry: false,
	});
}
