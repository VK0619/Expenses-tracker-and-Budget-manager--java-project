package ETS;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
public class IncomeTracking {
	private static Scanner scanner = new Scanner(System.in);
    public static void addIncome(Connection connection, int userId, String date, double amount, String description) {
        String sql = "INSERT INTO income (user_id, date, amount, description) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, date);
            preparedStatement.setDouble(3, amount);
            preparedStatement.setString(4, description);
            preparedStatement.executeUpdate();
            System.out.println("Income added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void displayIncome(Connection connection, int userId) {
        String sql = "SELECT * FROM income WHERE user_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            var resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int incomeId = resultSet.getInt("income_id");
                String incomeDate = resultSet.getString("date");
                double amount = resultSet.getDouble("amount");
                String incomeDescription = resultSet.getString("description");
                System.out.println("Income ID: " + incomeId +
                        ", Date: " + incomeDate +
                        ", Amount: " + amount +
                        ", Description: " + incomeDescription);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/expensetracker", "root", "root")) {
        	System.out.print("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& INCOME TRACKING PAGE &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
            System.out.print("Enter user ID: ");
            int userId = scanner.nextInt();
            scanner.nextLine(); 
            System.out.print("Enter income date (YYYY-MM-DD): ");
            String date = scanner.nextLine();
            System.out.print("Enter income amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Enter income description: ");
            String description = scanner.nextLine();
            addIncome(connection, userId, date, amount, description);
            displayIncome(connection, userId);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}

