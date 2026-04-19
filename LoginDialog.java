import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginDialog extends JDialog {
    private boolean authenticated = false;
    private UserManager userManager;

    private final Color bgDark = new Color(30, 33, 36);
    private final Color panelDark = new Color(43, 45, 49);
    private final Color textWhite = Color.WHITE;
    private final Color btnBlue = new Color(88, 101, 242);
    private final Color btnGreen = new Color(87, 242, 135);

    public LoginDialog() {
        super((Frame) null, "Login", true);
        userManager = new UserManager();

        setUndecorated(true);
        setSize(450, 520);
        setLocationRelativeTo(null);
        getContentPane().setBackground(bgDark);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        JButton closeBtn = new JButton("X");
        closeBtn.setForeground(Color.LIGHT_GRAY);
        closeBtn.setOpaque(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setFont(new Font("Inter", Font.BOLD, 18));
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> System.exit(0));
        topPanel.add(closeBtn);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(0, 60, 40, 60));

        JLabel titleLabel = new JLabel("Xpense");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 36));
        titleLabel.setForeground(textWhite);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subTitleLabel = new JLabel("Please sign in or register");
        subTitleLabel.setFont(new Font("Inter", Font.PLAIN, 16));
        subTitleLabel.setForeground(new Color(180, 180, 180));
        subTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        JLabel userLabel = new JLabel("Username");
        userLabel.setForeground(new Color(180, 180, 180));
        userLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField userField = new JTextField("");
        userField.setBackground(panelDark);
        userField.setForeground(textWhite);
        userField.setCaretColor(textWhite);
        userField.setFont(new Font("Inter", Font.PLAIN, 16));
        userField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        userField.setPreferredSize(new Dimension(300, 45));
        userField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(panelDark),
                BorderFactory.createEmptyBorder(0, 15, 0, 15)));
        userField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel passLabel = new JLabel("Password");
        passLabel.setForeground(new Color(180, 180, 180));
        passLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPasswordField passField = new JPasswordField("");
        passField.setBackground(panelDark);
        passField.setForeground(textWhite);
        passField.setCaretColor(textWhite);
        passField.setFont(new Font("Inter", Font.PLAIN, 16));
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        passField.setPreferredSize(new Dimension(300, 45));
        passField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(panelDark),
                BorderFactory.createEmptyBorder(0, 15, 0, 15)));
        passField.setAlignmentX(Component.LEFT_ALIGNMENT);

        formPanel.add(userLabel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(userField);
        formPanel.add(Box.createVerticalStrut(25));
        formPanel.add(passLabel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(passField);
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(btnBlue);
        loginButton.setForeground(textWhite);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setFont(new Font("Inter", Font.BOLD, 16));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fields cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (userManager.authenticate(username, password)) {
                authenticated = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton registerButton = new JButton("Register");
        registerButton.setBackground(btnGreen);
        registerButton.setForeground(bgDark);
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setFont(new Font("Inter", Font.BOLD, 16));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        registerButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fields cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (userManager.registerUser(username, password)) {
                JOptionPane.showMessageDialog(this, "Registration successful! You can now log in.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(subTitleLabel);
        centerPanel.add(Box.createVerticalStrut(40));
        centerPanel.add(formPanel);
        centerPanel.add(Box.createVerticalStrut(40));
        centerPanel.add(buttonPanel);

        add(centerPanel, BorderLayout.CENTER);
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}

