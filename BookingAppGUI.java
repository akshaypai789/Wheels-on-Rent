package p1;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class BookingAppGUI extends JFrame {
    private JComboBox<String> vehicleCombo;
    private JSpinner pickupDateSpinner, dropoffDateSpinner;
    private JSpinner pickupTimeSpinner, dropoffTimeSpinner;
    private JButton bookButton;
    private JLabel resultLabel;

    // Database constants - REPLACE 'your_password'
    private static final String DB_NAME = "vehicle_rental";
    private static final String URL = "jdbc:mysql://localhost:3306/" + DB_NAME + "?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "Kichu@2006"; // <--- CHANGE THIS

    public BookingAppGUI() {
        // 1. Initialize Database and Table structure first
        try {
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "JDBC Driver not found. Please check your classpath.", "Driver Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to connect to MySQL or create table. Check credentials/server.", "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            // Allow the GUI to start, but booking will fail unless fixed
        }

        setTitle("Wheels On Rent");
        setSize(500, 500); // Increased size for breathing room
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
        mainPanel.setBackground(new Color(255, 255, 255)); // Light green/blue background

        // --- Header ---
        JLabel header = new JLabel("Booking", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 26));
        header.setForeground(new Color(0, 0, 0));
        mainPanel.add(header, BorderLayout.NORTH);

        // --- Center Form Panel (Uses BoxLayout for clean vertical stacking) ---
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // --- Footer Button and Result ---
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setLocationRelativeTo(null);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new LineBorder(new Color(170, 200, 170), 1));

        // 1. Vehicle Type Panel
        panel.add(createVehicleTypePanel());

        // 2. Pickup Panel
        panel.add(createDateTimePanel("Pickup Details", new Color(0, 102, 204), true));

        // 3. Drop-off Panel
        panel.add(createDateTimePanel("Drop-off Details", new Color(204, 102, 0), false));

        return panel;
    }

    private JPanel createVehicleTypePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBackground(Color.WHITE);

        panel.add(createLabel("Select Vehicle Type:", new Color(50, 50, 50)));

        vehicleCombo = new JComboBox<>(new String[]{"Car", "Bike", "Scooter"});
        vehicleCombo.setBackground(Color.WHITE);
        vehicleCombo.setPreferredSize(new Dimension(150, 25));
        panel.add(vehicleCombo);

        return panel;
    }

    private JPanel createDateTimePanel(String title, Color borderColor, boolean isPickup) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBackground(Color.white);
        panel.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(borderColor, 2),
                title,
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16),
                borderColor
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 15, 5, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 1. Date Row
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panel.add(createLabel("Date (YYYY-MM-DD):", Color.BLACK), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        JSpinner dateSpinner = createDateSpinner("yyyy-MM-dd");
        if (isPickup) {
            pickupDateSpinner = dateSpinner;
        } else {
            dropoffDateSpinner = dateSpinner;
        }
        panel.add(dateSpinner, gbc);

        // 2. Time Row
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(createLabel("Time (HH:MM):", Color.BLACK), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        JSpinner timeSpinner = createTimeSpinner("HH:mm");
        if (isPickup) {
            pickupTimeSpinner = timeSpinner;
        } else {
            dropoffTimeSpinner = timeSpinner;
        }
        panel.add(timeSpinner, gbc);

        // Max width padding (to make the inner panel size consistent)
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(5, 0, 5, 0));
        wrapper.add(panel);
        return wrapper;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout(0, 10));
        footerPanel.setOpaque(false);

        // Book button
        bookButton = new JButton("Book Dates & Show Vehicles");
        bookButton.setBackground(new Color(255, 165, 0));
        bookButton.setForeground(Color.BLACK);
        bookButton.setFont(new Font("Arial", Font.BOLD, 18));
        bookButton.setPreferredSize(new Dimension(50, 40));
        bookButton.addActionListener(e -> bookVehicle());
        footerPanel.add(bookButton, BorderLayout.NORTH);

        // Result label
        resultLabel = new JLabel("Please enter booking details.", SwingConstants.CENTER);
        resultLabel.setFont(new Font("", Font.BOLD, 18));
        resultLabel.setForeground(Color.DARK_GRAY);
        footerPanel.add(resultLabel, BorderLayout.SOUTH);

        return footerPanel;
    }

    // --- Helper Components ---

    private JLabel createLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        return label;
    }

    private JSpinner createDateSpinner(String format) {
        // Use Calendar.HOUR instead of Calendar.HOUR_OF_DAY to ensure time components are zeroed out for date comparison
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        SpinnerDateModel model = new SpinnerDateModel(cal.getTime(), cal.getTime(), null, Calendar.DAY_OF_MONTH);
        JSpinner spinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, format);

        JFormattedTextField textField = (JFormattedTextField)editor.getTextField();
        textField.setHorizontalAlignment(JTextField.LEFT);
        textField.setBorder(new LineBorder(Color.LIGHT_GRAY));
        textField.setBackground(new Color(250, 250, 250)); // Light background for field

        spinner.setEditor(editor);
        return spinner;
    }

    private JSpinner createTimeSpinner(String format) {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.SECOND, 0);

        SpinnerDateModel model = new SpinnerDateModel(now.getTime(), null, null, Calendar.MINUTE);
        JSpinner spinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, format);

        JFormattedTextField textField = (JFormattedTextField)editor.getTextField();
        textField.setHorizontalAlignment(JTextField.LEFT);
        textField.setBorder(new LineBorder(Color.LIGHT_GRAY));
        textField.setBackground(new Color(250, 250, 250)); // Light background for field

        spinner.setEditor(editor);
        return spinner;
    }

    // --- Database Methods (unchanged) ---

    private void initializeDatabase() throws ClassNotFoundException, SQLException {
        // Ensure JDBC driver is loaded
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 1. Connect without specifying a database to run 'CREATE DATABASE'
        String genericUrl = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=UTC";
        try (Connection conn = DriverManager.getConnection(genericUrl, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            // Create the database if it doesn't exist
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            System.out.println("Database '" + DB_NAME + "' ensured to exist.");
        }

        // 2. Connect to the specific database to create the table
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            String createTableSQL = "CREATE TABLE IF NOT EXISTS bookings (" +
                    "booking_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "vehicle_type VARCHAR(50) NOT NULL, " +
                    "pickup_datetime DATETIME NOT NULL, " +
                    "dropoff_datetime DATETIME NOT NULL, " +
                    "selected_model VARCHAR(100)" +
                    ");";
            // Create the table if it doesn't exist
            stmt.executeUpdate(createTableSQL);
            System.out.println("Table 'bookings' ensured to exist.");
        }
    }


    private void bookVehicle() {
        String vehicleType = (String) vehicleCombo.getSelectedItem();

        // Date/Time combination logic
        Date pickupDateVal = (Date) pickupDateSpinner.getValue();
        Date pickupTimeVal = (Date) pickupTimeSpinner.getValue();
        Date dropoffDateVal = (Date) dropoffDateSpinner.getValue();
        Date dropoffTimeVal = (Date) dropoffTimeSpinner.getValue();

        Calendar calPickup = Calendar.getInstance();
        calPickup.setTime(pickupDateVal);
        Calendar calTimePickup = Calendar.getInstance();
        calTimePickup.setTime(pickupTimeVal);
        // Combine Date and Time components
        calPickup.set(Calendar.HOUR_OF_DAY, calTimePickup.get(Calendar.HOUR_OF_DAY));
        calPickup.set(Calendar.MINUTE, calTimePickup.get(Calendar.MINUTE));
        calPickup.set(Calendar.SECOND, 0);

        Calendar calDropoff = Calendar.getInstance();
        calDropoff.setTime(dropoffDateVal);
        Calendar calTimeDropoff = Calendar.getInstance();
        calTimeDropoff.setTime(dropoffTimeVal);
        // Combine Date and Time components
        calDropoff.set(Calendar.HOUR_OF_DAY, calTimeDropoff.get(Calendar.HOUR_OF_DAY));
        calDropoff.set(Calendar.MINUTE, calTimeDropoff.get(Calendar.MINUTE));
        calDropoff.set(Calendar.SECOND, 0);

        // Simple validation
        if (calPickup.getTime().after(calDropoff.getTime())) {
            resultLabel.setText("❌ Drop-off date/time must be after Pickup date/time!");
            resultLabel.setForeground(Color.RED);
            return;
        }

        Timestamp pickupTimestamp = new Timestamp(calPickup.getTimeInMillis());
        Timestamp dropoffTimestamp = new Timestamp(calDropoff.getTimeInMillis());

        // Format for display on the next screen (matching VehicleDisplayGUI's expected format)
        SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String displayPickup = displayFormat.format(pickupTimestamp);
        String displayDropoff = displayFormat.format(dropoffTimestamp);

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO bookings (vehicle_type, pickup_datetime, dropoff_datetime) VALUES (?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, vehicleType);
            stmt.setTimestamp(2, pickupTimestamp);
            stmt.setTimestamp(3, dropoffTimestamp);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                long bookingId = -1;
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        bookingId = generatedKeys.getLong(1);
                    }
                }

                resultLabel.setText("✅ Booking ID " + bookingId + " registered. Select your model below!");
                resultLabel.setForeground(new Color(0, 150, 0));

                // Transition to the vehicle display page
                this.setVisible(false);
                SwingUtilities.invokeLater(() -> new VehicleDisplayGUI(vehicleType, displayPickup, displayDropoff).setVisible(true));
                this.dispose();

            } else {
                resultLabel.setText("❌ Booking Failed: No rows affected.");
                resultLabel.setForeground(Color.RED);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            resultLabel.setText("❌ DB Error: Check connection and credentials.");
            resultLabel.setForeground(Color.RED);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Ignore
        }
        SwingUtilities.invokeLater(() -> new BookingAppGUI().setVisible(true));
    }
}