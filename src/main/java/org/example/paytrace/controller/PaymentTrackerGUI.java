package org.example.paytrace.controller;

import org.example.paytrace.service.LoginWindow;

import javax.swing.*;
import java.awt.*;

public class PaymentTrackerGUI extends JFrame {

    private JLabel welcomeLabel;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private TotalAmountPanel totalAmountPanel;
    private ManageDeductionPanel manageDeductionPanel;

    public PaymentTrackerGUI(String username) {
        initializeUI(username);
    }

    private void initializeUI(String username) {
        setTitle("PayTrace");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Welcome panel
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomePanel.add(welcomeLabel);
        add(welcomePanel, BorderLayout.NORTH);

        // Main panel with card layout
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        totalAmountPanel = new TotalAmountPanel(this::onTotalAmountEntered);
        manageDeductionPanel = new ManageDeductionPanel(this::updateRemainingAmount);

        mainPanel.add(totalAmountPanel, "TotalAmount");
        mainPanel.add(manageDeductionPanel, "ManageDeduction");

        add(mainPanel, BorderLayout.CENTER);

        // Footer panel
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        footerPanel.add(logoutButton);
        add(footerPanel, BorderLayout.SOUTH);

        // Set initial view
        cardLayout.show(mainPanel, "TotalAmount");
    }

    private void onTotalAmountEntered(double totalAmount) {
        manageDeductionPanel.setTotalAmount(totalAmount);
        cardLayout.show(mainPanel, "ManageDeduction");
    }

    private void updateRemainingAmount(double remainingAmount) {
        totalAmountPanel.updateRemainingAmount(remainingAmount);
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            new LoginWindow().setVisible(true);
        }
    }
}