import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class AccountCard extends JPanel {
    private ExpenseManager manager;
    private JLabel lblTotalTransactions;
    private JLabel lblLifetimeSpend;

    public AccountCard(ExpenseManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(40, 40, 40, 40));

        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.setOpaque(false);

        JLabel avatarLabel = new JLabel("VR", SwingConstants.CENTER);
        avatarLabel.setFont(new Font("Inter", Font.BOLD, 48));
        avatarLabel.setForeground(Color.WHITE);
        avatarLabel.setOpaque(true);
        avatarLabel.setBackground(new Color(88, 101, 242)); 
        avatarLabel.setPreferredSize(new Dimension(100, 100));
        avatarLabel.setMaximumSize(new Dimension(100, 100));
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel("Vanshika Rana");
        nameLabel.setFont(new Font("Inter", Font.BOLD, 36));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel emailLabel = new JLabel("vanshika.rana@example.com");
        emailLabel.setFont(new Font("Inter", Font.PLAIN, 18));
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        profilePanel.add(avatarLabel);
        profilePanel.add(Box.createVerticalStrut(20));
        profilePanel.add(nameLabel);
        profilePanel.add(Box.createVerticalStrut(10));
        profilePanel.add(emailLabel);
        profilePanel.add(Box.createVerticalStrut(50));

        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        statsPanel.setOpaque(false);

        lblTotalTransactions = new JLabel("Total Transactions: 0");
        lblTotalTransactions.setFont(new Font("Inter", Font.BOLD, 22));
        lblTotalTransactions.setHorizontalAlignment(SwingConstants.CENTER);

        lblLifetimeSpend = new JLabel("Lifetime Spend: ₹0.00");
        lblLifetimeSpend.setFont(new Font("Inter", Font.BOLD, 22));
        lblLifetimeSpend.setHorizontalAlignment(SwingConstants.CENTER);

        statsPanel.add(lblTotalTransactions);
        statsPanel.add(lblLifetimeSpend);

        add(profilePanel, BorderLayout.NORTH);
        add(statsPanel, BorderLayout.CENTER);
    }

    public void refresh() {
        List<Expense> allExpenses = manager.getAllExpenses();
        int totalTransactions = allExpenses.size();
        double lifetimeSpend = allExpenses.stream().filter(Expense::isPaid).mapToDouble(Expense::getAmount).sum();

        lblTotalTransactions.setText("Total Transactions: " + totalTransactions);
        lblLifetimeSpend.setText(String.format("Lifetime Spend: ₹%.2f", lifetimeSpend));
        
        SwingUtilities.updateComponentTreeUI(this);
    }
}

