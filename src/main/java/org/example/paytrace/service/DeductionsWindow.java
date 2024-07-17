package org.example.paytrace.service;

import com.toedter.calendar.JDateChooser;
import org.example.paytrace.model.Deduction;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
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
    private DeductionsChartService chartService;
    private DeductionsService deductionsService;

    private JDateChooser startDateChooser;
    private JDateChooser endDateChooser;

    public DeductionsWindow(String username, double totalAmount) {
        chartService = new DeductionsChartService();
        deductionsService = new DeductionsService();

        setTitle("Transaction History");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        datePanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        startDateChooser = new JDateChooser();
        startDateChooser.setDateFormatString("yyyy-MM-dd");
        datePanel.add(createLabeledPanel("Start Date:", startDateChooser));

        endDateChooser = new JDateChooser();
        endDateChooser.setDateFormatString("yyyy-MM-dd");
        datePanel.add(createLabeledPanel("End Date:", endDateChooser));

        add(datePanel, BorderLayout.NORTH);

        deductionsTextArea = new JTextArea();
        deductionsTextArea.setEditable(false);
        deductionsTextArea.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(deductionsTextArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        printButton = createStyledButton("Print", new Color(100, 100, 100), Color.WHITE);
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

        sortButton = createStyledButton("Sort", new Color(59, 89, 182), Color.WHITE);
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortDeductionsByDate();
            }
        });
        buttonPanel.add(sortButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createLabeledPanel(String labelText, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(label, BorderLayout.WEST);
        panel.add(component, BorderLayout.CENTER);
        return panel;
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
        sb.append("Balance: ₹").append(String.format("%.2f", currentRemainingAmount)).append("\n\n");
        sb.append("Transaction History:\n");

        for (Deduction deduction : currentDeductions) {
            sb.append("- Amount: ₹").append(String.format("%.2f", deduction.getAmount()))
                    .append(" | Reason: ").append(deduction.getReason())
                    .append(" | Date: ").append(deduction.getDateTime().toLocalDate()).append("\n");
        }

        deductionsTextArea.setText(sb.toString());
    }

    private void printDeductions() {
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

        PrintHelper.printDeductions(deductionsTextArea.getText(), startDate, endDate, currentDeductions, currentTotalAmount);
    }

    private void showChart() {
        if (currentDeductions == null || currentDeductions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No transaction to display in the chart.", "No Data", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        DeductionsChartService.ChartConfig chartConfig = new DeductionsChartService.ChartConfig()
                .setTitle("Transaction Chart")
                .setXAxisLabel("Date")
                .setYAxisLabel("Amount")
                .setBarColor(new Color(59, 89, 182))
                .setBackgroundColor(Color.WHITE)
                .setValueFormat("₹#,##0.00")
                .setDateFormat("yyyy-MM-dd")
                .setShowItemLabels(true)
                .setFontName("Arial")
                .setFontSize(12);

        JFreeChart chart = chartService.createChart(currentDeductions, chartConfig);

        DeductionsChartService.DeductionsChartGUI chartGUI = new DeductionsChartService.DeductionsChartGUI();
        chartGUI.displayChart(currentDeductions, chartConfig);
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
        button.setPreferredSize(new Dimension(120, 35));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}
