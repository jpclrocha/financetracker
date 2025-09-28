import api from "@/lib/api";
import { SummaryRequestDTO, SummaryResponseDTO } from "@/types/dashboard-types";
import { useQuery } from "@tanstack/react-query";

const getSummary = async ({
	startDate,
	endDate,
}: SummaryRequestDTO): Promise<SummaryResponseDTO> => {
	return (await api.post("/dashboard/summary", { startDate, endDate })).data;
};

export const useSummary = (startDate, endDate) => {
	return useQuery({
		queryKey: ["dashboard-summary"],
		queryFn: () => getSummary({ startDate, endDate }),
		enabled: !!startDate,
	});
};
