package org.example.paytrace.controller;

import javax.swing.*;
import java.awt.*;

public class TotalAmountPanel extends JPanel {

    private JTextField totalAmountField;
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
        inputPanel.setBackground(new Color(245, 245, 245)); // Light gray background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel promptLabel = new JLabel("Enter Total Amount:");
        promptLabel.setFont(new Font("Arial", Font.BOLD, 16));
        inputPanel.add(promptLabel, gbc);

        gbc.gridy++;
        totalAmountField = new JTextField(15);
        totalAmountField.setFont(new Font("Arial", Font.PLAIN, 16));
        inputPanel.add(totalAmountField, gbc);

        return inputPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(245, 245, 245)); // Light gray background

        JButton confirmButton = new JButton("Confirm Total Amount");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 16));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setBackground(new Color(50, 150, 50)); // Green background
        confirmButton.setOpaque(true);
        confirmButton.setBorderPainted(false);
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

    public interface TotalAmountEnteredListener {
        void onTotalAmountEntered(double totalAmount);
    }
}
