package p1;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.imageio.ImageIO;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

// Import the new confirmation screen
import p1.BookingConfirmationGUI;

public class VehicleDisplayGUI extends JFrame {

    private final String selectedType;
    private final String pickupDateTime;
    private final String dropoffDateTime;

    // FIX: Updated Formatter to match the incoming date string format (yyyy-MM-dd HH:mm)
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    // IMPORTANT: The image file names remain the same, assuming they are placed
    // where the Java runtime can find them (e.g., next to the compiled .class files).
    private static final Map<String, String> IMAGE_MAP = new LinkedHashMap<>() {{
        // Sedan Cars
        put("City", "city.jpg");
        put("Amaze", "amaze.jpg");
        put("Verna", "verna.jpg");
        put("Vento", "vento.jpg");
        put("Virtus", "virtus.jpg");

        // SUV Cars
        put("Mahindra XUV700", "xuv700.jpg");
        put("Tata Nexon", "nexon.jpg");
        put("Hyundai Creta", "creta.jpg");
        put("Mahindra Scorpio", "scorpio.jpg");
        put("Kia Sonet", "sonet.jpg");

        // Hatchback Cars
        put("Swift", "swift.png");
        put("Baleno", "baleno2.png"); // Assuming conversion to PNG
        put("Wagon R", "wagon_r.png"); // Assuming conversion to PNG
        put("Tata Punch", "tata_punch.jpeg");
        put("Ignis", "ignis.png");

        // Luxury Cars
        put("Defender", "defender.jpeg");
        put("Range Rover", "range_rover.jpeg");
        put("BMW X5", "bmw_x5.jpeg");
        put("Audi A6", "audi_a6.jpeg");
        put("Mercedes C-Class", "mercedes_c.jpeg");

        // Bikes
        put("Royal Enfield Classic 350", "classic_350.jpeg");
        put("Bajaj Pulsar 150", "pulsar_150.jpeg");
        put("Honda CB Shine", "cb_shine.jpeg");
        put("TVS Apache RTR 160", "apache_160.jpeg");

        // Scooters
        put("Honda Activa 6G", "activa_6g.jpeg");
        put("TVS Jupiter", "jupiter.jpeg");
        put("Suzuki Access 125", "access_125.jpeg");
        put("Yamaha Fascino 125", "fascino_125.jpeg");
    }};


    // --- Hardcoded Vehicle Data with Prices (These are now BASE Daily Prices) ---
    private static final Map<String, Integer> SEDAN_CARS = new LinkedHashMap<>() {{
        put("City", 3100); put("Amaze", 2800); put("Verna", 3200);
        put("Vento", 3000); put("Virtus", 3400);
    }};

    private static final Map<String, Integer> HATCHBACK_CARS = new LinkedHashMap<>() {{
        put("Swift", 1350); put("Baleno", 1570); put("Wagon R", 1350);
        put("Tata Punch", 1600); put("Ignis", 1290);
    }};

    private static final Map<String, Integer> SUV_CARS = new LinkedHashMap<>() {{
        put("Mahindra XUV700", 5500); put("Tata Nexon", 1799); put("Hyundai Creta", 2500);
        put("Mahindra Scorpio", 4000); put("Kia Sonet", 3299);
    }};

    private static final Map<String, Integer> LUXURY_CARS = new LinkedHashMap<>() {{
        put("Defender", 12000); put("Range Rover", 15000); put("BMW X5", 10000);
        put("Audi A6", 9500); put("Mercedes C-Class", 9000);
    }};

    private static final Map<String, Integer> BIKES = new LinkedHashMap<>() {{
        put("Royal Enfield Classic 350", 1200); put("Bajaj Pulsar 150", 800);
        put("Honda CB Shine", 600); put("TVS Apache RTR 160", 700);
    }};

    private static final Map<String, Integer> SCOOTERS = new LinkedHashMap<>() {{
        put("Honda Activa 6G", 400); put("TVS Jupiter", 350);
        put("Suzuki Access 125", 450); put("Yamaha Fascino 125", 500);
    }};
    // -----------------------------------------------------------------

    public VehicleDisplayGUI(String selectedType, String pickupDateTime, String dropoffDateTime) {
        this.selectedType = selectedType;
        this.pickupDateTime = pickupDateTime;
        this.dropoffDateTime = dropoffDateTime;

        setTitle("Vehicle Selection : " + selectedType);
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(245, 245, 255));

        // --- Header/Booking Info Panel ---
        JPanel infoPanel = new JPanel(new BorderLayout(20, 0));
        infoPanel.setBackground(new Color(200, 220, 255));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(100, 149, 237), 2),
                new EmptyBorder(10, 15, 10, 15)
        ));

        JLabel titleLabel = new JLabel("Choose Your " + selectedType + " Model", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        infoPanel.add(titleLabel, BorderLayout.WEST);

        JLabel datesLabel = new JLabel("<html><center><b>Pickup:</b> " + pickupDateTime + "<br><b>Drop-off:</b> " + dropoffDateTime + "</center></html>", SwingConstants.RIGHT);
        datesLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(datesLabel, BorderLayout.EAST);

        mainPanel.add(infoPanel, BorderLayout.NORTH);

        // --- Tabbed Pane for Vehicle Categories ---
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        tabbedPane.setForeground(new Color(0, 0, 128));

        // Determine which tabs to display based on the initial selection
        if (selectedType.equals("Car")) {
            tabbedPane.addTab("Sedan", createVehiclePanel(SEDAN_CARS));
            tabbedPane.addTab("Hatchback", createVehiclePanel(HATCHBACK_CARS));
            tabbedPane.addTab("SUV", createVehiclePanel(SUV_CARS));
            tabbedPane.addTab("Luxury", createVehiclePanel(LUXURY_CARS));
        } else if (selectedType.equals("Bike")) {
            tabbedPane.addTab("Bikes", createVehiclePanel(BIKES));
        } else if (selectedType.equals("Scooter")) {
            tabbedPane.addTab("Scooters", createVehiclePanel(SCOOTERS));
        }

        JScrollPane scrollPane = new JScrollPane(tabbedPane);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        setContentPane(mainPanel);
        setLocationRelativeTo(null);
    }

    // Creates a panel to hold vehicle cards for a specific category
    private JPanel createVehiclePanel(Map<String, Integer> vehicles) {
        // GridLayout for a fixed 2-column layout
        JPanel panel = new JPanel(new GridLayout(0, 2, 20, 20));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.white);

        for (Map.Entry<String, Integer> entry : vehicles.entrySet()) {
            panel.add(createVehicleCard(entry.getKey(), entry.getValue()));
        }

        // Use a wrapper to center the grid content
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setBackground(Color.WHITE);
        wrapper.add(panel);

        return wrapper;
    }

    // Creates an individual attractive card for a vehicle
    private JPanel createVehicleCard(String model, int baseDailyPrice) {
        JPanel card = new JPanel(new BorderLayout(10, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(192, 192, 192), 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(380, 120));

        // 1. Calculate final price based on duration (NEW LOGIC)
        long totalDays = calculateBookingDays();
        int finalPrice = baseDailyPrice * (int) totalDays;

        // 1. Image Placeholder (Left) - Uses the updated loadScaledIcon
        ImageIcon icon = loadScaledIcon(model, 100, 80);
        JLabel imageLabel = new JLabel(icon, SwingConstants.CENTER);
        imageLabel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));

        // 2. Details Panel (Center)
        JPanel detailPanel = new JPanel(new GridLayout(3, 1));
        detailPanel.setBackground(Color.white);

        JLabel modelLabel = new JLabel("<html><b>" + model + "</b></html>");
        modelLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Price Label updated to show total price and duration
        JLabel priceLabel = new JLabel("<html>Total: <span style='color:green; font-size:14px;'><b>₹" + finalPrice + "</b></span>"
                + " (Base: ₹" + baseDailyPrice + " x " + totalDays + " Day(s))</html>");
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        JButton selectButton = new JButton("Select & Finalize");
// --- MODIFIED COLORS ---
        selectButton.setBackground(Color.BLACK); // Set background to Black
        selectButton.setForeground(new Color(255, 165, 0)); // Set foreground (text) to Orange
// -----------------------
        selectButton.setFocusPainted(false);
        selectButton.setFont(new Font("Arial", Font.BOLD, 14));
        selectButton.addActionListener(e -> {
            // ... (rest of the action listener code)     // New logic: Open the BookingConfirmationGUI and pass finalPrice
            BookingConfirmationGUI confirmGUI = new BookingConfirmationGUI(
                    model,
                    finalPrice, // Pass the calculated finalPrice
                    pickupDateTime,
                    dropoffDateTime
            );
            confirmGUI.setVisible(true);
            this.dispose(); // Close the vehicle selection window
        });

        detailPanel.add(modelLabel);
        detailPanel.add(priceLabel);
        detailPanel.add(selectButton);

        card.add(imageLabel, BorderLayout.WEST);
        card.add(detailPanel, BorderLayout.CENTER);

        return card;
    }

    /**
     * Calculates the total number of booking days (full 24-hour periods).
     * If duration is <= 24 hours, returns 1. Otherwise, rounds up to the next full day.
     */
    private long calculateBookingDays() {
        try {
            // Updated to parse the format yyyy-MM-dd HH:mm as per the log output
            LocalDateTime pickup = LocalDateTime.parse(this.pickupDateTime, DATE_TIME_FORMATTER);
            LocalDateTime dropoff = LocalDateTime.parse(this.dropoffDateTime, DATE_TIME_FORMATTER);

            // Calculate total hours
            long totalHours = ChronoUnit.HOURS.between(pickup, dropoff);

            if (totalHours <= 0) {
                // If dropoff is before or same as pickup, treat as 1 day (minimum booking)
                return 1;
            }

            // Calculate days, rounding up: (totalHours + 23) / 24
            // This ensures 1 hour returns 1, 24 hours returns 1, 25 hours returns 2.
            long totalDays = (totalHours + 23) / 24;

            return totalDays;

        } catch (Exception e) {
            System.err.println("Error parsing date/time for price calculation. Check format: yyyy-MM-dd HH:mm. Error: " + e.getMessage());
            // Default to 1 day if parsing fails
            return 1;
        }
    }

    // Placeholder icon utility for missing images (generates an image object with text)
    private ImageIcon getPlaceholderIcon(int width, int height, String text) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();

        // Define color based on category for visual distinction
        Color color;
        if (SEDAN_CARS.containsKey(text)) color = new Color(173, 216, 230); // Light Blue (Sedan)
        else if (SUV_CARS.containsKey(text)) color = new Color(255, 165, 0); // Orange (SUV)
        else if (HATCHBACK_CARS.containsKey(text)) color = new Color(144, 238, 144); // Light Green (Hatchback)
        else if (LUXURY_CARS.containsKey(text)) color = new Color(211, 211, 211); // Light Gray (Luxury)
        else if (BIKES.containsKey(text)) color = new Color(255, 192, 203); // Pink (Bikes)
        else if (SCOOTERS.containsKey(text)) color = new Color(221, 160, 221); // Plum (Scooters)
        else color = new Color(240, 240, 240); // Default

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(color);
        g.fillRect(0, 0, width, height);
        g.setColor(new Color(100, 100, 100));
        g.drawRect(0, 0, width - 1, height - 1);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 10));
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

    /**
     * Attempts to load a scaled image icon from the application's classpath resources.
     * @param modelName The name of the vehicle model.
     * @param width The desired width.
     * @param height The desired height.
     * @return The scaled ImageIcon, or a placeholder if the file is not found or fails to load.
     */
    private ImageIcon loadScaledIcon(String modelName, int width, int height) {
        String fileName = IMAGE_MAP.get(modelName);

        if (fileName == null || fileName.isEmpty()) {
            return getPlaceholderIcon(width, height, modelName);
        }

        URL url = null;
        Image image = null;

        // 1. Attempt resource loading relative to the class package (e.g., /p1/city.jpg)
        url = getClass().getResource(fileName);

        if (url == null) {
            // 2. Fallback: Attempt resource loading from the classpath root (e.g., /city.jpg)
            url = getClass().getResource("/" + fileName);
        }

        if (url == null) {
            System.err.println("Error: Image resource not found for " + modelName + ". Looked for: " + fileName);
            // If the resource is not found, return the placeholder immediately
            return getPlaceholderIcon(width, height, modelName);
        }

        try {
            // Use ImageIO.read for more robust decoding via stream
            image = ImageIO.read(url.openStream());

            if (image == null) {
                // ImageIO.read returns null if format is not recognized or file is severely corrupted
                throw new Exception("Image format is unsupported or file is severely corrupted. Try a different format (e.g., PNG).");
            }

            // Scale the loaded image
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);

            // Log success for debugging
            // System.out.println("Loaded image successfully for " + modelName + " from URL: " + url);

            return new ImageIcon(scaledImage);

        } catch (Exception e) {
            // Log decoding errors and fall back to the placeholder
            System.err.println("Error decoding image for " + modelName + " (" + fileName + "): " + e.getMessage());
            return getPlaceholderIcon(width, height, modelName);
        }
    }
}
