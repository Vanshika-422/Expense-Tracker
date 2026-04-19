import java.util.List;

public class ExpenseManager {
    private List<Expense> expenses;

    public ExpenseManager() {
        this.expenses = FileHandler.loadExpenses();
    }

    public void addExpense(Expense e) {
        expenses.add(e);
        FileHandler.saveExpenses(expenses);
    }

    public void markAsPaid(String id) {
        for (Expense e : expenses) {
            if (e.getId().equals(id)) {
                e.setPaid(true);
                FileHandler.saveExpenses(expenses);
                break;
            }
        }
    }

    public void deleteExpense(String id) {
        expenses.removeIf(e -> e.getId().equals(id));
        FileHandler.saveExpenses(expenses);
    }

    public void updateExpense(String id, String newTitle, double newAmount, String newCategory,
            java.time.LocalDate newDate) {
        for (Expense e : expenses) {
            if (e.getId().equals(id)) {
                e.setTitle(newTitle);
                e.setAmount(newAmount);
                e.setCategory(newCategory);
                e.setDueDate(newDate);
                FileHandler.saveExpenses(expenses);
                break;
            }
        }
    }

    public List<Expense> getAllExpenses() {
        return expenses;
    }

    public String exportDataToCSV() {
        String desktopPath = System.getProperty("user.home") + "/Desktop/Financial_Report.csv";
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.File(desktopPath))) {
            writer.println("ID,Title,Amount,Category,Date,Status");
            for (Expense e : expenses) {
                String status = e.isPaid() ? "Paid" : "Pending";
                writer.println(e.getId() + "," + e.getTitle() + "," + e.getAmount() + "," + e.getCategory() + ","
                        + e.getDueDate() + "," + status);
            }
            return "✅ Success! Saved to Desktop: Financial_Report.csv";
        } catch (Exception ex) {
            return "❌ Error saving CSV file.";
        }
    }
}
