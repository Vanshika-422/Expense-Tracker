import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class TransactionsCard extends JPanel {
    private ExpenseManager manager;
    private DefaultTableModel tableModel;
    private JTable ledgerTable;
    private final String[] CATEGORIES = { "Food", "Transport", "Utilities", "Entertainment", "Shopping", "Other" };

    public TransactionsCard(ExpenseManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel title = new JLabel("Transactions");
        title.setFont(new Font("Inter", Font.BOLD, 32));
        headerPanel.add(title, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);

        JButton btnAdd = createStyledButton("Add", new Color(88, 101, 242));
        btnAdd.addActionListener(e -> showExpenseDialog(null));

        JButton btnEdit = createStyledButton("Edit", new Color(241, 196, 15));
        btnEdit.addActionListener(e -> editSelectedExpense());

        JButton btnDelete = createStyledButton("Delete", new Color(237, 66, 69));
        btnDelete.addActionListener(e -> deleteSelectedExpense());

        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        
        headerPanel.add(btnPanel, BorderLayout.EAST);
        String[] columns = { "ID", "Date", "Title", "Category", "Amount", "Status" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ledgerTable = new JTable(tableModel);
        ledgerTable.setRowHeight(40);
        ledgerTable.setFont(new Font("Inter", Font.PLAIN, 16));
        ledgerTable.getTableHeader().setFont(new Font("Inter", Font.BOLD, 16));
        ledgerTable.setShowVerticalLines(false);

        ledgerTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    String status = (String) table.getModel().getValueAt(row, 5);
                    if ("Paid".equals(status)) {
                        c.setForeground(new Color(87, 242, 135));
                    } else if ("Pending".equals(status)) {
                        c.setForeground(new Color(242, 87, 87));
                    } else {
                        c.setForeground(UIManager.getColor("Table.foreground"));
                    }
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(ledgerTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Separator.foreground")));

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Inter", Font.BOLD, 14));
        btn.setBorder(new EmptyBorder(8, 20, 8, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void showExpenseDialog(Expense existingExpense) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), existingExpense == null ? "Add Expense" : "Edit Expense", true);
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 20));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Title:"));
        JTextField txtTitle = new JTextField();
        formPanel.add(txtTitle);

        formPanel.add(new JLabel("Amount (₹):"));
        JTextField txtAmount = new JTextField();
        formPanel.add(txtAmount);

        formPanel.add(new JLabel("Category:"));
        JComboBox<String> cbCategory = new JComboBox<>(CATEGORIES);
        formPanel.add(cbCategory);

        formPanel.add(new JLabel("Due Date:"));
        JPanel datePanel = new JPanel(new GridLayout(1, 3, 5, 0));
        
        LocalDate now = LocalDate.now();
        String[] years = { String.valueOf(now.getYear() - 1), String.valueOf(now.getYear()), String.valueOf(now.getYear() + 1) };
        String[] months = new String[12];
        for (int i = 0; i < 12; i++) months[i] = String.format("%02d", i + 1);
        String[] days = new String[31];
        for (int i = 0; i < 31; i++) days[i] = String.format("%02d", i + 1);

        JComboBox<String> cbYear = new JComboBox<>(years);
        JComboBox<String> cbMonth = new JComboBox<>(months);
        JComboBox<String> cbDay = new JComboBox<>(days);

        datePanel.add(cbYear);
        datePanel.add(cbMonth);
        datePanel.add(cbDay);
        formPanel.add(datePanel);

        formPanel.add(new JLabel("Is Paid?:"));
        JCheckBox chkPaid = new JCheckBox();
        formPanel.add(chkPaid);

        if (existingExpense != null) {
            txtTitle.setText(existingExpense.getTitle());
            txtAmount.setText(String.valueOf(existingExpense.getAmount()));
            cbCategory.setSelectedItem(existingExpense.getCategory());
            cbYear.setSelectedItem(String.valueOf(existingExpense.getDueDate().getYear()));
            cbMonth.setSelectedIndex(existingExpense.getDueDate().getMonthValue() - 1);
            cbDay.setSelectedIndex(existingExpense.getDueDate().getDayOfMonth() - 1);
            chkPaid.setSelected(existingExpense.isPaid());
            chkPaid.setEnabled(false);
        } else {
            cbYear.setSelectedItem(String.valueOf(now.getYear()));
            cbMonth.setSelectedIndex(now.getMonthValue() - 1);
            cbDay.setSelectedIndex(now.getDayOfMonth() - 1);
        }

        dialog.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton btnSave = createStyledButton("Save", new Color(87, 242, 135));
        btnSave.addActionListener(e -> {
            try {
                String title = txtTitle.getText();
                double amount = Double.parseDouble(txtAmount.getText());
                String cat = (String) cbCategory.getSelectedItem();

                int y = Integer.parseInt((String) cbYear.getSelectedItem());
                int m = Integer.parseInt((String) cbMonth.getSelectedItem());
                int d = Integer.parseInt((String) cbDay.getSelectedItem());
                LocalDate date = LocalDate.of(y, m, d);

                boolean isPaid = chkPaid.isSelected();

                if (existingExpense == null) {
                    Expense ex = new Expense(UUID.randomUUID().toString(), title, amount, cat, date, isPaid);
                    manager.addExpense(ex);
                } else {
                    manager.updateExpense(existingExpense.getId(), title, amount, cat, date);
                    if(isPaid && !existingExpense.isPaid()) {
                        manager.markAsPaid(existingExpense.getId());
                    } else if (!isPaid && existingExpense.isPaid()) {
                    }
                }

                dialog.dispose();
                refresh();
                Container parent = getParent();
                while(parent != null && !(parent instanceof MainFrame)) {
                    parent = parent.getParent();
                }
                if(parent instanceof MainFrame) {
                    ((MainFrame) parent).refreshAllCards();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input. Check amount and date.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        btnPanel.add(btnSave);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void editSelectedExpense() {
        int selectedRow = ledgerTable.getSelectedRow();
        if (selectedRow >= 0) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            Expense expense = manager.getAllExpenses().stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
            if (expense != null) {
                showExpenseDialog(expense);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an expense to edit.");
        }
    }

    private void deleteSelectedExpense() {
        int selectedRow = ledgerTable.getSelectedRow();
        if (selectedRow >= 0) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            manager.deleteExpense(id);
            refresh();
            
            Container parent = getParent();
            while(parent != null && !(parent instanceof MainFrame)) {
                parent = parent.getParent();
            }
            if(parent instanceof MainFrame) {
                ((MainFrame) parent).refreshAllCards();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an expense to delete.");
        }
    }

    public void refresh() {
        List<Expense> allExpenses = manager.getAllExpenses();
        tableModel.setRowCount(0);
        for (Expense e : allExpenses) {
            tableModel.addRow(new Object[] {
                    e.getId(), e.getDueDate().toString(), e.getTitle(), e.getCategory(),
                    e.getAmount(), e.isPaid() ? "Paid" : "Pending"
            });
        }
    }
}

