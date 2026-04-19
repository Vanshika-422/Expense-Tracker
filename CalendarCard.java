import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CalendarCard extends JPanel {
    private ExpenseManager manager;
    private JPanel gridPanel;
    private JLabel monthLabel;
    private YearMonth currentYearMonth;

    public CalendarCard(ExpenseManager manager) {
        this.manager = manager;
        this.currentYearMonth = YearMonth.now();

        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel title = new JLabel("Calendar");
        title.setFont(new Font("Inter", Font.BOLD, 32));
        headerPanel.add(title, BorderLayout.WEST);

        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlsPanel.setOpaque(false);
        
        JButton prevBtn = new JButton("<");
        styleNavBtn(prevBtn);
        prevBtn.addActionListener(e -> {
            currentYearMonth = currentYearMonth.minusMonths(1);
            refresh();
        });

        monthLabel = new JLabel("");
        monthLabel.setFont(new Font("Inter", Font.BOLD, 22));
        monthLabel.setHorizontalAlignment(SwingConstants.CENTER);
        monthLabel.setPreferredSize(new Dimension(200, 30));

        JButton nextBtn = new JButton(">");
        styleNavBtn(nextBtn);
        nextBtn.addActionListener(e -> {
            currentYearMonth = currentYearMonth.plusMonths(1);
            refresh();
        });

        controlsPanel.add(prevBtn);
        controlsPanel.add(monthLabel);
        controlsPanel.add(nextBtn);
        headerPanel.add(controlsPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        gridPanel = new JPanel(new GridLayout(0, 7, 5, 5));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        add(gridPanel, BorderLayout.CENTER);
    }

    private void styleNavBtn(JButton btn) {
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Inter", Font.BOLD, 24));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void refresh() {
        monthLabel.setText(currentYearMonth.getMonth().name() + " " + currentYearMonth.getYear());
        gridPanel.removeAll();

        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : daysOfWeek) {
            JLabel lbl = new JLabel(day, SwingConstants.CENTER);
            lbl.setFont(new Font("Inter", Font.BOLD, 16));
            gridPanel.add(lbl);
        }

        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int dayOfWeekValue = firstOfMonth.getDayOfWeek().getValue() % 7;
        int daysInMonth = currentYearMonth.lengthOfMonth();

        List<Expense> expenses = manager.getAllExpenses();
        Map<LocalDate, List<Expense>> expensesByDate = expenses.stream()
                .filter(e -> YearMonth.from(e.getDueDate()).equals(currentYearMonth))
                .collect(Collectors.groupingBy(Expense::getDueDate));

        for (int i = 0; i < dayOfWeekValue; i++) {
            gridPanel.add(new JLabel(""));
        }

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentYearMonth.atDay(day);
            gridPanel.add(createDayCell(day, expensesByDate.get(date), date.equals(LocalDate.now())));
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel createDayCell(int day, List<Expense> dayExpenses, boolean isToday) {
        JPanel cell = new JPanel(new BorderLayout());
        cell.setOpaque(true);
        cell.setBackground(UIManager.getColor("Panel.background"));
        
        Color borderColor = isToday ? new Color(88, 101, 242) : UIManager.getColor("Separator.foreground");
        cell.setBorder(BorderFactory.createLineBorder(borderColor, isToday ? 2 : 1));

        JLabel lblDay = new JLabel(String.valueOf(day));
        lblDay.setFont(new Font("Inter", Font.BOLD, 16));
        lblDay.setBorder(new EmptyBorder(5, 5, 0, 0));
        cell.add(lblDay, BorderLayout.NORTH);

        if (dayExpenses != null && !dayExpenses.isEmpty()) {
            JPanel entriesPanel = new JPanel();
            entriesPanel.setLayout(new BoxLayout(entriesPanel, BoxLayout.Y_AXIS));
            entriesPanel.setOpaque(false);
            entriesPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
            
            for (Expense e : dayExpenses) {
                JLabel lblExpense = new JLabel(e.getTitle() + " - ₹" + e.getAmount());
                lblExpense.setFont(new Font("Inter", Font.BOLD, 10));
                lblExpense.setOpaque(true);
                lblExpense.setBackground(e.isPaid() ? new Color(87, 242, 135) : new Color(242, 87, 87));
                lblExpense.setForeground(Color.BLACK);
                lblExpense.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
                lblExpense.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                entriesPanel.add(lblExpense);
                entriesPanel.add(Box.createVerticalStrut(2));
            }
            
            JScrollPane scrollPane = new JScrollPane(entriesPanel);
            scrollPane.setBorder(null);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            cell.add(scrollPane, BorderLayout.CENTER);
        }

        return cell;
    }
}

