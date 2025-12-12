# Vehicle Rental System 🚗🏍️🛵

A full-stack desktop application built with **Java Swing** and **MySQL** that allows users to rent Cars, Bikes, and Scooters. The system features a modern dark-mode UI, dynamic pricing, user authentication (KYC), and a simulated UPI payment gateway.

## 🚀 Features
- **Vehicle Catalog:** Browsable tabs for Sedans, SUVs, Bikes, etc.
- **Dynamic Pricing:** Calculates cost based on vehicle type and duration.
- **User Auth:** Registration (with KYC) and Login.
- **Payment Simulation:** UPI QR code generation and receipt generation.
- **Database:** Persistent storage using MySQL.

## 🛠️ Tech Stack
- **Language:** Java (JDK 8+)
- **GUI:** Java Swing (JFrame, JPanel, JTabbedPane)
- **Database:** MySQL (JDBC Connectivity)

## ⚙️ Setup & Run
1. **Database:**
   - Install MySQL Server.
   - The app automatically creates the database `vehicle_rental` and tables on first run.
   - **Important:** Update the `DB_PASS` variable in `BookingAppGUI.java` and `MySQLAuthUtil.java` with your MySQL password.
2. **Run:**
   - Compile and run `BookingAppGUI.java` as the main class.

