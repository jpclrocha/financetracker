import api from "@/lib/api";
import { TransactionResponseDTO } from "@/types/transaction-types";
import { useQuery } from "@tanstack/react-query";

const getRecentTransactions = async (): Promise<TransactionResponseDTO[]> => {
	return (await api.get("/transaction?sort=date,asc&size=5")).data;
};

export const useRecentTransactions = () => {
	return useQuery({
		queryKey: ["recent-transactions"],
		queryFn: () => getRecentTransactions(),
	});
};
