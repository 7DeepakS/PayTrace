package org.example.paytrace.controller;

import com.toedter.calendar.JDateChooser;
import org.example.paytrace.model.Deduction;
import org.example.paytrace.service.DeductionsWindow;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ManageDeductionPanel extends JPanel {

    private JTextField deductionAmountField;
    private JTextField deductionReasonField;
    private JDateChooser deductionDateChooser;
    private JLabel remainingAmountLabel;
    private double totalAmount;
    private double remainingAmount;
    private List<Deduction> deductions;
    private DeductionsWindow deductionsWindow;
    private RemainingAmountListener remainingAmountListener;

    public ManageDeductionPanel(RemainingAmountListener listener) {
        this.remainingAmountListener = listener;
        this.deductions = new ArrayList<>();
        this.deductionsWindow = new DeductionsWindow();

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = createInputPanel();
        add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        inputPanel.add(new JLabel("Deduction Amount:"), gbc);
        gbc.gridx++;
        deductionAmountField = new JTextField(15);
        inputPanel.add(deductionAmountField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Deduction Reason:"), gbc);
        gbc.gridx++;
        deductionReasonField = new JTextField(15);
        inputPanel.add(deductionReasonField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Deduction Date:"), gbc);
        gbc.gridx++;
        deductionDateChooser = new JDateChooser();
        deductionDateChooser.setDateFormatString("yyyy-MM-dd");
        inputPanel.add(deductionDateChooser, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        remainingAmountLabel = new JLabel("Remaining Amount: ₹0.00");
        remainingAmountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(remainingAmountLabel, gbc);

        return inputPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addDeductionButton = new JButton("Add Deduction");
        addDeductionButton.addActionListener(e -> addDeduction());
        buttonPanel.add(addDeductionButton);

        JButton showDeductionsButton = new JButton("Show Deductions");
        showDeductionsButton.addActionListener(e -> showDeductions());
        buttonPanel.add(showDeductionsButton);

        return buttonPanel;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
        this.remainingAmount = totalAmount;
        updateRemainingAmountLabel();
    }

    private void addDeduction() {
        try {
            double deductionAmount = Double.parseDouble(deductionAmountField.getText());
            String reason = deductionReasonField.getText();
            Date date = deductionDateChooser.getDate();

            if (date == null) {
                JOptionPane.showMessageDialog(this, "Please select a date.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Add check for 0 amount deduction
            if (deductionAmount == 0) {
                JOptionPane.showMessageDialog(this, "Deduction amount cannot be zero.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());

            if (deductionAmount > remainingAmount) {
                JOptionPane.showMessageDialog(this, "Deduction amount exceeds the remaining amount. Please enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Deduction deduction = new Deduction(deductionAmount, reason, dateTime);
            deductions.add(deduction);
            remainingAmount -= deductionAmount;

            deductionAmountField.setText("");
            deductionReasonField.setText("");
            deductionDateChooser.setDate(null);

            JOptionPane.showMessageDialog(this, "Deduction added successfully.");
            updateRemainingAmountLabel();
            remainingAmountListener.onRemainingAmountChanged(remainingAmount);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showDeductions() {
        deductionsWindow.displayDeductions(deductions, totalAmount, remainingAmount);
    }

    private void updateRemainingAmountLabel() {
        remainingAmountLabel.setText("Remaining Amount: ₹" + String.format("%.2f", remainingAmount));
    }

    public interface RemainingAmountListener {
        void onRemainingAmountChanged(double remainingAmount);
    }
}