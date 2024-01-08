package ETS;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
public class ExpenseCategory {
	 private static Scanner scanner = new Scanner(System.in);
	    private Connection connection;
	    public ExpenseCategory(Connection connection) {
	        this.connection = connection;
	    }
	    public void registerCategory(String categoryName) {
	        String sql = "INSERT INTO category (category_name) VALUES (?)";
	        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	            preparedStatement.setString(1, categoryName);
	            preparedStatement.executeUpdate();
	            System.out.println("Category registered successfully.");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    public void displayCategories() {
	        String sql = "SELECT * FROM category";
	        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
	             ResultSet resultSet = preparedStatement.executeQuery()) {

	            while (resultSet.next()) {
	                int categoryId = resultSet.getInt("category_id");
	                String categoryName = resultSet.getString("category_name");
	                System.out.println("Category ID: " + categoryId + ", Category Name: " + categoryName);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/expensetracker", "root", "root")) {
        	ExpenseCategory expenseCategory = new ExpenseCategory(connection);
        	System.out.print("******************************* EXPENSE CATEGORY PAGE *********************************");
        	System.out.print("Enter a category name to register: ");
            String categoryName = scanner.next();
            expenseCategory.registerCategory(categoryName);
            System.out.println("\nCategories in the system:");
            expenseCategory.displayCategories();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}