# Vehicle Rental System 🚗🏍️🛵

This full-stack desktop application allows users to rent Cars, Bikes, and Scooters efficiently. It was developed as a mini-project for my college degree.

> Note: As this project was developed in parallel with my Java studies, vehicle images are currently hardcoded rather than fetched from the database to simplify the asset management process during learning.

## 📖 Overview
Built with Java Swing and MySQL, this system features a modern dark-mode UI, dynamic pricing logic, a comprehensive user authentication system (including KYC), and a simulated UPI payment gateway.

## 🚀 Features
- Vehicle Catalog: Browsable tabs categorized by vehicle type (Sedans, SUVs, Bikes, etc.).
- Dynamic Pricing: Automatically calculates total cost based on the specific vehicle model and rental duration.
- User Authentication: Secure Registration (collecting KYC details) and Login functionality.
- Payment Simulation: Generates a simulated UPI QR code and provides a final booking receipt.
- Database: Persistent data storage using MySQL for user and booking records.

## 🛠️ Tech Stack
- Language: Java (JDK 8+)
- GUI: Java Swing (JFrame, JPanel, JTabbedPane)
- Database: MySQL (JDBC Connectivity)

## ⚙️ Setup & Run

### 1. Database
- Install MySQL Server on your local machine.
- The application is designed to automatically create the database vehicle_rental and the necessary tables on the first run.
- ⚠️ Important: You must update the DB_PASS variable in both BookingAppGUI.java and MySQLAuthUtil.java to match your local MySQL root password.

### 2. Run
- Compile the project using your preferred IDE (IntelliJ, Eclipse) or command line.
- Run BookingAppGUI.java as the main class to launch the application.
