import java.time.LocalDate;

public class Expense {
    private String id;
    private String title;
    private double amount;
    private String category;
    private LocalDate dueDate;
    private boolean isPaid;

    public Expense(String id, String title, double amount, String category, LocalDate dueDate, boolean isPaid) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.dueDate = dueDate;
        this.isPaid = isPaid;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
