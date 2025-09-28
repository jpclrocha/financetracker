export interface TransactionResponseDTO {
	id: number;
	categoryName: string;
	amount: number;
	date: Date;
	description: string;
	isInstallment: boolean;
}
