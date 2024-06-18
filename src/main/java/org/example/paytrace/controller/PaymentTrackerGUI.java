package org.example.paytrace.controller;

import org.example.paytrace.model.Deduction;
import org.example.paytrace.service.DeductionsWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PaymentTrackerGUI extends JFrame {

    private JTextField totalAmountField;
    private JTextField deductionAmountField;
    private JTextField deductionReasonField;
    private JTextField dateField;
    private JTextField timeField;
    private JLabel remainingAmountLabel;
    private double totalAmount;
    private double remainingAmount;
    private List<Deduction> deductions;
    private DeductionsWindow deductionsWindow;

    public PaymentTrackerGUI() {
        deductions = new ArrayList<>();
        deductionsWindow = new DeductionsWindow();

        setTitle("Payment Tracker");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(8, 2, 10, 10)); // Adjusted grid layout with gaps

        inputPanel.add(new JLabel("Total Amount for the Month:"));
        totalAmountField = new JTextField();
        inputPanel.add(totalAmountField);

        inputPanel.add(new JLabel("Deduction Amount:"));
        deductionAmountField = new JTextField();
        inputPanel.add(deductionAmountField);

        inputPanel.add(new JLabel("Deduction Reason:"));
        deductionReasonField = new JTextField();
        inputPanel.add(deductionReasonField);

        inputPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        dateField = new JTextField();
        inputPanel.add(dateField);

        inputPanel.add(new JLabel("Time (HH:mm:ss):"));
        timeField = new JTextField();
        inputPanel.add(timeField);

        // Create styled buttons with improved appearance
        JButton addDeductionButton = createStyledButton("Add Deduction", new Color(59, 89, 182), Color.WHITE);
        addDeductionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDeduction();
            }
        });
        inputPanel.add(addDeductionButton);

        JButton showDeductionsButton = createStyledButton("Show Deductions", new Color(59, 89, 182), Color.WHITE);
        showDeductionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDeductions();
            }
        });
        inputPanel.add(showDeductionsButton);

        JButton resetButton = createStyledButton("Reset", new Color(183, 28, 28), Color.WHITE);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });
        inputPanel.add(resetButton);

        remainingAmountLabel = new JLabel("Remaining Amount: ₹0.00");
        remainingAmountLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Set font and style
        inputPanel.add(remainingAmountLabel);

        // Add padding around the input panel
        inputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        add(inputPanel, BorderLayout.NORTH);
    }

    private JButton createStyledButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 30)); // Set button size
        button.setFont(new Font("Arial", Font.PLAIN, 14)); // Set button font
        return button;
    }

    private void addDeduction() {
        try {
            if (totalAmountField.isEnabled()) {
                totalAmount = Double.parseDouble(totalAmountField.getText());
                remainingAmount = totalAmount;
                totalAmountField.setEnabled(false);
            }

            double deductionAmount = Double.parseDouble(deductionAmountField.getText());
            String reason = deductionReasonField.getText();
            String dateStr = dateField.getText();
            String timeStr = timeField.getText();

            LocalDateTime dateTime = parseDateTime(dateStr, timeStr);

            if (deductionAmount > remainingAmount) {
                JOptionPane.showMessageDialog(this, "Deduction amount exceeds the remaining amount. Please enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Deduction deduction = new Deduction(deductionAmount, reason, dateTime);
            deductions.add(deduction);
            remainingAmount -= deductionAmount;

            deductionAmountField.setText("");
            deductionReasonField.setText("");
            dateField.setText("");
            timeField.setText("");

            JOptionPane.showMessageDialog(this, "Deduction added successfully.");
            updateRemainingAmountLabel();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private LocalDateTime parseDateTime(String dateStr, String timeStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String dateTimeStr = dateStr + " " + timeStr;
            return LocalDateTime.parse(dateTimeStr, formatter);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid date or time format. Please use YYYY-MM-DD HH:mm:ss.", "Error", JOptionPane.ERROR_MESSAGE);
            throw e;
        }
    }

    private void showDeductions() {
        deductionsWindow.displayDeductions(deductions, totalAmount, remainingAmount);
    }

    private void updateRemainingAmountLabel() {
        remainingAmountLabel.setText("Remaining Amount: ₹" + String.format("%.2f", remainingAmount));
    }

    private void reset() {
        totalAmount = 0;
        remainingAmount = 0;
        deductions.clear();
        totalAmountField.setText("");
        totalAmountField.setEnabled(true);
        deductionAmountField.setText("");
        deductionReasonField.setText("");
        dateField.setText("");
        timeField.setText("");
        remainingAmountLabel.setText("Remaining Amount: ₹0.00");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Use Nimbus look and feel for modern appearance
                    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }

                PaymentTrackerGUI paymentTrackerGUI = new PaymentTrackerGUI();
                paymentTrackerGUI.setVisible(true);
            }
        });
    }
}
