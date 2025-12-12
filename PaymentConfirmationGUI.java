package p1;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class PaymentConfirmationGUI extends JFrame {

    private final String vehicleModel;
    private final int totalPrice;
    private final String pickupDateTime;
    private final String dropoffDateTime;
    private final String username;

    // Use the same IMAGE_MAP definitions from VehicleDisplayGUI for consistency
    private static final Map<String, String> IMAGE_MAP = new LinkedHashMap<>() {{
        // Sedan Cars
        put("City", "city.jpg"); put("Amaze", "amaze.jpg"); put("Verna", "verna.jpg");
        put("Vento", "vento.jpg"); put("Virtus", "virtus.jpg");

        // SUV Cars
        put("Mahindra XUV700", "xuv700.jpg"); put("Tata Nexon", "nexon.jpg");
        put("Hyundai Creta", "creta.jpg"); put("Mahindra Scorpio", "scorpio.jpg");
        put("Kia Sonet", "sonet.jpg");

        // Hatchback Cars
        put("Swift", "swift.png"); put("Baleno", "baleno.png");
        put("Wagon R", "wagon_r.png"); put("Tata Punch", "tata_punch.jpeg");
        put("Ignis", "ignis.jpeg");

        // Luxury Cars
        put("Defender", "defender.jpeg"); put("Range Rover", "range_rover.jpeg");
        put("BMW X5", "bmw_x5.jpeg"); put("Audi A6", "audi_a6.jpeg");
        put("Mercedes C-Class", "mercedes_c.jpeg");

        // Bikes
        put("Royal Enfield Classic 350", "classic_350.jpeg"); put("Bajaj Pulsar 150", "pulsar_150.jpeg");
        put("Honda CB Shine", "cb_shine.jpeg"); put("TVS Apache RTR 160", "apache_160.jpeg");

        // Scooters
        put("Honda Activa 6G", "activa_6g.jpeg"); put("TVS Jupiter", "jupiter.jpeg");
        put("Suzuki Access 125", "access_125.jpeg"); put("Yamaha Fascino 125", "fascino_125.jpeg");
    }};

    /**
     * Constructor for the final payment confirmation screen.
     */
    public PaymentConfirmationGUI(String username, String vehicleModel, int totalPrice, String pickupDateTime, String dropoffDateTime) {
        this.username = username;
        this.vehicleModel = vehicleModel;
        this.totalPrice = totalPrice;
        this.pickupDateTime = pickupDateTime;
        this.dropoffDateTime = dropoffDateTime;

        setTitle("Final Booking & Payment Review");
        setSize(600, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close this window, don't exit app

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(250, 250, 250));

        // --- Header (Welcome) ---
        JLabel welcomeLabel = new JLabel("Booking Summary for " + username, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Times New Roman", Font.BOLD, 22));
        welcomeLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // --- Center Panel: Image and Details ---
        JPanel centerPanel = new JPanel(new BorderLayout(20, 0));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));

        // 1. Vehicle Image (Left)
        JLabel imageLabel = new JLabel(loadVehicleImage(vehicleModel, 250, 180), SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(280, 200));
        imageLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        centerPanel.add(imageLabel, BorderLayout.WEST);

        // 2. Booking Details (Right)
        JPanel detailsPanel = createDetailsPanel();
        centerPanel.add(detailsPanel, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // --- Footer: Payment Button and T&C ---
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createDetailsPanel() {
        JPanel detailsPanel = new JPanel(new GridLayout(5, 1, 0, 10));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(new EmptyBorder(20, 10, 20, 20));

        JLabel modelLabel = new JLabel("<html><b style='font-size:10px;'>Vehicle:</b> " + vehicleModel + "</html>");
        modelLabel.setForeground(new Color(0, 51, 153));

        JLabel pickupLabel = new JLabel("<html><b>Pickup:</b> " + pickupDateTime + "</html>");
        JLabel dropoffLabel = new JLabel("<html><b>Drop-off:</b> " + dropoffDateTime + "</html>");

        JLabel priceLabel = new JLabel("<html><b style='font-size:20px; color:#008000;'>Total Amount: ₹" + totalPrice + "</b></html>");

        detailsPanel.add(modelLabel);
        detailsPanel.add(pickupLabel);
        detailsPanel.add(dropoffLabel);
        detailsPanel.add(new JSeparator());
        detailsPanel.add(priceLabel);

        return detailsPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout(0, 15));
        footerPanel.setBackground(new Color(250, 250, 250));

        // --- Proceed to Payment Button ---
        JButton paymentButton = new JButton("Proceed to Payment (UPI)");
        paymentButton.setBackground(Color.BLACK);
        paymentButton.setForeground(Color.red);
        paymentButton.setFont(new Font("Arial", Font.BOLD, 18));
        paymentButton.setPreferredSize(new Dimension(getWidth(), 50));

        // **MODIFIED LOGIC: Open the simulated UPI QR Code dialog and then the FinalConfirmationGUI**
        paymentButton.addActionListener(e -> {
            this.setVisible(false); // Hide the review screen

            // 1. Create the simulated UPI Payment Dialog
            JDialog qrDialog = new JDialog(this, "Scan to Pay ", true); // Modal dialog
            qrDialog.setSize(400, 500);
            qrDialog.setLayout(new BorderLayout(10, 10));
            qrDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            // --- Header ---
            JLabel instructionLabel = new JLabel("Scan UPI QR Code to Pay ₹" + totalPrice, SwingConstants.CENTER);
            instructionLabel.setFont(new Font("Arial", Font.BOLD, 18));
            qrDialog.add(instructionLabel, BorderLayout.NORTH);

            // --- UPI ID Label ---
            JLabel upiIdLabel = new JLabel("UPI ID: project.simulated@upi", SwingConstants.CENTER);
            upiIdLabel.setFont(new Font("Arial", Font.ITALIC, 14));

            // --- QR Code Image (Center) ---
            // NOTE: Requires 'upi_qr_code.png' file to be present in resources
            ImageIcon qrIcon = loadQrCodeImage(300, 300);
            JLabel qrLabel = new JLabel(qrIcon, SwingConstants.CENTER);

            // Panel to hold QR and UPI ID
            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            centerPanel.add(qrLabel, BorderLayout.CENTER);
            centerPanel.add(upiIdLabel, BorderLayout.SOUTH);
            qrDialog.add(centerPanel, BorderLayout.CENTER);


            // --- Footer Button for Simulation ---
            JButton successButton = new JButton("Simulate Successful Payment");
            successButton.setBackground(new Color(0, 128, 0)); // Green
            successButton.setForeground(Color.black);
            successButton.setFont(new Font("Arial", Font.BOLD, 18));
            successButton.addActionListener(event -> {
                qrDialog.dispose();

                // Show confirmation message
                JOptionPane.showMessageDialog(null,
                        "Payment of ₹" + totalPrice + " confirmed. Your receipt is ready!",
                        "Payment Complete",
                        JOptionPane.INFORMATION_MESSAGE);

                // Launch the final confirmation screen
                new FinalConfirmationGUI(
                        username,
                        vehicleModel,
                        totalPrice,
                        pickupDateTime,
                        dropoffDateTime
                );

                // Close the PaymentConfirmationGUI window
                dispose();
            });

            JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonWrapper.setBorder(new EmptyBorder(0, 0, 10, 0));
            buttonWrapper.add(successButton);
            qrDialog.add(buttonWrapper, BorderLayout.SOUTH);

            qrDialog.setLocationRelativeTo(this);
            qrDialog.setVisible(true);

            // If the user closes the dialog manually, bring the payment review screen back
            if (!qrDialog.isVisible()) {
                this.setVisible(true);
            }
        });

        footerPanel.add(paymentButton, BorderLayout.NORTH);

        // --- Terms & Conditions Disclaimer ---
        JTextArea termsArea = new JTextArea();
        termsArea.setText(
                "An immediate cancellation can lead to a penalty of up to 100%. " +
                        "Carefully check & review your booking before proceeding.\n\n" +
                        "Terms & Conditions"
        );
        termsArea.setFont(new Font("Arial", Font.PLAIN, 11));
        termsArea.setForeground(Color.RED.darker());
        termsArea.setBackground(new Color(255, 240, 240)); // Very light red background
        termsArea.setLineWrap(true);
        termsArea.setWrapStyleWord(true);
        termsArea.setEditable(false);
        termsArea.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.RED, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        footerPanel.add(termsArea, BorderLayout.CENTER);

        return footerPanel;
    }


    // --- Helper Methods ---

    /**
     * Attempts to load a scaled vehicle image icon from the application's classpath resources.
     */
    private ImageIcon loadVehicleImage(String modelName, int width, int height) {
        String fileName = IMAGE_MAP.getOrDefault(modelName, "placeholder.png");

        URL url = null;
        Image image = null;

        try {
            url = getClass().getResource(fileName);
            if (url == null) {
                url = getClass().getResource("/" + fileName);
            }

            if (url != null) {
                image = ImageIO.read(url.openStream());

                if (image != null) {
                    Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaledImage);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading image for final confirmation (" + fileName + "): " + e.getMessage());
        }

        // Return a generic fallback placeholder with text
        return getPlaceholderIcon(width, height, "Image N/A");
    }

    /**
     * Attempts to load a scaled QR code image icon from the application's classpath resources.
     * Assumes the image file is named 'upi_qr_code.png'.
     */
    private ImageIcon loadQrCodeImage(int width, int height) {
        String fileName = "upi_qr_code.png"; // Simulated QR code image file

        URL url = null;
        Image image = null;

        try {
            url = getClass().getResource(fileName);
            if (url == null) {
                url = getClass().getResource("/" + fileName);
            }

            if (url != null) {
                image = ImageIO.read(url.openStream());

                if (image != null) {
                    Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaledImage);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading QR code image: " + e.getMessage());
        }

        // Fallback: If image fails to load, use a generic placeholder
        return getPlaceholderIcon(width, height, "UPI QR Code");
    }

    // Placeholder icon utility
    private ImageIcon getPlaceholderIcon(int width, int height, String text) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(220, 220, 220));
        g.fillRect(0, 0, width, height);
        g.setColor(new Color(100, 100, 100));
        g.drawRect(0, 0, width - 1, height - 1);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        FontMetrics fm = g.getFontMetrics();

        String[] lines = text.split(" ");
        int lineHeight = fm.getHeight();
        int totalHeight = lineHeight * lines.length;
        int startY = (height - totalHeight) / 2 + fm.getAscent();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int x = (width - fm.stringWidth(line)) / 2;
            g.drawString(line, x, startY + (i * lineHeight));
        }

        g.dispose();
        return new ImageIcon(bufferedImage);
    }
}