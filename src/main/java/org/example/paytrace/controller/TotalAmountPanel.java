package org.example.paytrace.controller;

import javax.swing.*;
import java.awt.*;

public class TotalAmountPanel extends JPanel {

    private JTextField totalAmountField;
    private JLabel remainingAmountLabel;
    private TotalAmountEnteredListener totalAmountEnteredListener;

    public TotalAmountPanel(TotalAmountEnteredListener listener) {
        this.totalAmountEnteredListener = listener;
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

        inputPanel.add(new JLabel("Enter Total Amount for the Month:"), gbc);

        gbc.gridy++;
        totalAmountField = new JTextField(15);
        inputPanel.add(totalAmountField, gbc);

        gbc.gridy++;
        remainingAmountLabel = new JLabel("Remaining Amount: ₹0.00");
        remainingAmountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(remainingAmountLabel, gbc);

        return inputPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton confirmButton = new JButton("Confirm Total Amount");
        confirmButton.addActionListener(e -> confirmTotalAmount());
        buttonPanel.add(confirmButton);

        return buttonPanel;
    }

    private void confirmTotalAmount() {
        try {
            double totalAmount = Double.parseDouble(totalAmountField.getText());
            totalAmountEnteredListener.onTotalAmountEntered(totalAmount);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric total amount.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateRemainingAmount(double remainingAmount) {
        remainingAmountLabel.setText("Remaining Amount: ₹" + String.format("%.2f", remainingAmount));
    }

    public interface TotalAmountEnteredListener {
        void onTotalAmountEntered(double totalAmount);
    }
}