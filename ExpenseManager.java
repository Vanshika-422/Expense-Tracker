import java.util.List;
import java.util.stream.Collectors;

public class ExpenseManager {
    private List<Expense> expenses;

    public ExpenseManager() {
        // FIXED: Load existing data from file immediately when program starts
        this.expenses = FileHandler.loadFromFile();
    }

    public void addExpense(Expense e) {
        expenses.add(e);
        FileHandler.saveToFile(expenses); // FIXED: Auto-save
    }

    public void deleteExpense(int index) {
        if (index >= 0 && index < expenses.size()) {
            expenses.remove(index);
            FileHandler.saveToFile(expenses); // FIXED: Auto-save
        }
    }

    public List<Expense> getAllExpenses() {
        return expenses;
    }

    public List<Expense> filterByCategory(String category) {
        return expenses.stream()
                .filter(e -> e.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public List<Expense> filterByDate(String date) {
        return expenses.stream()
                .filter(e -> e.getDate().equals(date))
                .collect(Collectors.toList());
    }

    public double calculateTotal() {
        double total = 0;
        for (Expense e : expenses) {
            total += e.getAmount();
        }
        return total;
    }

    public void editExpense(int index, Expense updatedExpense) {
        if (index >= 0 && index < expenses.size()) {
            expenses.set(index, updatedExpense);
            FileHandler.saveToFile(expenses); // FIXED: Auto-save
        }
    }
}