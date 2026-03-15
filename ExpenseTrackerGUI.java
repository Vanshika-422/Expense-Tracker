import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ExpenseTrackerGUI extends JFrame {
    private ExpenseManager manager;
    private ExpenseAnalyst analyst;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel totalLabel, highestLabel;

    public ExpenseTrackerGUI(ExpenseManager manager, ExpenseAnalyst analyst) {
        this.manager = manager;
        this.analyst = analyst;

        setTitle("Expense Tracker v1.0");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel dashboard = new JPanel(new GridLayout(1, 2, 10, 10));
        dashboard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        totalLabel = createStatCard("Total Spent", "₹0.0");
        highestLabel = createStatCard("Highest Expense", "₹0.0");
        
        dashboard.add(totalLabel);
        dashboard.add(highestLabel);
        add(dashboard, BorderLayout.NORTH);

        String[] columns = {"Title", "Amount", "Category", "Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton addBtn = new JButton("Add Expense");
        JButton refreshBtn = new JButton("Refresh Statistics");

        addBtn.addActionListener(e -> showAddDialog());
        refreshBtn.addActionListener(e -> updateDashboard());

        btnPanel.add(addBtn);
        btnPanel.add(refreshBtn);
        add(btnPanel, BorderLayout.SOUTH);

        refreshData();
        setVisible(true);
    }

    private JLabel createStatCard(String title, String value) {
        JLabel label = new JLabel("<html><div style='text-align: center; padding:10px; border:1px solid gray; border-radius:5px;'>" 
                                  + "<b style='font-size:12px;'>" + title + "</b><br><span style='font-size:16px; color:blue;'>" 
                                  + value + "</span></div></html>");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private void showAddDialog() {
        JTextField titleF = new JTextField();
        JTextField amountF = new JTextField();
        String[] cats = {"Food", "Transport", "Shopping", "Bills", "Health", "Other"};
        JComboBox<String> catF = new JComboBox<>(cats);
        JTextField dateF = new JTextField("2026-03-12");
        JCheckBox isBillF = new JCheckBox("Is this an upcoming bill?");

        Object[] message = {
            "Title:", titleF,
            "Amount:", amountF,
            "Category:", catF,
            "Date (YYYY-MM-DD):", dateF,
            isBillF
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Add New Entry", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String title = titleF.getText();
                double amount = Double.parseDouble(amountF.getText());
                String category = (String) catF.getSelectedItem();
                String date = dateF.getText();
                boolean isBill = isBillF.isSelected();

                manager.addExpense(new Expense(title, amount, category, date, isBill));
                refreshData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Input. Please check amount.");
            }
        }
    }

    private void refreshData() {
        tableModel.setRowCount(0);
        List<Expense> all = manager.getAllExpenses();
        for (Expense e : all) {
            Object[] row = {e.getTitle(), "₹" + e.getAmount(), e.getCategory(), e.getDate(), e.isBill() ? "PENDING" : "PAID"};
            tableModel.addRow(row);
        }
        updateDashboard();
    }

    private void updateDashboard() {
        double total = analyst.calculateTotalSpent(manager.getAllExpenses());
        totalLabel.setText("<html><div style='text-align: center; padding:10px; border:1px solid gray; border-radius:5px;'>" 
                           + "<b>Total Spent</b><br><span style='color:blue;'>₹" + total + "</span></div></html>");
        
        if (!manager.getAllExpenses().isEmpty()) {
            Expense high = manager.getAllExpenses().get(0);
            for (Expense e : manager.getAllExpenses()) {
                if (e.getAmount() > high.getAmount()) high = e;
            }
            highestLabel.setText("<html><div style='text-align: center; padding:10px; border:1px solid gray; border-radius:5px;'>" 
                                 + "<b>Highest Expense</b><br><span style='color:red;'>₹" + high.getAmount() + "</span></div></html>");
        }
    }
}
