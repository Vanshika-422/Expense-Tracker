import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SettingsCard extends JPanel {
    private ExpenseManager manager;

    public SettingsCard(ExpenseManager manager) {
        this.manager = manager;
        
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("Settings");
        title.setFont(new Font("Inter", Font.BOLD, 36));
        title.setBorder(new EmptyBorder(0, 0, 40, 0));
        add(title, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JButton btnExport = new JButton("Export to Excel (CSV)");
        btnExport.setFont(new Font("Inter", Font.BOLD, 20));
        btnExport.setBackground(new Color(88, 101, 242));
        btnExport.setForeground(Color.WHITE);
        btnExport.setFocusPainted(false);
        btnExport.setBorderPainted(false);
        btnExport.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExport.setMaximumSize(new Dimension(300, 60));
        btnExport.setPreferredSize(new Dimension(300, 60));
        btnExport.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnExport.addActionListener(e -> {
            String result = this.manager.exportDataToCSV();
            JOptionPane.showMessageDialog(this, result, "Export Status", JOptionPane.INFORMATION_MESSAGE);
        });

        contentPanel.add(btnExport);
        
        add(contentPanel, BorderLayout.CENTER);
    }

    public void refresh() {
    }
}

