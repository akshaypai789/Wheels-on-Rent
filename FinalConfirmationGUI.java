package p1;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class FinalConfirmationGUI extends JFrame {

    private final String vehicleModel;
    private final int totalPrice;
    private final String pickupDateTime;
    private final String dropoffDateTime;
    private final String username;

    private static final String PICKUP_ADDRESS = "Wheels on rent, Shop No. 12, Saphalyam Complex, Near Palayam Market, Palayam, Thiruvananthapuram, Kerala 695034";
    private static final String SUPPORT_PHONE = "9899007561";

    public FinalConfirmationGUI(String username, String vehicleModel, int totalPrice, String pickupDateTime, String dropoffDateTime) {
        this.username = username;
        this.vehicleModel = vehicleModel;
        this.totalPrice = totalPrice;
        this.pickupDateTime = pickupDateTime;
        this.dropoffDateTime = dropoffDateTime;

        setTitle("Booking Finalized - Receipt");
        setSize(700, 650); // Slightly increased height for better spacing
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(new Color(248, 255, 248)); // Very light, clean background

        // --- Header ---
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        // --- Center: Details Panel ---
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(createDetailsPanel(), BorderLayout.CENTER);
        mainPanel.add(centerWrapper, BorderLayout.CENTER);

        // --- Footer: Thank You ---
        JLabel thankYouLabel = new JLabel("THANK YOU!", SwingConstants.CENTER);
        thankYouLabel.setFont(new Font("Arial", Font.BOLD, 48)); // Slightly smaller, bolder font
        thankYouLabel.setForeground(new Color(0, 51, 153));
        thankYouLabel.setBorder(new EmptyBorder(10, 0, 0, 0));
        mainPanel.add(thankYouLabel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel iconLabel = new JLabel("✔", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Arial", Font.BOLD, 36));
        iconLabel.setForeground(new Color(0, 150, 0));
        iconLabel.setBorder(new EmptyBorder(0, 0, 0, 15));

        JLabel header = new JLabel("CONGRATULATIONS, " + username.toUpperCase() + "! Booking Confirmed.", SwingConstants.LEFT);
        header.setFont(new Font("Arial", Font.BOLD, 22));
        header.setForeground(new Color(0, 100, 0));

        headerPanel.add(iconLabel, BorderLayout.WEST);
        headerPanel.add(header, BorderLayout.CENTER);
        return headerPanel;
    }

    private JPanel createDetailsPanel() {
        // Use GridBagLayout for flexible alignment and spacing
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(150, 200, 150), 2),
                new EmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // 1. Booking Details Section (Titled Border)
        JPanel bookingDetails = new JPanel(new GridLayout(4, 2, 10, 10));
        bookingDetails.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY, 1),
                "Booking Summary",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                new Color(0, 51, 153)));
        bookingDetails.setBackground(new Color(248, 255, 255)); // Light blue tint

        bookingDetails.add(new JLabel("Vehicle Model:"));
        bookingDetails.add(createValueLabel(vehicleModel, new Color(0, 51, 153)));
        bookingDetails.add(new JLabel("Pickup Time:"));
        bookingDetails.add(createValueLabel(pickupDateTime, Color.BLACK));
        bookingDetails.add(new JLabel("Drop-off Time:"));
        bookingDetails.add(createValueLabel(dropoffDateTime, Color.BLACK));
        bookingDetails.add(new JLabel("Total Amount Paid:"));
        bookingDetails.add(createValueLabel("₹" + totalPrice, new Color(0, 128, 0).darker()));

        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        panel.add(bookingDetails, gbc);

        // Add a separator space
        gbc.gridy = row++; gbc.insets = new Insets(15, 0, 15, 0); gbc.weighty = 0;
        panel.add(new JSeparator(SwingConstants.HORIZONTAL), gbc);

        // 2. Collection & Assistance Section

        // Pickup Point Label
        gbc.gridy = row++; gbc.insets = new Insets(0, 5, 5, 5); gbc.gridwidth = 2;
        JLabel collectionHeader = new JLabel("Collection & Assistance", SwingConstants.LEFT);
        collectionHeader.setFont(new Font("Arial", Font.BOLD, 16));
        collectionHeader.setForeground(new Color(100, 100, 100));
        panel.add(collectionHeader, gbc);

        // Pickup Address
        gbc.gridy = row++; gbc.gridwidth = 1; gbc.weightx = 0.3;
        panel.add(new JLabel("Pickup Point:"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        JTextArea addressArea = new JTextArea(PICKUP_ADDRESS);
        addressArea.setEditable(false);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        addressArea.setFont(new Font("Arial", Font.BOLD, 13));
        addressArea.setForeground(Color.DARK_GRAY);
        addressArea.setBackground(Color.WHITE);
        addressArea.setBorder(BorderFactory.createEmptyBorder());
        panel.add(addressArea, gbc);

        // Support Phone
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 1; gbc.weightx = 0.3;
        panel.add(new JLabel("Support Helpline:"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        JLabel supportLabel = createValueLabel(SUPPORT_PHONE + " (24/7 Roadside Assistance)", Color.RED.darker());
        supportLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(supportLabel, gbc);

        // Add vertical space at the bottom
        gbc.gridy = row++; gbc.gridx = 0; gbc.gridwidth = 2; gbc.weighty = 1.0;
        panel.add(Box.createVerticalGlue(), gbc);

        return panel;
    }

    // Helper function for consistent styling of value labels
    private JLabel createValueLabel(String text, Color color) {
        JLabel label = new JLabel("<html><b>" + text + "</b></html>");
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        label.setForeground(color);
        return label;
    }
}