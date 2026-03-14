import java.util.List;

public class ExpenseAnalyst {

    // 1. CALCULATE TOTAL SPENT (Excludes upcoming bills)
    public double calculateTotalSpent(List<Expense> list) {
        double total = 0;
        for (Expense e : list) {
            if (!e.isBill()) { 
                total += e.getAmount();
            }
        }
        return total;
    }

    // 2. CATEGORY WISE TOTAL
    public double getCategoryTotal(List<Expense> list, String categoryName) {
        double catTotal = 0;
        for (Expense e : list) {
            if (e.getCategory().equalsIgnoreCase(categoryName)) {
                catTotal += e.getAmount();
            }
        }
        return catTotal;
    }

    // 3. CALENDAR SEARCH (Finds what is due/paid on a specific date)
    public void showPaymentsForDate(List<Expense> list, String targetDate) {
        System.out.println("\n--- Payments for " + targetDate + " ---");
        boolean found = false;
        for (Expense e : list) {
            if (e.getDate().equals(targetDate)) {
                String status = e.isBill() ? "[PENDING BILL]" : "[PAID]";
                System.out.println("- " + e.getTitle() + ": ₹" + e.getAmount() + " " + status);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Nothing found for this date.");
        }
    }

    // 4. HIGHEST EXPENSE ke liye ek method 
    public void showHighestExpense(List<Expense> list) {
        if (list.isEmpty()) return;
        Expense highest = list.get(0);
        for (Expense e : list) {
            if (e.getAmount() > highest.getAmount()) {
                highest = e;
            }
        }
        System.out.println("Highest Expense: ₹" + highest.getAmount() + " (" + highest.getTitle() + ")");
    }
}