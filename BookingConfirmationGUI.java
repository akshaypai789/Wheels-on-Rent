package p1;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class BookingConfirmationGUI extends JFrame {

    private final String vehicleModel;
    // Renamed dailyRate to totalPrice to reflect the calculated value
    private final int totalPrice;
    private final String pickupDateTime;
    private final String dropoffDateTime;

    /**
     * Constructor for the booking confirmation and authentication prompt screen.
     */
    public BookingConfirmationGUI(String vehicleModel, int totalPrice, String pickupDateTime, String dropoffDateTime) {
        this.vehicleModel = vehicleModel;
        this.totalPrice = totalPrice;
        this.pickupDateTime = pickupDateTime;
        this.dropoffDateTime = dropoffDateTime;

        setTitle("Step 2: Authentication Required");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        mainPanel.setBackground(new Color(240, 245, 255));

        // --- Header (Booking Summary) ---
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // --- Center Panel (Auth Options) ---
        JPanel authPanel = createAuthPanel();
        mainPanel.add(authPanel, BorderLayout.CENTER);

        // --- Footer (Information) ---
        JLabel footerLabel = new JLabel(
                "<html><p style='text-align:center;'>You must Register or Log In to proceed with payment and secure your booking.</p></html>",
                SwingConstants.CENTER
        );
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        footerLabel.setForeground(Color.GRAY);
        mainPanel.add(footerLabel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setLocationRelativeTo(null);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        headerPanel.setBackground(new Color(200, 220, 255));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(100, 149, 237), 2),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel modelLabel = new JLabel("Booking Confirmed: " + vehicleModel, SwingConstants.CENTER);
        modelLabel.setFont(new Font("Arial", Font.BOLD, 18));
        modelLabel.setForeground(new Color(0, 51, 153));

        JLabel datesLabel = new JLabel("<html><center>Pickup: <b>" + pickupDateTime + "</b> &nbsp;|&nbsp; Drop-off: <b>" + dropoffDateTime + "</b></center></html>", SwingConstants.CENTER);
        datesLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        JLabel priceLabel = new JLabel("Total Price: ₹" + totalPrice, SwingConstants.CENTER);
        priceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        priceLabel.setForeground(new Color(255, 100, 0));

        headerPanel.add(modelLabel);
        headerPanel.add(datesLabel);
        headerPanel.add(priceLabel);
        return headerPanel;
    }

    private JPanel createAuthPanel() {
        JPanel authPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        authPanel.setBackground(new Color(240, 245, 255));
        authPanel.setBorder(new EmptyBorder(30, 0, 30, 0));

        // --- Register Card ---
        // Subtitle changed to null/empty string to remove it
        authPanel.add(createAuthCard("REGISTER", "", new Color(46, 139, 87), new Color(209, 237, 226), "register_action"));

        // --- Log In Card ---
        // Subtitle changed to null/empty string to remove it
        authPanel.add(createAuthCard("LOG IN", "", new Color(70, 130, 180), new Color(210, 230, 240), "login_action"));

        return authPanel;
    }

    private JPanel createAuthCard(String title, String subtitle, Color mainColor, Color secondaryColor, String action) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(secondaryColor);
        card.setPreferredSize(new Dimension(150, 150));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(mainColor, 2, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        // Action button (placed NORTH)
        JButton actionButton = new JButton("GO");
        actionButton.setBackground(Color.BLACK);
        actionButton.setForeground(Color.RED);
        actionButton.setFont(new Font("Arial", Font.BOLD, 18));
        actionButton.addActionListener(e -> {
            if ("register_action".equals(action)) {
                new RegistrationGUI(this, vehicleModel, totalPrice, pickupDateTime, dropoffDateTime);
            } else if ("login_action".equals(action)) {
                new LoginGUI(this, vehicleModel, totalPrice, pickupDateTime, dropoffDateTime);
            }
        });

        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        buttonWrapper.setBackground(secondaryColor);
        buttonWrapper.add(actionButton);
        card.add(buttonWrapper, BorderLayout.NORTH);

        // Title (Big Letters - Placed CENTER)
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(mainColor.darker());
        card.add(titleLabel, BorderLayout.CENTER);

        // Subtitle (Placed SOUTH - Now conditional)
        if (subtitle != null && !subtitle.trim().isEmpty()) {
            JLabel subtitleLabel = new JLabel(subtitle, SwingConstants.CENTER);
            subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 11));
            subtitleLabel.setForeground(Color.DARK_GRAY);
            card.add(subtitleLabel, BorderLayout.SOUTH);
        } else {
            // Add vertical padding/spacer if no subtitle is present
            card.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.SOUTH);
        }

        return card;
    }
}