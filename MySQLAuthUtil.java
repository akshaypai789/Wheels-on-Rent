package p1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLAuthUtil {

    // IMPORTANT: REPLACE these with your actual database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/vehicle_rental";
    private static final String DB_USER = "root"; // Use your MySQL username
    private static final String DB_PASS = "Kichu@2006"; // Use your MySQL password

    // --- TEST USER CREDENTIALS ---
    private static final String TEST_EMAIL = "project@test.com";
    private static final String TEST_PASS = "secure123";
    private static final String TEST_NAME = "Adarsh";
    // -----------------------------

    public static Connection getConnection() throws SQLException {
        // Ensure the JDBC driver is loaded
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
            throw new SQLException("MySQL Driver not found.", e);
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    /**
     * Ensures the 'users' table exists in the database.
     * NOTE: The call to insertTestUser() has been removed from here.
     */
    public static void ensureUserTableExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                + "user_id INT AUTO_INCREMENT PRIMARY KEY,"
                + "name VARCHAR(100) NOT NULL,"
                + "email VARCHAR(100) NOT NULL UNIQUE,"
                + "password_hash VARCHAR(255) NOT NULL,"
                + "age INT,"
                + "license_number VARCHAR(50),"
                + "aadhar_number VARCHAR(50),"
                + "phone_number VARCHAR(20),"
                + "photo_path VARCHAR(255)"
                + ")";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Table 'users' ensured to exist with all KYC fields.");

        } catch (SQLException e) {
            System.err.println("Error ensuring 'users' table exists: " + e.getMessage());
        }
    }

    /**
     * Inserts a predefined test user into the database if no users currently exist with that email.
     * This method will ONLY be called when the LoginGUI is initialized.
     */
    public static void insertTestUser() {
        String countSQL = "SELECT COUNT(*) FROM users WHERE email = ?";
        String insertSQL = "INSERT INTO users (name, email, password_hash, age, license_number, aadhar_number, phone_number, photo_path) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement countStmt = conn.prepareStatement(countSQL)) {

            countStmt.setString(1, TEST_EMAIL);
            try (ResultSet rs = countStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    // User does not exist, proceed with insertion
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
                        insertStmt.setString(1, TEST_NAME);
                        insertStmt.setString(2, TEST_EMAIL);
                        insertStmt.setString(3, TEST_PASS);
                        insertStmt.setInt(4, 25); // Age
                        insertStmt.setString(5, "T00000000"); // License
                        insertStmt.setString(6, "111122223333"); // Aadhar
                        insertStmt.setString(7, "9999999999"); // Phone
                        insertStmt.setString(8, "test_photo.jpg"); // Photo Path

                        insertStmt.executeUpdate();
                        System.out.println("\n*** SUCCESS: Test User inserted for Login GUI ***");
                        System.out.println("LOGIN EMAIL: " + TEST_EMAIL + " | PASSWORD: " + TEST_PASS);
                        System.out.println("*****************\n");
                    }
                } else {
                    System.out.println("Test user already exists. Ready for login.");
                }
            }

        } catch (SQLException e) {
            if (e.getErrorCode() != 1062) {
                System.err.println("Error inserting test user: " + e.getMessage());
            }
        }
    }

    // registerUser method is unchanged...
    public static boolean registerUser(String name, String email, String password, int age,
                                       String license, String aadhar, String phone, String photoPath) throws SQLException {
        // ... (body unchanged)
        String sql = "INSERT INTO users (name, email, password_hash, age, license_number, aadhar_number, phone_number, photo_path) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setInt(4, age);
            pstmt.setString(5, license);
            pstmt.setString(6, aadhar);
            pstmt.setString(7, phone);
            pstmt.setString(8, photoPath);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                System.err.println("Registration failed: Email already in use.");
                return false;
            }
            throw e;
        }
    }

    // authenticateUser method is unchanged...
    public static String authenticateUser(String email, String password) throws SQLException {
        // ... (body unchanged)
        String sql = "SELECT name, password_hash FROM users WHERE email = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password_hash");
                    String userName = rs.getString("name");

                    if (storedPassword.equals(password)) {
                        return userName;
                    }
                }
                return null;
            }
        }
    }
}