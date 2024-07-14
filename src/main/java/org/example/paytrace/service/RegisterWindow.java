package org.example.paytrace.service;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RegisterWindow extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private LoginWindow loginWindow;

    public RegisterWindow(LoginWindow loginWindow) {
        this.loginWindow = loginWindow;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("PayTrace - Register");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Heading panel
        JPanel headingPanel = new JPanel(new BorderLayout());
        headingPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Logo on the left
        ImageIcon logoIcon = new ImageIcon("src/main/java/logo/PayTraceLogo.jpg");
        Image scaledImage = logoIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(scaledImage);
        JLabel logoLabel = new JLabel(logoIcon);
        headingPanel.add(logoLabel, BorderLayout.WEST);

        // "Register for PayTrace" label on the right
        JLabel headingLabel = new JLabel("Register for PayTrace");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headingLabel.setBorder(new EmptyBorder(0, 20, 0, 0)); // Add left padding
        headingPanel.add(headingLabel, BorderLayout.CENTER);

        add(headingPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Times New Roman", Font.BOLD, 14));
        mainPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        passwordField = new JPasswordField(15);
        mainPanel.add(passwordField, gbc);

        // Confirm Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Confirm Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        confirmPasswordField = new JPasswordField(15);
        mainPanel.add(confirmPasswordField, gbc);

        // Register button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton registerButton = createStyledButton("Register");
        registerButton.addActionListener(e -> attemptRegister());
        mainPanel.add(registerButton, gbc);

        add(mainPanel, BorderLayout.CENTER);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                loginWindow.setVisible(true);
            }
        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(59, 89, 182));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(120, 40));
        return button;
    }

    private void attemptRegister() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (username.isEmpty() || password.isEmpty() || username.trim().isEmpty() || username.charAt(0) == ' ') {
            JOptionPane.showMessageDialog(this,
                    "Username and password cannot be empty. Username cannot start with a space.",
                    "Registration Failed",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Passwords do not match.",
                    "Registration Failed",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (loginWindow.userExists(username)) {
            JOptionPane.showMessageDialog(this,
                    "Username already exists. Please choose a different one.",
                    "Registration Failed",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            loginWindow.addUser(username, password);
            JOptionPane.showMessageDialog(this,
                    "Registration successful. You can now login.",
                    "Registration Successful",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
            loginWindow.setVisible(true);
        }
    }
}