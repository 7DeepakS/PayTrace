package org.example.paytrace.service;

import org.example.paytrace.model.Deduction;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DeductionsWindow extends JFrame {

    private JTextArea deductionsTextArea;
    private JButton printButton;
    private JButton chartButton;
    private List<Deduction> currentDeductions;
    private double currentTotalAmount;
    private double currentRemainingAmount;

    public DeductionsWindow() {
        setTitle("Deductions History");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen
        setLayout(new BorderLayout());

        deductionsTextArea = new JTextArea();
        deductionsTextArea.setEditable(false);
        deductionsTextArea.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font for text area

        JScrollPane scrollPane = new JScrollPane(deductionsTextArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add padding around the panel
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5)); // Center-align buttons with spacing

        printButton = createStyledButton("Print", Color.GRAY, Color.WHITE);
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printDeductions();
            }
        });
        buttonPanel.add(printButton);

        chartButton = createStyledButton("Show Chart", new Color(59, 89, 182), Color.WHITE);
        chartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showChart();
            }
        });
        buttonPanel.add(chartButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void displayDeductions(List<Deduction> deductions, double totalAmount, double remainingAmount) {
        currentDeductions = deductions;
        currentTotalAmount = totalAmount;
        currentRemainingAmount = remainingAmount;

        deductionsTextArea.setText("");
        deductionsTextArea.append("Total Amount: ₹" + String.format("%.2f", totalAmount) + "\n"); // Replace $ with ₹
        deductionsTextArea.append("Remaining Amount: ₹" + String.format("%.2f", remainingAmount) + "\n\n"); // Replace $ with ₹
        deductionsTextArea.append("Deductions History:\n");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (Deduction deduction : deductions) {
            String formattedDateTime = deduction.getDateTime().format(formatter);
            deductionsTextArea.append("- Amount: ₹" + deduction.getAmount() + " | Reason: " + deduction.getReason() + " | Date: " + formattedDateTime + "\n"); // Replace $ with ₹
        }

        setVisible(true);
    }

    private void printDeductions() {
        PrintHelper.printDeductions(deductionsTextArea.getText());
    }

    private void showChart() {
        if (currentDeductions == null || currentDeductions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No deductions to display in the chart.", "No Data", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        DeductionsChart deductionsChart = new DeductionsChart();
        JPanel chartPanel = deductionsChart.createChartPanel(currentDeductions);

        JFrame chartFrame = new JFrame("Deductions Chart");
        chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chartFrame.setLayout(new BorderLayout());
        chartFrame.add(chartPanel, BorderLayout.CENTER);
        chartFrame.setSize(800, 600);
        chartFrame.setLocationRelativeTo(this); // Center the chart frame relative to deductions window
        chartFrame.setVisible(true);
    }

    private JButton createStyledButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14)); // Set button font
        button.setPreferredSize(new Dimension(120, 30)); // Set button size
        return button;
    }
}
