export interface SummaryRequestDTO {
	startDate: Date;
	endDate: Date | null;
}

export interface SummaryResponseDTO {
	summaryPeriod: string;
	totalIncome: number;
	totalExpenses: number;
	netBalance: number;
	transactions: TransactionTypeSummaryDTO;
	budgets: BudgetSummaryDTO[];
	spendingInsights: SpendingInsightsDTO;
}

interface TransactionTypeSummaryDTO {
	transactionCount: number;
	incomeTransactionCount: number;
	expenseTransactionCount: number;
}

interface BudgetSummaryDTO {
	name: string;
	amount: number;
	amountSpent: number;
	amountRemaining: number;
	usagePercent: number;
}

interface CategorySummaryDTO {
	name: string;
	totalSpent: number;
}

interface SpendingInsightsDTO {
	biggestExpenseCategory: CategorySummaryDTO | null;
	leastExpenseCategory: CategorySummaryDTO | null;
}
