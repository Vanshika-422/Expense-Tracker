import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class BudgetManager {
    private static final String FILE_NAME = "budgets.txt";
    private Map<String, Double> categoryBudgets;

    public BudgetManager() {
        categoryBudgets = new HashMap<>();
        loadBudgets();
    }

    private void loadBudgets() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            categoryBudgets.put("Food", 10000.0);
            categoryBudgets.put("Transport", 5000.0);
            categoryBudgets.put("Utilities", 3000.0);
            categoryBudgets.put("Entertainment", 5000.0);
            categoryBudgets.put("Shopping", 8000.0);
            categoryBudgets.put("Other", 4000.0);
            saveBudgets();
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    categoryBudgets.put(parts[0], Double.parseDouble(parts[1]));
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading budgets: " + e.getMessage());
        }
    }

    public void saveBudgets() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<String, Double> entry : categoryBudgets.entrySet()) {
                bw.write(entry.getKey() + "," + entry.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving budgets: " + e.getMessage());
        }
    }

    public Map<String, Double> getBudgets() {
        return categoryBudgets;
    }

    public void updateBudget(String category, double newLimit) {
        categoryBudgets.put(category, newLimit);
        saveBudgets();
    }
}

