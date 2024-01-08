package ETS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@SuppressWarnings("serial")
public class UserRegisterGUI extends JFrame {

    private JTextField usernameField;
    private JTextField descriptionField;
    private JPasswordField passwordField;

    public UserRegisterGUI() {
        setTitle("User Registration");
        setResizable(500,20);
        setSize(600,50);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(20,5));

        usernameField = new JTextField(10);
        descriptionField = new JTextField(5);
        passwordField = new JPasswordField(5);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel()); 
        panel.add(registerButton);
       

        add(panel);
        setVisible(true);
    }

    private void setResizable(int i, int j) {
	
		
	}

	private void registerUser() {
        String username = usernameField.getText();
        String description = descriptionField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/expensetracker", "root", "root")) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO users (username, description, password) VALUES (?, ?, ?)")) {

                preparedStatement.setString(1, username);
                preparedStatement.setString(2, description);
                preparedStatement.setString(3, password);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "User registered successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to register user.");
                }

            } catch (SQLException e) {
                // Handle exceptions more gracefully (log or show user-friendly message)
                e.printStackTrace();
            }
        } catch (SQLException e) {
            // Handle exceptions more gracefully (log or show user-friendly message)
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UserRegisterGUI();
            }
        });
    }
}
