package ETS;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
public class ReceiptManager {
	private static Scanner scanner = new Scanner(System.in);

    public static void addReceipt(Connection connection, int expenseId, String filePath) {
        String sql = "INSERT INTO receipt (expense_id, file_path) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, expenseId);
            preparedStatement.setString(2, filePath);
            preparedStatement.executeUpdate();
            System.out.println("Receipt added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void displayReceipt(Connection connection, int expenseId) {
        String sql = "SELECT * FROM receipt WHERE expense_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, expenseId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int receiptId = resultSet.getInt("receipt_id");
                String filePath = resultSet.getString("file_path");

                System.out.println("Receipt ID: " + receiptId +
                        ", Expense ID: " + expenseId +
                        ", File Path: " + filePath);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/expensetracker", "root", "root")) {
        	System.out.print("################################### EXPENSE RECEIPT PAGE ###########################################");
            System.out.print("Enter expense ID: ");
            int expenseId = scanner.nextInt();
            scanner.nextLine(); 
            System.out.println("Choose any of your system file path ");
            System.out.println("Enter file path: ");
            String filePath = scanner.nextLine();
            System.out.println("Receipt of the respected ID will be stored in the file!");
            

            addReceipt(connection, expenseId, filePath);
            displayReceipt(connection, expenseId);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}