import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainFrame extends JFrame {
    private ExpenseManager manager;
    private BudgetManager budgetManager;
    private boolean isDarkMode = false;

    private JPanel contentPanel;
    private CardLayout cardLayout;

    private OverviewCard overviewCard;
    private TransactionsCard transactionsCard;
    private BudgetsCard budgetsCard;
    private ChartsCard chartsCard;
    private CalendarCard calendarCard;
    private AccountCard accountCard;
    private UpcomingCard upcomingCard;
    private CreditCardsCard creditCardsCard;

    public MainFrame(ExpenseManager manager) {
        this.manager = manager;
        this.budgetManager = new BudgetManager();

        setTitle("Xpense");
        setSize(1300, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        applyTheme();

        initSidebar();
        initContentArea();
    }

    private void applyTheme() {
        if (isDarkMode) {
            UIManager.put("Panel.background", new Color(30, 33, 36));
            UIManager.put("Label.foreground", Color.WHITE);
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Table.background", new Color(43, 45, 49));
            UIManager.put("Table.foreground", Color.WHITE);
            UIManager.put("TableHeader.background", new Color(30, 33, 36));
            UIManager.put("TableHeader.foreground", Color.WHITE);
            UIManager.put("ScrollPane.background", new Color(30, 33, 36));
            UIManager.put("Separator.foreground", new Color(60, 63, 68));
            UIManager.put("ComboBox.background", new Color(43, 45, 49));
            UIManager.put("ComboBox.foreground", Color.WHITE);
            UIManager.put("TextField.background", new Color(43, 45, 49));
            UIManager.put("TextField.foreground", Color.WHITE);
            UIManager.put("TextField.caretForeground", Color.WHITE);
            UIManager.put("CheckBox.background", new Color(30, 33, 36));
            UIManager.put("CheckBox.foreground", Color.WHITE);
        } else {
            UIManager.put("Panel.background", new Color(250, 250, 250));
            UIManager.put("Label.foreground", new Color(60, 60, 60));
            UIManager.put("Button.foreground", new Color(60, 60, 60));
            UIManager.put("Table.background", Color.WHITE);
            UIManager.put("Table.foreground", new Color(60, 60, 60));
            UIManager.put("TableHeader.background", new Color(245, 245, 245));
            UIManager.put("TableHeader.foreground", new Color(60, 60, 60));
            UIManager.put("ScrollPane.background", new Color(250, 250, 250));
            UIManager.put("Separator.foreground", new Color(220, 220, 220));
            UIManager.put("ComboBox.background", Color.WHITE);
            UIManager.put("ComboBox.foreground", new Color(60, 60, 60));
            UIManager.put("TextField.background", Color.WHITE);
            UIManager.put("TextField.foreground", new Color(60, 60, 60));
            UIManager.put("TextField.caretForeground", new Color(60, 60, 60));
            UIManager.put("CheckBox.background", new Color(250, 250, 250));
            UIManager.put("CheckBox.foreground", new Color(60, 60, 60));
        }

        getContentPane().setBackground(UIManager.getColor("Panel.background"));
        ThemeManager.applyTheme(this);
        SwingUtilities.updateComponentTreeUI(this);

        refreshAllCards();
    }

    private void toggleTheme() {
        isDarkMode = !isDarkMode;
        applyTheme();
    }

    private void initSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(280, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UIManager.getColor("Separator.foreground")));
        sidebar.setOpaque(true);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setOpaque(false);
        menuPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        Map<String, String> primaryNav = new LinkedHashMap<>();
        primaryNav.put("Overview", "📊");
        primaryNav.put("Transactions", "📋");
        primaryNav.put("Upcoming transactions", "🕒");
        primaryNav.put("Credit cards", "💳");
        primaryNav.put("Budgets", "⏱");
        primaryNav.put("Charts", "🍩");
        primaryNav.put("Calendar", "📅");

        for (Map.Entry<String, String> entry : primaryNav.entrySet()) {
            menuPanel.add(createSidebarButton(entry.getValue() + "   " + entry.getKey(), entry.getKey()));
        }

        JPanel divider = new JPanel();
        divider.setMaximumSize(new Dimension(260, 1));
        divider.setBackground(UIManager.getColor("Separator.foreground"));
        divider.setOpaque(true);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(divider);
        menuPanel.add(Box.createVerticalStrut(10));

        Map<String, String> secondaryNav = new LinkedHashMap<>();
        secondaryNav.put("Settings", "⚙");

        for (Map.Entry<String, String> entry : secondaryNav.entrySet()) {
            menuPanel.add(createSidebarButton(entry.getValue() + "   " + entry.getKey(), entry.getKey()));
        }

        JScrollPane scrollPane = new JScrollPane(menuPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        sidebar.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIManager.getColor("Separator.foreground")));

        JButton profileBtn = createSidebarButton("👤   Profile", "Profile");
        profileBtn.setBorder(new EmptyBorder(15, 20, 10, 20));

        JPanel toggleWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        toggleWrapper.setOpaque(false);
        JCheckBox themeToggle = new JCheckBox("Dark mode");
        themeToggle.setSelected(isDarkMode);
        themeToggle.setFocusPainted(false);
        themeToggle.setFont(new Font("Inter", Font.BOLD, 14));
        themeToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        themeToggle.addActionListener(e -> toggleTheme());
        toggleWrapper.add(themeToggle);

        bottomPanel.add(profileBtn);
        bottomPanel.add(toggleWrapper);

        sidebar.add(bottomPanel, BorderLayout.SOUTH);

        add(sidebar, BorderLayout.WEST);
    }

    private JButton createSidebarButton(String text, String targetCard) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFont(new Font("Inter", Font.BOLD, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(280, 50));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(12, 20, 12, 20));

        if (targetCard.equals("Overview")) {
            btn.setForeground(new Color(24, 144, 255));
        }

        btn.addActionListener(e -> {
            cardLayout.show(contentPanel, targetCard);
            refreshAllCards();
        });

        return btn;
    }

    private SettingsCard settingsCard;

    private void initContentArea() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setOpaque(false);

        overviewCard = new OverviewCard(manager);
        transactionsCard = new TransactionsCard(manager);
        budgetsCard = new BudgetsCard(manager, budgetManager);
        chartsCard = new ChartsCard(manager);
        calendarCard = new CalendarCard(manager);
        accountCard = new AccountCard(manager);
        upcomingCard = new UpcomingCard(manager);
        creditCardsCard = new CreditCardsCard();
        settingsCard = new SettingsCard(manager);

        contentPanel.add(overviewCard, "Overview");
        contentPanel.add(transactionsCard, "Transactions");
        contentPanel.add(upcomingCard, "Upcoming transactions");
        contentPanel.add(creditCardsCard, "Credit cards");
        contentPanel.add(budgetsCard, "Budgets");
        contentPanel.add(chartsCard, "Charts");
        contentPanel.add(calendarCard, "Calendar");
        contentPanel.add(accountCard, "Profile");
        contentPanel.add(settingsCard, "Settings");

        add(contentPanel, BorderLayout.CENTER);

        refreshAllCards();
    }

    public void refreshAllCards() {
        if (overviewCard != null)
            overviewCard.refresh();
        if (transactionsCard != null)
            transactionsCard.refresh();
        if (budgetsCard != null)
            budgetsCard.refresh();
        if (chartsCard != null)
            chartsCard.refresh();
        if (calendarCard != null)
            calendarCard.refresh();
        if (accountCard != null)
            accountCard.refresh();
        if (upcomingCard != null)
            upcomingCard.refresh();
    }
}