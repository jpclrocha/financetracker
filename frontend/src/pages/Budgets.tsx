import { useState } from "react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Progress } from "@/components/ui/progress";
import { Badge } from "@/components/ui/badge";
import { Plus, Target, AlertTriangle, CheckCircle } from "lucide-react";
import { cn } from "@/lib/utils";

// Mock data
const budgets = [
  {
    id: 1,
    name: "Groceries",
    amount: 600,
    spent: 450,
    categories: ["Groceries", "Food & Dining"],
    period: "Monthly",
    status: "on-track"
  },
  {
    id: 2,
    name: "Entertainment",
    amount: 300,
    spent: 285,
    categories: ["Entertainment", "Streaming Services"],
    period: "Monthly",
    status: "warning"
  },
  {
    id: 3,
    name: "Transportation",
    amount: 200,
    spent: 150,
    categories: ["Transportation", "Gas", "Public Transit"],
    period: "Monthly",
    status: "on-track"
  },
  {
    id: 4,
    name: "Utilities",
    amount: 400,
    spent: 340,
    categories: ["Utilities", "Internet", "Phone"],
    period: "Monthly",
    status: "on-track"
  },
  {
    id: 5,
    name: "Shopping",
    amount: 250,
    spent: 310,
    categories: ["Shopping", "Clothing"],
    period: "Monthly",
    status: "over-budget"
  }
];

const BudgetCard = ({ budget }: { budget: typeof budgets[0] }) => {
  const percentage = (budget.spent / budget.amount) * 100;
  const remaining = budget.amount - budget.spent;
  
  const getStatusIcon = () => {
    switch (budget.status) {
      case "over-budget":
        return <AlertTriangle className="h-4 w-4 text-destructive" />;
      case "warning":
        return <AlertTriangle className="h-4 w-4 text-warning" />;
      default:
        return <CheckCircle className="h-4 w-4 text-success" />;
    }
  };

  const getStatusColor = () => {
    switch (budget.status) {
      case "over-budget":
        return "text-destructive";
      case "warning":
        return "text-warning";
      default:
        return "text-success";
    }
  };

  const getBorderColor = () => {
    switch (budget.status) {
      case "over-budget":
        return "border-destructive/20 bg-destructive/5";
      case "warning":
        return "border-warning/20 bg-warning/5";
      default:
        return "";
    }
  };

  return (
    <Card className={cn("transition-all hover:shadow-md", getBorderColor())}>
      <CardHeader className="pb-3">
        <div className="flex items-center justify-between">
          <CardTitle className="text-lg font-semibold">{budget.name}</CardTitle>
          <div className="flex items-center space-x-2">
            {getStatusIcon()}
            <Badge variant="outline">{budget.period}</Badge>
          </div>
        </div>
      </CardHeader>
      <CardContent className="space-y-4">
        <div className="space-y-2">
          <div className="flex justify-between text-sm">
            <span className="text-muted-foreground">Spent</span>
            <span className="font-medium">
              ${budget.spent.toLocaleString()} / ${budget.amount.toLocaleString()}
            </span>
          </div>
          
          <Progress 
            value={Math.min(percentage, 100)} 
            className="h-2"
          />
          
          <div className="flex justify-between items-center text-sm">
            <span className={cn("font-medium", getStatusColor())}>
              {percentage.toFixed(1)}% used
            </span>
            <span className={cn(
              "font-medium",
              remaining >= 0 ? "text-success" : "text-destructive"
            )}>
              {remaining >= 0 ? "$" : "-$"}
              {Math.abs(remaining).toLocaleString()} remaining
            </span>
          </div>
        </div>

        <div className="space-y-2">
          <p className="text-sm text-muted-foreground">Categories:</p>
          <div className="flex flex-wrap gap-1">
            {budget.categories.map((category, index) => (
              <Badge key={index} variant="secondary" className="text-xs">
                {category}
              </Badge>
            ))}
          </div>
        </div>

        <div className="flex gap-2">
          <Button variant="outline" size="sm" className="flex-1">
            Edit Budget
          </Button>
          <Button variant="ghost" size="sm" className="flex-1">
            View Details
          </Button>
        </div>
      </CardContent>
    </Card>
  );
};

const Budgets = () => {
  const totalBudget = budgets.reduce((sum, budget) => sum + budget.amount, 0);
  const totalSpent = budgets.reduce((sum, budget) => sum + budget.spent, 0);
  const overBudgetCount = budgets.filter(b => b.status === "over-budget").length;
  const warningCount = budgets.filter(b => b.status === "warning").length;

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
          <h1 className="text-3xl font-bold">Budgets</h1>
          <p className="text-muted-foreground">Track and manage your spending limits</p>
        </div>
        <Button className="flex items-center space-x-2">
          <Plus className="h-4 w-4" />
          <span>Create Budget</span>
        </Button>
      </div>

      {/* Summary Cards */}
      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Total Budget
            </CardTitle>
            <Target className="h-4 w-4 text-primary" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">${totalBudget.toLocaleString()}</div>
            <p className="text-xs text-muted-foreground">Across {budgets.length} budgets</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Total Spent
            </CardTitle>
            <Target className="h-4 w-4 text-destructive" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">${totalSpent.toLocaleString()}</div>
            <p className="text-xs text-muted-foreground">
              {((totalSpent / totalBudget) * 100).toFixed(1)}% of total budget
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Over Budget
            </CardTitle>
            <AlertTriangle className="h-4 w-4 text-destructive" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-destructive">{overBudgetCount}</div>
            <p className="text-xs text-muted-foreground">Budgets exceeded</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Warnings
            </CardTitle>
            <AlertTriangle className="h-4 w-4 text-warning" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-warning">{warningCount}</div>
            <p className="text-xs text-muted-foreground">Near budget limit</p>
          </CardContent>
        </Card>
      </div>

      {/* Budget Cards */}
      <div className="grid gap-6 md:grid-cols-2 xl:grid-cols-3">
        {budgets.map((budget) => (
          <BudgetCard key={budget.id} budget={budget} />
        ))}
      </div>
    </div>
  );
};

export default Budgets;