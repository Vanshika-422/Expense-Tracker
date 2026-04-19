import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UpcomingCard extends JPanel {
    private ExpenseManager manager;
    private ExpenseAnalyst analyst;
    private DefaultTableModel tableModel;
    private JTable upcomingTable;

    public UpcomingCard(ExpenseManager manager) {
        this.manager = manager;
        this.analyst = new ExpenseAnalyst();
        
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Upcoming Transactions");
        title.setFont(new Font("Inter", Font.BOLD, 28));
        title.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        String[] columns = { "Due Date", "Title", "Category", "Amount", "Days Left" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        upcomingTable = new JTable(tableModel);
        upcomingTable.setRowHeight(40);
        upcomingTable.setFont(new Font("Inter", Font.BOLD, 16));
        upcomingTable.getTableHeader().setFont(new Font("Inter", Font.BOLD, 16));
        upcomingTable.setShowVerticalLines(false);

        upcomingTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setForeground(new Color(242, 87, 87));
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(upcomingTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Separator.foreground")));
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refresh() {
        List<Expense> allExpenses = manager.getAllExpenses();
        List<Expense> pending = analyst.getPendingBills(allExpenses);

        tableModel.setRowCount(0);
        java.time.LocalDate today = java.time.LocalDate.now();

        for (Expense e : pending) {
            long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(today, e.getDueDate());
            String daysLeftStr = daysBetween < 0 ? "Overdue by " + Math.abs(daysBetween) + " days" 
                               : daysBetween == 0 ? "Due Today" 
                               : daysBetween + " days";

            tableModel.addRow(new Object[] {
                    e.getDueDate().toString(), 
                    e.getTitle(), 
                    e.getCategory(),
                    "₹" + e.getAmount(), 
                    daysLeftStr
            });
        }
    }
}

