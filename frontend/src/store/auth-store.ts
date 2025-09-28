import { CustomerResponseDTO } from "@/types/customer-types";
import { create } from "zustand/react";

interface AuthState {
	user: CustomerResponseDTO | null;
	setUser: (user: CustomerResponseDTO | null) => void;
	logout: () => void;
}

export const useAuthStore = create<AuthState>((set) => ({
	user: null,
	setUser: (user) => set({ user }),
	logout: () => set({ user: null }),
}));
