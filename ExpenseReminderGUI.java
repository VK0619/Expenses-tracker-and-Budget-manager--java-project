package ETS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("serial")
public class ExpenseReminderGUI extends JFrame {

    private JTextField userIdField;
    private JTextField descriptionField;
    private JTextField dateField;
    private JTextField intervalDaysField;
    private JTextArea reminderTextArea;
    @SuppressWarnings("unused")
	private Connection connection;

    public ExpenseReminderGUI(Connection connection) {
        this.connection = connection;

        setTitle("Expense Reminder");
        setSize(600, 500); // Adjusted size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(20, 5));

        userIdField = new JTextField();
        descriptionField = new JTextField();
        dateField = new JTextField();
        intervalDaysField = new JTextField();
        reminderTextArea = new JTextArea();
        reminderTextArea.setEditable(false);

        JButton setReminderButton = new JButton("Set Reminder");
        setReminderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setReminder();
                displayReminder();
            }
        });

        panel.add(new JLabel("User ID:"));
        panel.add(userIdField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(dateField);
        panel.add(new JLabel("Interval Days:"));
        panel.add(intervalDaysField);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(setReminderButton);

        add(panel, BorderLayout.SOUTH);
        add(new JScrollPane(reminderTextArea), BorderLayout.CENTER);
    }

    private void setReminder() {
    	try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/expensetracker", "root", "root")){
            int userId = Integer.parseInt(userIdField.getText());
            String description = descriptionField.getText();
            String date = dateField.getText();
            int intervalDays = Integer.parseInt(intervalDaysField.getText());

            String sql = "INSERT INTO reminder(user_id, description, date, interval_days) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, description);
                preparedStatement.setString(3, date);
                preparedStatement.setInt(4, intervalDays);
                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Reminder set successfully.");
            }
        } catch (NumberFormatException | SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error setting reminder: " + ex.getMessage());
        }
    }

    private void displayReminder() {
    	try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/expensetracker", "root", "root")){
            int userId = Integer.parseInt(userIdField.getText());
            String sql = "SELECT * FROM reminder WHERE user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();
                StringBuilder displayText = new StringBuilder();
                while (resultSet.next()) {
                    String date = resultSet.getString("date");
                    String description = resultSet.getString("description");
                    int intervalDays = resultSet.getInt("interval_days");

                    displayText.append("Date: ").append(date)
                            .append(", Description: ").append(description)
                            .append(", Interval Days: ").append(intervalDays)
                            .append("\n");
                }
                reminderTextArea.setText(displayText.toString());
            }
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(this, "Error displaying reminders: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/expensetracker", "root", "root")) {
                new ExpenseReminderGUI(connection).setVisible(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
