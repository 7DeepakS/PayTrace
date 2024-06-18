package org.example.paytrace.service;

import org.example.paytrace.controller.PaymentTrackerGUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginWindow() {
        setTitle("Login");
        setSize(350, 200); // Adjusted size for better layout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 1, 0, 10)); // Vertical layout with spacing

        // Username panel
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        usernamePanel.add(new JLabel("Username:"));
        usernameField = new JTextField(15); // Set preferred width
        usernamePanel.add(usernameField);
        inputPanel.add(usernamePanel);

        // Password panel
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        passwordPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(15); // Set preferred width
        passwordPanel.add(passwordField);
        inputPanel.add(passwordPanel);

        // Login button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(59, 89, 182)); // Custom background color
        loginButton.setForeground(Color.WHITE); // Text color
        loginButton.setFocusPainted(false); // Remove focus border
        loginButton.setPreferredSize(new Dimension(100, 30)); // Button size
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Hardcoded validation (replace with your logic)
                if ("admin".equals(username) && "admin".equals(password)) {
                    openMainApplication();
                } else {
                    JOptionPane.showMessageDialog(LoginWindow.this,
                            "Invalid username or password. Please try again.",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(loginButton);
        inputPanel.add(buttonPanel);

        // Add padding around the input panel
        inputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        add(inputPanel, BorderLayout.CENTER);
    }

    private void openMainApplication() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PaymentTrackerGUI paymentTrackerGUI = new PaymentTrackerGUI();
                paymentTrackerGUI.setVisible(true);
                dispose(); // Close the login window after successful login
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Use the UIManager to set look and feel to Nimbus for a more modern appearance
                    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }

                LoginWindow loginWindow = new LoginWindow();
                loginWindow.setVisible(true);
            }
        });
    }
}
