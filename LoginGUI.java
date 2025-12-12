package p1;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class LoginGUI extends JFrame {

    private final String vehicleModel;
    private final int dailyRate;
    private final String pickupDateTime;
    private final String dropoffDateTime;
    private final JFrame parentFrame;

    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginGUI(JFrame parentFrame, String vehicleModel, int dailyRate, String pickupDateTime, String dropoffDateTime) {
        this.parentFrame = parentFrame;
        this.vehicleModel = vehicleModel;
        this.dailyRate = dailyRate;
        this.pickupDateTime = pickupDateTime;
        this.dropoffDateTime = dropoffDateTime;

        // *** FIX: Insert test user ONLY when the login screen opens ***
        MySQLAuthUtil.insertTestUser();

        setTitle("User Login");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(255, 255, 255));

        // --- Header ---
        JLabel headerLabel = new JLabel("Login to Finalize Booking", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        headerLabel.setForeground(new Color(204, 102, 0));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // --- Form Panel ---
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // --- Button Panel ---
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setLocationRelativeTo(parentFrame);
        parentFrame.setVisible(false); // Hide the confirmation screen
        setVisible(true);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 15));
        formPanel.setBackground(Color.white);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder("Enter Credentials"),
                new EmptyBorder(10, 10, 10, 10)
        ));

        emailField = new JTextField(15);
        passwordField = new JPasswordField(15);

        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);

        return formPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        JButton loginButton = new JButton("LOG IN");
        // UPDATED: Black background and Yellow text for the main action button
        loginButton.setBackground(Color.BLACK);
        loginButton.setForeground(Color.RED);
        loginButton.setFont(new Font("Arial", Font.BOLD, 18));
        loginButton.addActionListener(e -> attemptLogin());

        JButton backButton = new JButton("Cancel");
        backButton.setFont(new Font("Arial", Font.BOLD, 15));
        backButton.addActionListener(e -> returnToParent());

        buttonPanel.add(loginButton);
        buttonPanel.add(backButton);
        return buttonPanel;
    }

    private void returnToParent() {
        this.dispose();
        parentFrame.setVisible(true);
    }

    private void attemptLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email and Password are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Assumes MySQLAuthUtil is available
            String userName = MySQLAuthUtil.authenticateUser(email, password);

            if (userName != null) {
                JOptionPane.showMessageDialog(this,
                        "Login successful! Welcome back, " + userName + ". Proceeding to payment.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                new PaymentConfirmationGUI(
                        userName,
                        vehicleModel,
                        dailyRate,
                        pickupDateTime,
                        dropoffDateTime
                ).setVisible(true);

                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Login failed: Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            System.err.println("Database Error during Login: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "A database error occurred during login. Check console for details.", "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}