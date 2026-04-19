import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Font;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
                    UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
                    UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 13));
                    UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 13));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                LoginDialog loginDialog = new LoginDialog();
                loginDialog.setVisible(true);

                if (loginDialog.isAuthenticated()) {
                    ExpenseManager manager = new ExpenseManager();

                    MainFrame mainFrame = new MainFrame(manager);

                    mainFrame.setVisible(true);
                } else {
                    System.exit(0);
                }
            }
        });
    }
}
