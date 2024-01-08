package ETS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
public class ExpenseManager {
    private static Scanner scanner = new Scanner(System.in);
    private Connection connection;
    public ExpenseManager(Connection connection) {
        this.connection = connection;
    }

    public void addExpense(int categoryId, String date, double amount, String description) {
        String sql = "INSERT INTO expense (category_id, date, amount, description) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, categoryId);
            preparedStatement.setString(2, date);
            preparedStatement.setDouble(3, amount);
            preparedStatement.setString(4, description);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Expense added successfully.");
            } else {
                System.out.println("Failed to add expense.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayExpenses() {
        String sql = "SELECT * FROM expense";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int expenseId = resultSet.getInt("expense_id");
                int categoryId = resultSet.getInt("category_id");
                String date = resultSet.getString("date");
                double amount = resultSet.getDouble("amount");
                String description = resultSet.getString("description");

                System.out.println("Expense ID: " + expenseId +
                        ", Category ID: " + categoryId +
                        ", Date: " + date +
                        ", Amount: " + amount +
                        ", Description: " + description);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/expensetracker", "root", "root")) {
            ExpenseManager expenseManager = new ExpenseManager(connection);

            System.out.print("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$  EXPENSES PAGE  $&&&&&&&&&&&&&&&&&&&&$$$$$$$$$$$$$$$$$");
            System.out.print("Enter category ID: ");
            int categoryId = scanner.nextInt();
            System.out.print("Enter date (YYYY-MM-DD): ");
            String date = scanner.next();
            System.out.print("Enter amount: ");
            double amount = scanner.nextDouble();
            System.out.print("Enter description: ");
            String description = scanner.next();

            expenseManager.addExpense(categoryId, date, amount, description);

            System.out.println("\nAll Expenses:");
            expenseManager.displayExpenses();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            scanner.close(); 
        }
    }
}
