import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BudgetsCard extends JPanel {
    private ExpenseManager manager;
    private BudgetManager budgetManager;
    private JPanel listPanel;
    private JPanel analysisPanel;
    private JLabel lblAnalysisPrimary;
    private JLabel lblAnalysisSecondary;

    public BudgetsCard(ExpenseManager manager, BudgetManager budgetManager) {
        this.manager = manager;
        this.budgetManager = budgetManager;
        setLayout(new BorderLayout(0, 20));
        setOpaque(false);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Budgets");
        title.setFont(new Font("Inter", Font.BOLD, 32));
        add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setOpaque(false);

        analysisPanel = new JPanel();
        analysisPanel.setLayout(new BoxLayout(analysisPanel, BoxLayout.Y_AXIS));
        analysisPanel.setOpaque(true);
        analysisPanel.setBackground(UIManager.getColor("Panel.background"));
        analysisPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIManager.getColor("Separator.foreground"), 1, true),
                new EmptyBorder(20, 20, 20, 20)));

        lblAnalysisPrimary = new JLabel("You're within your budget!");
        lblAnalysisPrimary.setFont(new Font("Inter", Font.BOLD, 28));
        lblAnalysisPrimary.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblAnalysisSecondary = new JLabel("₹0 left with X days to go.");
        lblAnalysisSecondary.setFont(new Font("Inter", Font.BOLD, 22));
        lblAnalysisSecondary.setForeground(new Color(150, 150, 150));
        lblAnalysisSecondary.setAlignmentX(Component.CENTER_ALIGNMENT);

        analysisPanel.add(lblAnalysisPrimary);
        analysisPanel.add(Box.createVerticalStrut(10));
        analysisPanel.add(lblAnalysisSecondary);

        centerPanel.add(analysisPanel, BorderLayout.NORTH);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    public void refresh() {
        listPanel.removeAll();

        List<Expense> allExpenses = manager.getAllExpenses();
        LocalDate now = LocalDate.now();

        Map<String, Double> currentMonthSpent = new HashMap<>();
        for (Expense e : allExpenses) {
            if (e.isPaid() && e.getDueDate().getMonth() == now.getMonth()
                    && e.getDueDate().getYear() == now.getYear()) {
                currentMonthSpent.put(e.getCategory(),
                        currentMonthSpent.getOrDefault(e.getCategory(), 0.0) + e.getAmount());
            }
        }

        Map<String, Double> categoryBudgets = budgetManager.getBudgets();

        double totalBudget = 0;
        double totalSpent = 0;
        StringBuilder warnings = new StringBuilder();

        for (String category : categoryBudgets.keySet()) {
            double budget = categoryBudgets.get(category);
            double spent = currentMonthSpent.getOrDefault(category, 0.0);
            totalBudget += budget;
            totalSpent += spent;

            int percent = (int) Math.min(100, (spent / budget) * 100);
            if (spent > budget) {
                warnings.append("Over budget in ").append(category).append("! ");
            } else if (percent >= 90) {
                warnings.append("Almost reached limit in ").append(category).append(". ");
            }

            listPanel.add(createBudgetRow(category, spent, budget, percent));
            listPanel.add(Box.createVerticalStrut(20));
        }

        int daysLeft = now.lengthOfMonth() - now.getDayOfMonth();
        double remaining = totalBudget - totalSpent;

        if (totalSpent > totalBudget) {
            lblAnalysisPrimary.setText("You are OVER your total budget!");
            lblAnalysisPrimary.setForeground(new Color(242, 87, 87));
            lblAnalysisSecondary
                    .setText(String.format("Exceeded by ₹%.2f. %s", Math.abs(remaining), warnings.toString()));
        } else {
            lblAnalysisPrimary.setText("You're within your budget!");
            lblAnalysisPrimary.setForeground(new Color(87, 242, 135));
            lblAnalysisSecondary.setText(
                    String.format("₹%.2f left with %d days to go. %s", remaining, daysLeft, warnings.toString()));
        }

        analysisPanel.setBackground(UIManager.getColor("Panel.background"));
        analysisPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIManager.getColor("Separator.foreground"), 1, true),
                new EmptyBorder(20, 20, 20, 20)));

        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel createBudgetRow(String category, double spent, double budget, int percent) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(900, 80));

        JPanel topLabels = new JPanel(new BorderLayout());
        topLabels.setOpaque(false);

        JLabel lblCat = new JLabel(category);
        lblCat.setFont(new Font("Inter", Font.BOLD, 18));

        JPanel rightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightTop.setOpaque(false);

        JLabel lblPercent = new JLabel(percent + "%");
        lblPercent.setFont(new Font("Inter", Font.BOLD, 16));

        JButton btnEdit = new JButton("✎ Edit");
        btnEdit.setFont(new Font("Inter", Font.PLAIN, 12));
        btnEdit.setFocusPainted(false);
        btnEdit.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Enter new budget for " + category + ":", budget);
            if (input != null && !input.trim().isEmpty()) {
                try {
                    double newLimit = Double.parseDouble(input);
                    budgetManager.updateBudget(category, newLimit);
                    refresh();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid number.");
                }
            }
        });

        rightTop.add(lblPercent);
        rightTop.add(btnEdit);

        topLabels.add(lblCat, BorderLayout.WEST);
        topLabels.add(rightTop, BorderLayout.EAST);

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(percent);
        progressBar.setPreferredSize(new Dimension(0, 20));
        progressBar.setBorderPainted(false);

        if (percent >= 100) {
            progressBar.setForeground(new Color(237, 66, 69));
        } else if (percent >= 90) {
            progressBar.setForeground(new Color(254, 231, 92));
        } else {
            progressBar.setForeground(new Color(87, 242, 135));
        }
        progressBar.setBackground(UIManager.getColor("Separator.foreground"));

        JPanel bottomLabels = new JPanel(new BorderLayout());
        bottomLabels.setOpaque(false);

        JLabel lblSpent = new JLabel(String.format("₹%.2f spent", spent));
        lblSpent.setFont(new Font("Inter", Font.PLAIN, 14));

        JLabel lblBudget = new JLabel(String.format("₹%.2f limit", budget));
        lblBudget.setFont(new Font("Inter", Font.PLAIN, 14));

        bottomLabels.add(lblSpent, BorderLayout.WEST);
        bottomLabels.add(lblBudget, BorderLayout.EAST);

        row.add(topLabels, BorderLayout.NORTH);
        row.add(progressBar, BorderLayout.CENTER);
        row.add(bottomLabels, BorderLayout.SOUTH);

        return row;
    }
}

