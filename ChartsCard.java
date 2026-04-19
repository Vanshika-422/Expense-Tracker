import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChartsCard extends JPanel {
    private ExpenseManager manager;
    private final String[] CATEGORIES = { "Food", "Transport", "Utilities", "Entertainment", "Shopping", "Other" };
    private final Color[] CATEGORY_COLORS = {
            new Color(237, 66, 69),
            new Color(88, 101, 242),
            new Color(254, 231, 92),
            new Color(155, 89, 182),
            new Color(230, 126, 34),
            new Color(149, 165, 166)
    };

    private PieChartPanel allTimeChart;
    private PieChartPanel currentMonthChart;

    public ChartsCard(ExpenseManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Expense Distribution");
        title.setFont(new Font("Inter", Font.BOLD, 32));
        title.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel chartsContainer = new JPanel(new GridLayout(1, 2, 20, 0));
        chartsContainer.setOpaque(false);

        allTimeChart = new PieChartPanel("All Time Spending", false);
        currentMonthChart = new PieChartPanel("Current Month Spending", true);

        chartsContainer.add(allTimeChart);
        chartsContainer.add(currentMonthChart);

        add(chartsContainer, BorderLayout.CENTER);
    }

    public void refresh() {
        allTimeChart.repaint();
        currentMonthChart.repaint();
    }

    class PieChartPanel extends JPanel {
        private String chartTitle;
        private boolean onlyCurrentMonth;

        public PieChartPanel(String title, boolean onlyCurrentMonth) {
            this.chartTitle = title;
            this.onlyCurrentMonth = onlyCurrentMonth;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            List<Expense> paidExpenses = manager.getAllExpenses().stream()
                    .filter(Expense::isPaid)
                    .collect(Collectors.toList());

            if (onlyCurrentMonth) {
                LocalDate now = LocalDate.now();
                paidExpenses = paidExpenses.stream()
                        .filter(e -> e.getDueDate().getMonth() == now.getMonth()
                                && e.getDueDate().getYear() == now.getYear())
                        .collect(Collectors.toList());
            }

            double total = paidExpenses.stream().mapToDouble(Expense::getAmount).sum();

            int width = getWidth();
            int height = getHeight();
            g2d.setColor(UIManager.getColor("Label.foreground"));
            g2d.setFont(new Font("Inter", Font.BOLD, 20));
            FontMetrics fm = g2d.getFontMetrics();
            int titleWidth = fm.stringWidth(chartTitle);
            g2d.drawString(chartTitle, (width - titleWidth) / 2, 30);

            int diameter = Math.min(width, height) - 150;
            if (diameter < 100)
                diameter = 100;
            int x = (width - diameter) / 2;
            int y = 60;

            if (total == 0) {
                g2d.setColor(UIManager.getColor("Separator.foreground"));
                g2d.fillArc(x, y, diameter, diameter, 0, 360);
                g2d.setColor(UIManager.getColor("Panel.background"));
                g2d.fillArc(x + diameter / 4, y + diameter / 4, diameter / 2, diameter / 2, 0, 360);

                g2d.setColor(UIManager.getColor("Label.foreground"));
                g2d.setFont(new Font("Inter", Font.BOLD, 16));
                g2d.drawString("No Data", x + diameter / 2 - 30, y + diameter / 2 + 5);
                return;
            }

            Map<String, Double> categoryTotals = paidExpenses.stream()
                    .collect(Collectors.groupingBy(Expense::getCategory, Collectors.summingDouble(Expense::getAmount)));

            int startAngle = 0;

            int legendY = y + diameter + 30;

            for (int i = 0; i < CATEGORIES.length; i++) {
                String cat = CATEGORIES[i];
                double catTotal = categoryTotals.getOrDefault(cat, 0.0);
                if (catTotal > 0) {
                    int arcAngle = (int) Math.round((catTotal / total) * 360.0);
                    g2d.setColor(CATEGORY_COLORS[i]);
                    g2d.fillArc(x, y, diameter, diameter, startAngle, arcAngle);
                    startAngle += arcAngle;

                    int legendX = (width / 2) - 80;
                    g2d.fillRect(legendX, legendY, 15, 15);
                    g2d.setColor(UIManager.getColor("Label.foreground"));
                    g2d.setFont(new Font("Inter", Font.BOLD, 14));
                    g2d.drawString(cat + String.format(" (%.1f%%)", (catTotal / total) * 100), legendX + 25,
                            legendY + 12);
                    legendY += 25;
                }
            }

            g2d.setColor(UIManager.getColor("Panel.background"));
            g2d.fillArc(x + diameter / 4, y + diameter / 4, diameter / 2, diameter / 2, 0, 360);

            g2d.setColor(UIManager.getColor("Label.foreground"));
            g2d.setFont(new Font("Inter", Font.BOLD, 18));
            String totalStr = String.format("₹%.0f", total);
            fm = g2d.getFontMetrics();
            int totalWidth = fm.stringWidth(totalStr);
            g2d.drawString(totalStr, x + (diameter - totalWidth) / 2, y + diameter / 2 + 6);
        }
    }
}

