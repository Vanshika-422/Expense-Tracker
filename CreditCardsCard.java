import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CreditCardsCard extends JPanel {

    public CreditCardsCard() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Credit Cards");
        title.setFont(new Font("Inter", Font.BOLD, 28));
        title.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 30));
        cardsPanel.setOpaque(false);

        cardsPanel.add(createCard("HDFC Bank Platinum", "4321 •••• •••• 9012", 45000.0, 150000.0, new Color(24, 144, 255)));
        cardsPanel.add(createCard("SBI SimplySAVE", "5432 •••• •••• 3456", 12500.0, 75000.0, new Color(155, 89, 182)));
        cardsPanel.add(createCard("Axis Bank Neo", "4111 •••• •••• 1111", 0.0, 100000.0, new Color(230, 126, 34)));

        add(cardsPanel, BorderLayout.CENTER);
    }

    private JPanel createCard(String bankName, String number, double outstanding, double limit, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setName("customBg");
        card.setPreferredSize(new Dimension(350, 220));
        card.setBackground(bgColor);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblBank = new JLabel(bankName);
        lblBank.setFont(new Font("Inter", Font.BOLD, 22));
        lblBank.setForeground(Color.WHITE);

        JLabel lblNumber = new JLabel(number);
        lblNumber.setFont(new Font("Inter", Font.PLAIN, 18));
        lblNumber.setForeground(new Color(255, 255, 255, 200));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(lblBank, BorderLayout.NORTH);
        top.add(lblNumber, BorderLayout.SOUTH);

        JLabel lblOutstandingLabel = new JLabel("Outstanding Balance");
        lblOutstandingLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        lblOutstandingLabel.setForeground(new Color(255, 255, 255, 200));

        JLabel lblOutstanding = new JLabel(String.format("₹%.2f", outstanding));
        lblOutstanding.setFont(new Font("Inter", Font.BOLD, 26));
        lblOutstanding.setForeground(Color.WHITE);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        
        JPanel balPanel = new JPanel(new BorderLayout());
        balPanel.setOpaque(false);
        balPanel.add(lblOutstandingLabel, BorderLayout.NORTH);
        balPanel.add(lblOutstanding, BorderLayout.CENTER);
        
        JLabel lblLimit = new JLabel(String.format("Limit: ₹%.2f", limit));
        lblLimit.setFont(new Font("Inter", Font.PLAIN, 14));
        lblLimit.setForeground(new Color(255, 255, 255, 200));
        
        bottom.add(balPanel, BorderLayout.WEST);
        bottom.add(lblLimit, BorderLayout.EAST);

        card.add(top, BorderLayout.NORTH);
        card.add(bottom, BorderLayout.SOUTH);

        return card;
    }
}

