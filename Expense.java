public class Expense {
    private String title;
    private double amount;
    private String category;
    private String date; 
    private boolean isBill;

    public Expense(String title, double amount, String category, String date, boolean isBill) {
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.isBill = isBill;
    }

    public String getTitle() { return title; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getDate() { return date; }
    public boolean isBill() { return isBill; }

    @Override
    public String toString() {
        return title + "," + amount + "," + category + "," + date + "," + isBill;
    }
}