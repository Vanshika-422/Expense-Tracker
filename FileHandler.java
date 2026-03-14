import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final String FILE_NAME = "expenses.txt";

    public static void saveToFile(List<Expense> expenses) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Expense e : expenses) {
                writer.println(e.toString());
            }
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    public static List<Expense> loadFromFile() {
        List<Expense> loadedExpenses = new ArrayList<>();
        File file = new File(FILE_NAME);
        
        if (!file.exists()) return loadedExpenses;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String title = parts[0];
                    double amount = Double.parseDouble(parts[1]);
                    String category = parts[2];
                    String date = parts[3];
                    boolean isBill = Boolean.parseBoolean(parts[4]);
                    loadedExpenses.add(new Expense(title, amount, category, date, isBill));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
        return loadedExpenses;
    }
}