import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ExpenseAnalyst {

    public List<Expense> getPendingBills(List<Expense> expenses) {
        return expenses.stream()
                .filter(e -> !e.isPaid())
                .sorted((e1, e2) -> e1.getDueDate().compareTo(e2.getDueDate()))
                .collect(Collectors.toList());
    }

    public String generateFinancialAdvice(List<Expense> expenses) {
        long smallPaymentsCount = expenses.stream()
                .filter(e -> e.getAmount() < 500 && e.isPaid())
                .count();

        double totalUnnecessary = expenses.stream()
                .filter(e -> e.getCategory().equalsIgnoreCase("Entertainment")
                        || e.getCategory().equalsIgnoreCase("Shopping"))
                .mapToDouble(Expense::getAmount)
                .sum();

        StringBuilder advice = new StringBuilder();

        if (smallPaymentsCount > 3) {
            advice.append("⚠️ Tip: You have ").append(smallPaymentsCount)
                    .append(" small payments. These 'micro-transactions' add up quickly! ");
        }

        if (totalUnnecessary > 2000) {
            advice.append("💡 Consider reducing your Shopping/Entertainment budget. You've spent ₹")
                    .append(totalUnnecessary).append(" here recently.");
        }

        if (advice.length() == 0) {
            return "✅ Your spending looks highly optimized. Great job!";
        }

        return advice.toString();
    }

    public Map<String, Double> getCategoryBreakdown(List<Expense> expenses) {
        Map<String, Double> breakdown = new HashMap<>();
        for (Expense e : expenses) {
            if (e.isPaid()) {
                breakdown.put(e.getCategory(), breakdown.getOrDefault(e.getCategory(), 0.0) + e.getAmount());
            }
        }
        return breakdown;
    }
}
