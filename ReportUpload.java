package ETS;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Scanner;
public class ReportUpload {
	private static Scanner scanner = new Scanner(System.in);
    private Connection connection;

    public ReportUpload(Connection connection) {
        this.connection = connection;
    }

    public void generateReport(int userId, int categoryId, String reportType, String reportData) {
        String sql = "INSERT INTO report (user_id, category_id, report_type, report_data, generated_at) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, categoryId);
            preparedStatement.setString(3, reportType);
            preparedStatement.setString(4, reportData);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Report generated successfully.");
            } else {
                System.out.println("Failed to generate report.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayReports() {
        String sql = "SELECT * FROM report";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int reportId = resultSet.getInt("report_id");
                int userId = resultSet.getInt("user_id");
                int categoryId = resultSet.getInt("category_id");
                String reportType = resultSet.getString("report_type");
                String reportData = resultSet.getString("report_data");
                Timestamp generatedAt = resultSet.getTimestamp("generated_at");

                System.out.println("Report ID: " + reportId +
                        ", User ID: " + userId +
                        ", Category ID: " + categoryId +
                        ", Report Type: " + reportType +
                        ", Report Data: " + reportData +
                        ", Generated At: " + generatedAt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/expensetracker", "root", "root")) {
            ReportUpload reportUpload = new ReportUpload(connection);
            System.out.print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! EXPENSE REPORT PAGE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.print("Enter user ID: ");
            int userId = scanner.nextInt();
            System.out.print("Enter category ID: ");
            int categoryId = scanner.nextInt();
            System.out.print("Enter report type: ");
            String reportType = scanner.next();
            System.out.print("Enter report data: ");
            String reportData = scanner.next();

            reportUpload.generateReport(userId, categoryId, reportType, reportData);

            System.out.println("\nAll Reports:");
            reportUpload.displayReports();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            scanner.close(); 
        }
    }
}