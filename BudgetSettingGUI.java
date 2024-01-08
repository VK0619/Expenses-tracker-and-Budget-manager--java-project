package ETS;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("serial")
public class BudgetSettingGUI extends JFrame {

    private JTextField userIdField;
    private JTextField categoryField;
    private JTextField monthlyLimitField;

    public BudgetSettingGUI(Connection connection) {
        setTitle("Budget Setting");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        userIdField = new JTextField();
        categoryField = new JTextField();
        monthlyLimitField = new JTextField();

        JButton setBudgetButton = new JButton("Set Budget");
        setBudgetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setBudget();
                monitorBudget();
            }
        });

        panel.add(new JLabel("User ID:"));
        panel.add(userIdField);
        panel.add(new JLabel("Category Name:"));
        panel.add(categoryField);
        panel.add(new JLabel("Monthly Limit:"));
        panel.add(monthlyLimitField);
        panel.add(new JLabel()); 
        panel.add(setBudgetButton);

        add(panel, BorderLayout.NORTH);

        JTextArea budgetMonitorArea = new JTextArea();
        budgetMonitorArea.setEditable(false);
        add(new JScrollPane(budgetMonitorArea), BorderLayout.CENTER);
    }

    private void setBudget() {
        try {
            int userId = Integer.parseInt(userIdField.getText());
            String categoryName = categoryField.getText();
            double monthlyLimit = Integer.parseInt(monthlyLimitField.getText());

            String insertSql = "INSERT INTO budget (user_id, category_name, monthly_limit) VALUES (?, ?, ?)";

            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/expensetracker", "root", "root");
                 PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {

                insertStatement.setInt(1, userId);
                insertStatement.setString(2, categoryName);
                insertStatement.setDouble(3, monthlyLimit);

                int rowsAffected = insertStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Budget set successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "No rows were affected. Budget may not be set.");
                }
            }
        } catch (NumberFormatException | SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error setting budget: " + ex.getMessage());
        }
    }

    @SuppressWarnings("null")
	private void monitorBudget() {
        try {
            int userId = Integer.parseInt(userIdField.getText());

            String sql = "SELECT b.category_id, b.monthly_limit, COALESCE(SUM(e.amount), 0) AS total_expense " +
                    "FROM budget b " +
                    "LEFT JOIN expenses e ON b.user_id = e.user_id AND b.category_id = e.category_id " +
                    "WHERE b.user_id = ? " +
                    "GROUP BY b.category_id, b.monthly_limit";

            StringBuilder displayText = new StringBuilder();

            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/expensetracker", "root", "root");
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int categoryId = resultSet.getInt("category_id");
                    int monthlyLimit = (int) resultSet.getInt("monthly_limit");
                   int totalExpense = resultSet.getInt("total_expense");
                    int remainingBudget = monthlyLimit - totalExpense;

                    displayText.append("Category ID: ").append(categoryId)
                            .append(", Monthly Limit: ").append(monthlyLimit)
                            .append(", Total Expense: ").append(totalExpense)
                            .append(", Remaining Budget: ").append(remainingBudget)
                            .append("\n");
                }

                JTextComponent budgetMonitorArea = null;
                budgetMonitorArea.setText(displayText.toString());
            }
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(this, "Error monitoring budget: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/expensetracker", "root", "root")) {
                new BudgetSettingGUI(connection).setVisible(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
