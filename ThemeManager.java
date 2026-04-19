import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class ThemeManager {
    public static void applyTheme(Component comp) {
        Color bg = UIManager.getColor("Panel.background");
        Color fg = UIManager.getColor("Label.foreground");

        if (comp instanceof JPanel || comp instanceof JScrollPane || comp instanceof JViewport) {
            if (comp.getName() == null || !comp.getName().equals("customBg")) {
                comp.setBackground(bg);
            }
        } else if (comp instanceof JLabel) {
            Color currentFg = comp.getForeground();
            if (!currentFg.equals(new Color(87, 242, 135)) && !currentFg.equals(new Color(242, 87, 87)) && !currentFg.equals(new Color(24, 144, 255))) {
                comp.setForeground(fg);
            }
        } else if (comp instanceof JCheckBox) {
            comp.setForeground(fg);
            comp.setBackground(bg);
        } else if (comp instanceof JButton) {
            Color currentFg = comp.getForeground();
            if (!currentFg.equals(new Color(87, 242, 135)) && !currentFg.equals(new Color(242, 87, 87)) && !currentFg.equals(Color.WHITE)) {
                comp.setForeground(UIManager.getColor("Button.foreground"));
            }
        } else if (comp instanceof JTextField || comp instanceof JComboBox) {
            comp.setBackground(UIManager.getColor("TextField.background"));
            comp.setForeground(UIManager.getColor("TextField.foreground"));
            if (comp instanceof JTextField) {
                ((JTextField) comp).setCaretColor(UIManager.getColor("TextField.caretForeground"));
            }
        } else if (comp instanceof JTable) {
            comp.setBackground(UIManager.getColor("Table.background"));
            comp.setForeground(UIManager.getColor("Table.foreground"));
            JTableHeader header = ((JTable) comp).getTableHeader();
            if (header != null) {
                header.setBackground(UIManager.getColor("TableHeader.background"));
                header.setForeground(UIManager.getColor("TableHeader.foreground"));
            }
        }

        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                applyTheme(child);
            }
        }
    }
}

