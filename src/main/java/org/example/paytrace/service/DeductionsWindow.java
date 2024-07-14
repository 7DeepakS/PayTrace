package org.example.paytrace.service;

import com.toedter.calendar.JDateChooser;
import org.example.paytrace.model.Deduction;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeductionsWindow extends JFrame {

    private JTextArea deductionsTextArea;
    private JButton printButton;
    private JButton chartButton;
    private JButton sortButton;
    private List<Deduction> originalDeductions;
    private List<Deduction> currentDeductions;
    private double currentTotalAmount;
    private double currentRemainingAmount;
    private DeductionsChartBackend chartBackend;
    private DeductionsService deductionsService;

    private JDateChooser startDateChooser;
    private JDateChooser endDateChooser;

    public DeductionsWindow() {
        chartBackend = new DeductionsChartBackend();
        deductionsService = new DeductionsService();

        setTitle("Deductions History");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        datePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        startDateChooser = new JDateChooser();
        startDateChooser.setDateFormatString("yyyy-MM-dd");
        datePanel.add(new JLabel("Start Date:"));
        datePanel.add(startDateChooser);

        endDateChooser = new JDateChooser();
        endDateChooser.setDateFormatString("yyyy-MM-dd");
        datePanel.add(new JLabel("End Date:"));
        datePanel.add(endDateChooser);

        add(datePanel, BorderLayout.NORTH);

        deductionsTextArea = new JTextArea();
        deductionsTextArea.setEditable(false);
        deductionsTextArea.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(deductionsTextArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

        printButton = createStyledButton("Print", Color.GRAY, Color.WHITE);
        printButton.addActionListener(e -> printDeductions());
        buttonPanel.add(printButton);

        chartButton = createStyledButton("Show Chart", new Color(59, 89, 182), Color.WHITE);
        chartButton.addActionListener(e -> showChart());
        buttonPanel.add(chartButton);

        sortButton = createStyledButton("Sort", new Color(59, 89, 182), Color.WHITE);
        sortButton.addActionListener(e -> sortDeductionsByDate());
        buttonPanel.add(sortButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void displayDeductions(List<Deduction> deductions, double totalAmount, double remainingAmount) {
        displayDeductions(deductions, totalAmount, remainingAmount, null, null);
    }

    public void displayDeductions(List<Deduction> deductions, double totalAmount, double remainingAmount, LocalDate startDate, LocalDate endDate) {
        originalDeductions = new ArrayList<>(deductions);
        currentTotalAmount = totalAmount;
        currentRemainingAmount = remainingAmount;

        if (startDate != null && endDate != null) {
            currentDeductions = deductionsService.filterAndSortDeductionsByDate(originalDeductions, startDate, endDate);
        } else {
            currentDeductions = new ArrayList<>(originalDeductions);
        }

        updateDeductionsTextArea();
        setVisible(true);
    }

    private void updateDeductionsTextArea() {
        StringBuilder sb = new StringBuilder();
        sb.append("Total Amount: ₹").append(String.format("%.2f", currentTotalAmount)).append("\n");
        sb.append("Remaining Amount: ₹").append(String.format("%.2f", currentRemainingAmount)).append("\n\n");
        sb.append("Deductions History:\n");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Deduction deduction : currentDeductions) {
            String formattedDate = deduction.getDateTime().toLocalDate().format(formatter);
            sb.append("- Amount: ₹").append(String.format("%.2f", deduction.getAmount()))
                    .append(" | Reason: ").append(deduction.getReason())
                    .append(" | Date: ").append(formattedDate).append("\n");
        }

        deductionsTextArea.setText(sb.toString());
    }

    private void printDeductions() {
        PrintHelper.printDeductions(deductionsTextArea.getText());
    }

    private void showChart() {
        if (currentDeductions == null || currentDeductions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No deductions to display in the chart.", "No Data", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Create a default ChartConfig object (adjust this as necessary)
        ChartConfig chartConfig = new ChartConfig();
        // Configure chartConfig if needed, e.g., chartConfig.setSomeOption(value);

        JFreeChart chart = chartBackend.createChart(currentDeductions, chartConfig);
        JPanel chartPanel = new ChartPanel(chart);

        JFrame chartFrame = new JFrame("Deductions Chart");
        chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chartFrame.setLayout(new BorderLayout());
        chartFrame.add(chartPanel, BorderLayout.CENTER);
        chartFrame.setSize(800, 600);
        chartFrame.setLocationRelativeTo(this);
        chartFrame.setVisible(true);
    }

    private void sortDeductionsByDate() {
        if (originalDeductions != null && !originalDeductions.isEmpty()) {
            LocalDate startDate = null;
            LocalDate endDate = null;

            Date startDateValue = startDateChooser.getDate();
            Date endDateValue = endDateChooser.getDate();

            if (startDateValue != null) {
                startDate = startDateValue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }
            if (endDateValue != null) {
                endDate = endDateValue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }

            currentDeductions = deductionsService.filterAndSortDeductionsByDate(originalDeductions, startDate, endDate);
            updateDeductionsTextArea();
        }
    }

    private JButton createStyledButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 30));
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        return button;
    }
}
