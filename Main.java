import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        ExpenseManager manager = new ExpenseManager();
        ExpenseAnalyst analyst = new ExpenseAnalyst();

        SwingUtilities.invokeLater(() -> {
            new ExpenseTrackerGUI(manager, analyst);
        });
    }
}