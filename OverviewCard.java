import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class OverviewCard extends JPanel {
    private ExpenseManager manager;
    private JLabel lblTotalSpent;
    private JLabel lblPaid;
    private JLabel lblPending;
    private DefaultTableModel tableModel;
    private JTable recentTable;
    private BarChartPanel barChartPanel;

    public OverviewCard(ExpenseManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout(20, 20));
        setOpaque(false);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel statsGrid = new JPanel(new GridLayout(1, 3, 20, 0));
        statsGrid.setOpaque(false);
        statsGrid.setPreferredSize(new Dimension(0, 120));

        JPanel totalPanel = createStatPanel("Summary (All Time)");
        lblTotalSpent = new JLabel("₹0.00", SwingConstants.LEFT);
        lblTotalSpent.setFont(new Font("Inter", Font.BOLD, 28));
        lblTotalSpent.setForeground(new Color(24, 144, 255)); // Blueish
        totalPanel.add(lblTotalSpent, BorderLayout.CENTER);

        JPanel paidPanel = createStatPanel("Paid Expenses");
        lblPaid = new JLabel("₹0.00", SwingConstants.LEFT);
        lblPaid.setFont(new Font("Inter", Font.BOLD, 28));
        lblPaid.setForeground(new Color(87, 242, 135)); // Green
        paidPanel.add(lblPaid, BorderLayout.CENTER);

        JPanel pendingPanel = createStatPanel("Pending Bills");
        lblPending = new JLabel("₹0.00", SwingConstants.LEFT);
        lblPending.setFont(new Font("Inter", Font.BOLD, 28));
        lblPending.setForeground(new Color(242, 87, 87)); // Red
        pendingPanel.add(lblPending, BorderLayout.CENTER);

        statsGrid.add(totalPanel);
        statsGrid.add(paidPanel);
        statsGrid.add(pendingPanel);

        barChartPanel = new BarChartPanel();
        barChartPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIManager.getColor("Separator.foreground"), 1, true),
                new EmptyBorder(15, 15, 15, 15)));

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(true);
        tablePanel.setBackground(UIManager.getColor("Panel.background"));
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIManager.getColor("Separator.foreground"), 1, true),
                new EmptyBorder(15, 15, 15, 15)));

        JLabel tblTitle = new JLabel("Transactions");
        tblTitle.setFont(new Font("Inter", Font.BOLD, 22));
        tblTitle.setBorder(new EmptyBorder(0, 0, 10, 0));

        String[] columns = { "Date", "Title", "Category", "Amount", "Status" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        recentTable = new JTable(tableModel);
        recentTable.setRowHeight(40);
        recentTable.setFont(new Font("Inter", Font.PLAIN, 16));
        recentTable.getTableHeader().setFont(new Font("Inter", Font.BOLD, 16));
        recentTable.setShowVerticalLines(false);
        recentTable.setIntercellSpacing(new Dimension(0, 0));

        recentTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    String status = (String) table.getModel().getValueAt(row, 4);
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

        JScrollPane scrollPane = new JScrollPane(recentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        tablePanel.add(tblTitle, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, barChartPanel, tablePanel);
        splitPane.setResizeWeight(0.5);
        splitPane.setOpaque(false);
        splitPane.setBorder(null);
        splitPane.setDividerSize(10);

        add(statsGrid, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createStatPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIManager.getColor("Separator.foreground"), 1, true),
                new EmptyBorder(15, 15, 15, 15)));

        panel.setOpaque(true);
        panel.setBackground(UIManager.getColor("Panel.background"));

        JLabel titleLabel = new JLabel(title, SwingConstants.LEFT);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 14));
        panel.add(titleLabel, BorderLayout.NORTH);

        return panel;
    }

    public void refresh() {
        List<Expense> allExpenses = manager.getAllExpenses();

        double totalSpent = allExpenses.stream().mapToDouble(Expense::getAmount).sum();
        double paidSpent = allExpenses.stream().filter(Expense::isPaid).mapToDouble(Expense::getAmount).sum();
        double pendingSpent = allExpenses.stream().filter(e -> !e.isPaid()).mapToDouble(Expense::getAmount).sum();

        lblTotalSpent.setText(String.format("₹%.2f", totalSpent));
        lblPaid.setText(String.format("₹%.2f", paidSpent));
        lblPending.setText(String.format("₹%.2f", pendingSpent));

        tableModel.setRowCount(0);

        for (int i = Math.max(0, allExpenses.size() - 10); i < allExpenses.size(); i++) {
            Expense e = allExpenses.get(i);
            tableModel.addRow(new Object[] {
                    e.getDueDate().toString(), e.getTitle(), e.getCategory(),
                    "₹" + e.getAmount(), e.isPaid() ? "Paid" : "Pending"
            });
        }

        Component[] comps = ((JPanel) getComponent(0)).getComponents();
        for (Component c : comps) {
            if (c instanceof JPanel) {
                c.setBackground(UIManager.getColor("Panel.background"));
                ((JPanel) c).setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(UIManager.getColor("Separator.foreground"), 1, true),
                        new EmptyBorder(15, 15, 15, 15)));
            }
        }

        Component split = getComponent(1);
        if (split instanceof JSplitPane) {
            Component bottom = ((JSplitPane) split).getBottomComponent();
            if (bottom instanceof JPanel) {
                bottom.setBackground(UIManager.getColor("Panel.background"));
                ((JPanel) bottom).setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(UIManager.getColor("Separator.foreground"), 1, true),
                        new EmptyBorder(15, 15, 15, 15)));
            }
        }

        barChartPanel.setBackground(UIManager.getColor("Panel.background"));
        barChartPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIManager.getColor("Separator.foreground"), 1, true),
                new EmptyBorder(15, 15, 15, 15)));

        barChartPanel.setData(allExpenses);
        barChartPanel.repaint();
    }

    class BarChartPanel extends JPanel {
        private double[] data = new double[7];
        private String[] labels = new String[7];

        public BarChartPanel() {
            setOpaque(true);
            setLayout(new BorderLayout());
            JLabel title = new JLabel("Last 7 Days");
            title.setFont(new Font("Inter", Font.BOLD, 16));
            add(title, BorderLayout.NORTH);
        }

        public void setData(List<Expense> expenses) {
            LocalDate today = LocalDate.now();
            for (int i = 0; i < 7; i++) {
                LocalDate d = today.minusDays(6 - i);
                labels[i] = d.getDayOfMonth() + " " + d.getMonth().name().substring(0, 3);

                data[i] = expenses.stream()
                        .filter(e -> e.getDueDate().equals(d) && e.isPaid())
                        .mapToDouble(Expense::getAmount).sum();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight() - 30;
            int yOffset = 30;

            double max = 0;
            for (double d : data)
                if (d > max)
                    max = d;
            if (max == 0)
                max = 100;

            int barWidth = (width - 40) / 7 - 10;
            int x = 20;

            g2d.setFont(new Font("Inter", Font.PLAIN, 10));
            Color fg = UIManager.getColor("Label.foreground");

            for (int i = 0; i < 7; i++) {
                int barHeight = (int) ((data[i] / max) * (height - 30));

                g2d.setColor(new Color(237, 66, 69));
                g2d.fillRect(x, yOffset + height - 20 - barHeight, barWidth, barHeight);

                g2d.setColor(fg);
                g2d.drawString(labels[i], x, yOffset + height - 5);

                x += barWidth + 10;
            }
        }
    }
}
