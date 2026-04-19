import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final String FILE_NAME = "expenses.txt";

    public static void saveExpenses(List<Expense> expenses) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Expense e : expenses) {
                bw.write(e.getId() + "," + e.getTitle() + "," + e.getAmount() + "," +
                        e.getCategory() + "," + e.getDueDate() + "," + e.isPaid());
                bw.newLine();
            }
        } catch (IOException ex) {
            System.out.println("Error saving file: " + ex.getMessage());
        }
    }

    public static List<Expense> loadExpenses() {
        List<Expense> expenses = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists())
            return expenses;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    Expense e = new Expense(
                            data[0], data[1], Double.parseDouble(data[2]),
                            data[3], LocalDate.parse(data[4]), Boolean.parseBoolean(data[5]));
                    expenses.add(e);
                }
            }
        } catch (Exception ex) {
            System.out.println("Error loading file: " + ex.getMessage());
        }
        return expenses;
    }
}
