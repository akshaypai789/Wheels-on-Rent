package p1;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;

public class CalendarDialog extends JDialog {

    private Date selectedDate = null;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final Calendar currentCalendar = Calendar.getInstance();

    public CalendarDialog(JFrame owner, Date initialDate) {
        super(owner, "Select Date", true); // Modal dialog

        // Initialize calendar to the initial date
        currentCalendar.setTime(initialDate);

        // Set the dialog to match the requested dark theme
        getContentPane().setBackground(new Color(30, 30, 30));
        setLayout(new BorderLayout(10, 10));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createCalendarGrid(), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setBackground(new Color(30, 30, 30));
        panel.setBorder(new EmptyBorder(10, 10, 5, 10));

        // Month and Year display
        JLabel monthLabel = new JLabel(
                new SimpleDateFormat("MMM yyyy").format(currentCalendar.getTime()),
                SwingConstants.CENTER
        );
        monthLabel.setForeground(Color.WHITE);
        monthLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Navigation buttons
        JButton prevButton = createNavButton(" < ");
        JButton nextButton = createNavButton(" > ");

        JPanel navPanel = new JPanel(new BorderLayout(15, 0));
        navPanel.setOpaque(false);
        navPanel.add(prevButton, BorderLayout.WEST);
        navPanel.add(nextButton, BorderLayout.EAST);

        panel.add(navPanel, BorderLayout.WEST);
        panel.add(monthLabel, BorderLayout.CENTER);

        return panel;
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(50, 50, 50));
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(new Color(70, 70, 70), 1));
        button.setOpaque(true);
        button.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Month navigation simulated!", "Info", JOptionPane.INFORMATION_MESSAGE);
        });
        return button;
    }

    private JPanel createCalendarGrid() {
        JPanel gridPanel = new JPanel(new GridLayout(0, 7, 5, 5));
        gridPanel.setBackground(new Color(30, 30, 30));
        gridPanel.setBorder(new EmptyBorder(0, 10, 10, 10));

        // Day Headers (Su to Sat)
        String[] days = {"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};
        for (String day : days) {
            JLabel label = new JLabel(day, SwingConstants.CENTER);
            label.setForeground(new Color(150, 150, 150));
            label.setFont(new Font("Arial", Font.BOLD, 12));
            gridPanel.add(label);
        }

        // --- Date Button Population ---

        Calendar tempCal = (Calendar) currentCalendar.clone();
        tempCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = tempCal.get(Calendar.DAY_OF_WEEK);

        // Add blank labels for padding
        for (int i = 1; i < firstDayOfWeek; i++) {
            gridPanel.add(new JLabel(""));
        }

        // Add day buttons
        int totalDays = currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Calendar today = Calendar.getInstance();

        for (int day = 1; day <= totalDays; day++) {
            JButton dayButton = new JButton(String.valueOf(day));
            dayButton.setFocusPainted(false);
            dayButton.setBorder(new EmptyBorder(5, 5, 5, 5));
            dayButton.setOpaque(true);

            dayButton.setForeground(Color.WHITE);
            dayButton.setBackground(new Color(50, 50, 50));

            // Highlight today's date
            boolean isToday = currentCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                    currentCalendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                    day == today.get(Calendar.DAY_OF_MONTH);

            if (isToday) {
                dayButton.setBackground(new Color(255, 140, 0));
            }

            // Highlight the initially selected date
            if (selectedDate != null) {
                Calendar initialCal = Calendar.getInstance();
                initialCal.setTime(selectedDate);
                if (currentCalendar.get(Calendar.YEAR) == initialCal.get(Calendar.YEAR) &&
                        currentCalendar.get(Calendar.MONTH) == initialCal.get(Calendar.MONTH) &&
                        day == initialCal.get(Calendar.DAY_OF_MONTH)) {

                    dayButton.setBorder(new LineBorder(Color.CYAN, 2));
                }
            }

            // *** FIX APPLIED HERE ***
            final int dayToSelect = day;
            dayButton.addActionListener(e -> selectDate(dayToSelect));

            gridPanel.add(dayButton);
        }

        return gridPanel;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(new Color(30, 30, 30));

        JButton todayButton = new JButton("▼ Today");
        todayButton.setForeground(new Color(255, 140, 0));
        todayButton.setBackground(new Color(50, 50, 50));
        todayButton.setFocusPainted(false);
        todayButton.setOpaque(true);
        todayButton.addActionListener(e -> {
            Calendar today = Calendar.getInstance();
            currentCalendar.setTime(today.getTime());
            selectDate(today.get(Calendar.DAY_OF_MONTH));
        });

        JButton closeButton = new JButton("✕ Close");
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(new Color(50, 50, 50));
        closeButton.setFocusPainted(false);
        closeButton.setOpaque(true);
        closeButton.addActionListener(e -> dispose());

        panel.add(todayButton);
        panel.add(closeButton);
        return panel;
    }

    private void selectDate(int day) {
        currentCalendar.set(Calendar.DAY_OF_MONTH, day);
        this.selectedDate = currentCalendar.getTime();
        dispose();
    }

    public Date getSelectedDate() {
        return selectedDate;
    }
}