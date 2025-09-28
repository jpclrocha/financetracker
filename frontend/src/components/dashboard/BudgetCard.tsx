import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Progress } from "@/components/ui/progress";
import { cn } from "@/lib/utils";

interface BudgetCardProps {
  name: string;
  spent: number;
  budget: number;
  currency?: string;
}

export const BudgetCard = ({ 
  name, 
  spent, 
  budget, 
  currency = "$" 
}: BudgetCardProps) => {
  const percentage = (spent / budget) * 100;
  const remaining = budget - spent;
  
  const getProgressColor = () => {
    if (percentage >= 90) return "text-destructive";
    if (percentage >= 75) return "text-warning";
    return "text-success";
  };

  const getProgressVariant = () => {
    if (percentage >= 100) return "destructive";
    if (percentage >= 90) return "warning";
    return "default";
  };

  return (
    <Card className="transition-all hover:shadow-md">
      <CardHeader className="pb-3">
        <CardTitle className="text-base font-medium capitalize">{name}</CardTitle>
      </CardHeader>
      <CardContent className="space-y-3">
        <div className="flex justify-between text-sm">
          <span className="text-muted-foreground">Spent</span>
          <span className="font-medium">
            {currency}{spent.toLocaleString()} / {currency}{budget.toLocaleString()}
          </span>
        </div>
        
        <Progress 
          value={Math.min(percentage, 100)} 
          className="h-2"
          // Note: Progress component would need variant support added
        />
        
        <div className="flex justify-between items-center text-sm">
          <span className={cn("font-medium", getProgressColor())}>
            {percentage.toFixed(1)}% used
          </span>
          <span className={cn(
            "font-medium",
            remaining >= 0 ? "text-success" : "text-destructive"
          )}>
            {remaining >= 0 ? currency : "-" + currency}
            {Math.abs(remaining).toLocaleString()} remaining
          </span>
        </div>
      </CardContent>
    </Card>
  );
};