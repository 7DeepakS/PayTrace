package org.example.paytrace.controller;

import org.example.paytrace.service.LoginWindow;
import org.example.paytrace.service.PrintHelper;

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
        PrintHelper.setUsername(username);
    }

    private void initializeUI(String username) {
        setTitle("PayTrace");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Welcome panel with logo
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        welcomePanel.setBackground(new Color(151, 175, 214)); // Blue background

        // Add logo
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/logo/PayTraceLogo.png"));
        Image scaledImage = logoIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel logoLabel = new JLabel(scaledIcon);
        welcomePanel.add(logoLabel, BorderLayout.WEST);

        // Welcome message
        welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0)); // Add left padding
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);

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
        footerPanel.setBackground(new Color(119, 136, 171)); // Blue background

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 14));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(220, 50, 50)); // Red background
        logoutButton.setOpaque(true);
        logoutButton.setBorderPainted(false);
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
        // No need to update remaining amount in TotalAmountPanel
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
