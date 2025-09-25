import { SummaryCard } from "@/components/dashboard/SummaryCard";
import { BudgetCard } from "@/components/dashboard/BudgetCard";
import {
	Card,
	CardContent,
	CardDescription,
	CardHeader,
	CardTitle,
} from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import {
	DollarSign,
	TrendingUp,
	TrendingDown,
	Receipt,
	Plus,
	ArrowUpRight,
	ArrowDownRight,
} from "lucide-react";

// Mock data - replace with actual API calls
const summaryData = {
	totalIncome: 5420.0,
	totalExpenses: 3240.5,
	netBalance: 2179.5,
	transactionCount: 47,
};

const budgets = [
	{ name: "Groceries", spent: 450, budget: 600 },
	{ name: "Entertainment", spent: 280, budget: 300 },
	{ name: "Transportation", spent: 150, budget: 200 },
	{ name: "Utilities", spent: 340, budget: 400 },
];

const recentTransactions = [
	{
		id: 1,
		description: "Grocery Store",
		amount: -85.3,
		category: "Groceries",
		date: "2024-01-15",
	},
	{
		id: 2,
		description: "Salary Deposit",
		amount: 3200.0,
		category: "Income",
		date: "2024-01-15",
	},
	{
		id: 3,
		description: "Netflix Subscription",
		amount: -15.99,
		category: "Entertainment",
		date: "2024-01-14",
	},
	{
		id: 4,
		description: "Gas Station",
		amount: -45.2,
		category: "Transportation",
		date: "2024-01-13",
	},
	{
		id: 5,
		description: "Electric Bill",
		amount: -120.5,
		category: "Utilities",
		date: "2024-01-12",
	},
];

const Dashboard = () => {
	return (
		<div className='space-y-6'>
			{/* Header */}
			<div className='flex justify-between items-center'>
				<div>
					<h1 className='text-3xl font-bold'>Dashboard</h1>
					<p className='text-muted-foreground'>
						Welcome back! Here's your financial overview for January
						2024.
					</p>
				</div>
				<Button className='flex items-center space-x-2'>
					<Plus className='h-4 w-4' />
					<span>Add Transaction</span>
				</Button>
			</div>

			{/* Summary Cards */}
			<div className='grid gap-4 md:grid-cols-2 lg:grid-cols-4'>
				<SummaryCard
					title='Total Income'
					value={`$${summaryData.totalIncome.toLocaleString()}`}
					icon={TrendingUp}
					variant='success'
					trend={{ value: "12%", isPositive: true }}
				/>
				<SummaryCard
					title='Total Expenses'
					value={`$${summaryData.totalExpenses.toLocaleString()}`}
					icon={TrendingDown}
					variant='destructive'
					trend={{ value: "5%", isPositive: false }}
				/>
				<SummaryCard
					title='Net Balance'
					value={`$${summaryData.netBalance.toLocaleString()}`}
					icon={DollarSign}
					variant='success'
				/>
				<SummaryCard
					title='Transactions'
					value={summaryData.transactionCount.toString()}
					icon={Receipt}
				/>
			</div>

			<div className='grid gap-6 lg:grid-cols-3'>
				{/* Recent Transactions */}
				<div className='lg:col-span-2'>
					<Card>
						<CardHeader className='flex flex-row items-center justify-between'>
							<div>
								<CardTitle>Recent Transactions</CardTitle>
								<CardDescription>
									Your latest financial activity
								</CardDescription>
							</div>
							<Button variant='outline' size='sm'>
								View All
							</Button>
						</CardHeader>
						<CardContent>
							<div className='space-y-4'>
								{recentTransactions.map((transaction) => (
									<div
										key={transaction.id}
										className='flex items-center justify-between p-3 rounded-lg border'
									>
										<div className='flex items-center space-x-4'>
											<div
												className={`p-2 rounded-full ${
													transaction.amount > 0
														? "bg-success/10"
														: "bg-muted"
												}`}
											>
												{transaction.amount > 0 ? (
													<ArrowUpRight className='h-4 w-4 text-success' />
												) : (
													<ArrowDownRight className='h-4 w-4 text-destructive' />
												)}
											</div>
											<div>
												<p className='font-medium'>
													{transaction.description}
												</p>
												<p className='text-sm text-muted-foreground'>
													{transaction.category} •{" "}
													{transaction.date}
												</p>
											</div>
										</div>
										<div
											className={`font-medium ${
												transaction.amount > 0
													? "text-success"
													: "text-foreground"
											}`}
										>
											{transaction.amount > 0 ? "+" : ""}$
											{Math.abs(
												transaction.amount
											).toLocaleString()}
										</div>
									</div>
								))}
							</div>
						</CardContent>
					</Card>
				</div>

				{/* Budget Overview */}
				<div>
					<Card>
						<CardHeader className='flex flex-row items-center justify-between'>
							<div>
								<CardTitle>Budget Overview</CardTitle>
								<CardDescription>
									January 2024 spending progress
								</CardDescription>
							</div>
							<Button variant='outline' size='sm'>
								Manage
							</Button>
						</CardHeader>
						<CardContent>
							<div className='space-y-4'>
								{budgets.map((budget, index) => (
									<BudgetCard
										key={index}
										name={budget.name}
										spent={budget.spent}
										budget={budget.budget}
									/>
								))}
							</div>
						</CardContent>
					</Card>
				</div>
			</div>
		</div>
	);
};

export default Dashboard;
