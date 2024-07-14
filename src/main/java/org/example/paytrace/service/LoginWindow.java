package org.example.paytrace.service;

import org.example.paytrace.controller.PaymentTrackerGUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LoginWindow extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private Map<String, String> users;
    private final String USER_FILE = "users.txt";

    public LoginWindow() {
        loadUsers();
        initializeUI();
    }

    private void loadUsers() {
        users = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("User file not found. Creating a new one.");
        }
    }

    private void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeUI() {
        setTitle("PayTrace - Login");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        // "Welcome to PayTrace" label on the right
        JLabel headingLabel = new JLabel("Welcome to PayTrace");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headingLabel.setBorder(new EmptyBorder(0, 20, 0, 0)); // Add left padding
        headingPanel.add(headingLabel, BorderLayout.CENTER);

        add(headingPanel, BorderLayout.NORTH);

        // Main panel for login form
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel usernameLabel = new JLabel("Username:");
        mainPanel.add(usernameLabel, gbc);

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
        JLabel passwordLabel = new JLabel("Password:");
        mainPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        passwordField = new JPasswordField(15);
        mainPanel.add(passwordField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));

        // Login button
        JButton loginButton = createStyledButton("Login");
        loginButton.addActionListener(e -> attemptLogin());
        buttonPanel.add(loginButton);

        // Register button
        JButton registerButton = createStyledButton("Register");
        registerButton.addActionListener(e -> openRegisterWindow());
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);
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

    private void attemptLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (users.containsKey(username) && users.get(username).equals(password)) {
            openMainApplication(username);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password. Please try again.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openRegisterWindow() {
        SwingUtilities.invokeLater(() -> {
            RegisterWindow registerWindow = new RegisterWindow(this);
            registerWindow.setVisible(true);
            setVisible(false);
        });
    }

    private void openMainApplication(String username) {
        SwingUtilities.invokeLater(() -> {
            PaymentTrackerGUI paymentTrackerGUI = new PaymentTrackerGUI(username);
            paymentTrackerGUI.setVisible(true);
            dispose();
        });
    }

    public void addUser(String username, String password) {
        users.put(username, password);
        saveUsers();
    }

    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                e.printStackTrace();
            }
            LoginWindow loginWindow = new LoginWindow();
            loginWindow.setVisible(true);
        });
    }
}