package p1;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.io.File;

public class RegistrationGUI extends JFrame {

    private final String vehicleModel;
    private final int dailyRate; // NOTE: This variable holds the totalPrice from the booking
    private final String pickupDateTime;
    private final String dropoffDateTime;
    private final JFrame parentFrame;

    // Form Fields
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField ageField;
    private JTextField licenseField;
    private JTextField aadharField;
    private JTextField phoneField;

    // Photo upload components
    private JTextField photoPathField;
    private String selectedPhotoPath = "";

    public RegistrationGUI(JFrame parentFrame, String vehicleModel, int dailyRate, String pickupDateTime, String dropoffDateTime) {
        this.parentFrame = parentFrame;
        this.vehicleModel = vehicleModel;
        this.dailyRate = dailyRate;
        this.pickupDateTime = pickupDateTime;
        this.dropoffDateTime = dropoffDateTime;

        // Ensure the users table exists before allowing registration
        MySQLAuthUtil.ensureUserTableExists();

        setTitle("User Registration (");
        setSize(550, 650); // Increased size to fit more fields
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(250, 250, 255));

        // --- Header ---
        JLabel headerLabel = new JLabel("Register and Provide Documents ", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setForeground(new Color(0, 102, 204));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // --- Form Panel (Scrollable) ---
        JScrollPane scrollPane = new JScrollPane(createFormPanel());
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // --- Button Panel ---
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setLocationRelativeTo(parentFrame);
        parentFrame.setVisible(false); // Hide the confirmation screen
        setVisible(true);
    }

    private JPanel createFormPanel() {
        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setBackground(Color.WHITE);

        // Uses BoxLayout for flexible, vertical stacking
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Helper to create a labeled field group
        JPanel nameGroup = createFieldGroup("Full Name:", nameField = new JTextField(30));
        JPanel emailGroup = createFieldGroup("Email:", emailField = new JTextField(30));
        JPanel passwordGroup = createFieldGroup("Password:", passwordField = new JPasswordField(30));
        JPanel ageGroup = createFieldGroup("Age:", ageField = new JTextField(30));
        JPanel phoneGroup = createFieldGroup("Phone Number:", phoneField = new JTextField(30));

        // KYC fields
        JPanel licenseGroup = createFieldGroup("License Number:", licenseField = new JTextField(30));
        JPanel aadharGroup = createFieldGroup("Aadhaar Card No.:", aadharField = new JTextField(30));


        // Photo upload group
        JPanel photoGroup = createPhotoUploadGroup();

        formPanel.add(createSectionHeader("Personal Details"));
        formPanel.add(nameGroup);
        formPanel.add(emailGroup);
        formPanel.add(passwordGroup);
        formPanel.add(ageGroup);
        formPanel.add(phoneGroup);


        formPanel.add(createSectionHeader("KYC Documents"));
        formPanel.add(licenseGroup);
        formPanel.add(aadharGroup);
        formPanel.add(photoGroup);

        formWrapper.add(formPanel, BorderLayout.NORTH); // Align content to the top
        return formWrapper;
    }

    // Helper to create a section separator
    private JLabel createSectionHeader(String title) {
        JLabel header = new JLabel("<html><br><b>" + title + "</b></html>");
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setForeground(new Color(0, 102, 204).darker());
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        return header;
    }

    // Helper to create a label-field row
    private JPanel createFieldGroup(String labelText, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, field.getPreferredSize().height + 10));
        panel.setBorder(new EmptyBorder(5, 0, 5, 0));

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(150, label.getPreferredSize().height));

        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    private JPanel createPhotoUploadGroup() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        panel.setBorder(new EmptyBorder(10, 0, 10, 0));

        photoPathField = new JTextField("No file selected", 30);
        photoPathField.setEditable(false);

        JButton browseButton = new JButton("Upload Photo");
        browseButton.addActionListener(e -> selectPhotoFile());

        JLabel label = new JLabel("Photo Upload:");

        label.setPreferredSize(new Dimension(150, label.getPreferredSize().height));

        panel.add(label, BorderLayout.WEST);
        panel.add(photoPathField, BorderLayout.CENTER);
        panel.add(browseButton, BorderLayout.EAST);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    private void selectPhotoFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select User Photo (JPG/PNG)");

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedPhotoPath = selectedFile.getAbsolutePath();
            photoPathField.setText(selectedFile.getName());
        }
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(250, 250, 255));

        JButton registerButton = new JButton("REGISTER NOW");
        // UPDATED: Black background and Yellow text
        registerButton.setBackground(Color.BLACK);
        registerButton.setForeground(Color.RED);
        registerButton.setFont(new Font("Arial", Font.BOLD, 18));
        registerButton.setPreferredSize(new Dimension(150, 35));
        registerButton.addActionListener(e -> attemptRegistration());

        JButton backButton = new JButton("Cancel");
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setPreferredSize(new Dimension(100, 35));
        backButton.addActionListener(e -> returnToParent());

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        return buttonPanel;
    }

    private void returnToParent() {
        this.dispose();
        parentFrame.setVisible(true);
    }

    private void attemptRegistration() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String ageStr = ageField.getText().trim();
        String license = licenseField.getText().trim();
        String aadhar = aadharField.getText().trim();
        String phone = phoneField.getText().trim();
        String photoPath = selectedPhotoPath; // Get the path of the selected photo

        // Basic validation
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() ||
                ageStr.isEmpty() || license.isEmpty() || aadhar.isEmpty() || phone.isEmpty() || photoPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields (including photo upload) are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
            if (age < 18) {
                JOptionPane.showMessageDialog(this, "You must be 18 or older to register.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Age must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Assumes MySQLAuthUtil is available
            boolean success = MySQLAuthUtil.registerUser(name, email, password, age, license, aadhar, phone, photoPath);

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Registration successful! Welcome, " + name + ". You are now logged in. Proceeding to payment.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                // **FIXED CODE: Open PaymentConfirmationGUI upon successful registration**
                new PaymentConfirmationGUI(
                        name, // Pass the newly registered user's name
                        vehicleModel,
                        dailyRate, // dailyRate holds the calculated totalPrice
                        pickupDateTime,
                        dropoffDateTime
                ).setVisible(true);

                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed: This email is already registered.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            System.err.println("Database Error during Registration: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "A database error occurred during registration. Check console for details.", "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}