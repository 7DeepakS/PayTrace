package org.example.paytrace.service;

import org.example.paytrace.model.Deduction;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrintHelper {

    private static final Logger LOGGER = Logger.getLogger(PrintHelper.class.getName());
    private static String username = null;
    private static final String LOGO_PATH = "/logo/PayTraceLogo.png";

    public static void setUsername(String newUsername) {
        if (newUsername != null && !newUsername.trim().isEmpty()) {
            username = newUsername;
            LOGGER.info("Username set to: " + username);
        } else {
            LOGGER.warning("Attempted to set null or empty username.");
        }
    }

    public static void printDeductions(String textToPrint, LocalDate startDate, LocalDate endDate, List<Deduction> currentDeductions, double totalAmount) {
        if (username == null || username.trim().isEmpty()) {
            LOGGER.warning("Username not set. Please log in before printing.");
            JOptionPane.showMessageDialog(null, "Please log in before printing.", "Login Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LOGGER.info("Printing deductions for user: " + username);
        try {
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            printerJob.setJobName("Transaction History");

            if (printerJob.printDialog()) {
                printerJob.setPrintable((graphics, pageFormat, pageIndex) ->
                        printPage(graphics, pageFormat, pageIndex, textToPrint, startDate, endDate, currentDeductions, totalAmount));
                printerJob.print();
            }
        } catch (PrinterException ex) {
            LOGGER.log(Level.SEVERE, "Error printing transaction", ex);
            JOptionPane.showMessageDialog(null, "Error printing: " + ex.getMessage(), "Print Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static int printPage(Graphics graphics, PageFormat pageFormat, int pageIndex, String textToPrint, LocalDate startDate, LocalDate endDate, List<Deduction> currentDeductions, double totalAmount) throws PrinterException {
        if (pageIndex > 0) {
            return Printable.NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        // Load and draw logo
        BufferedImage logoImage = loadImage();
        if (logoImage != null) {
            Image scaledImage = logoImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            scaledIcon.paintIcon(null, g2d, 50, 50);
        } else {
            g2d.setFont(new Font("Arial", Font.ITALIC, 12));
            g2d.drawString("Logo not available", 50, 70);
        }

        // PayTrace Name in the middle
        g2d.setFont(new Font("Arial", Font.BOLD, 22));
        g2d.drawString("PayTrace", 300, 100); // Adjust the coordinates as needed

        // Username
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString("User: " + username, 50, 160);

        // Add space before Transaction History
        int yOffset = 180;

        // Draw a straight line
        g2d.setColor(Color.BLACK);
        g2d.drawLine(50, yOffset, (int) pageFormat.getImageableWidth() - 50, yOffset);
        yOffset += 20; // Add some space after the line

        // Title
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("Transaction History", 50, yOffset);
        yOffset += 30; // Add space after the title

        // Calculate total amount and balance
        double totalDeductions = currentDeductions.stream().mapToDouble(Deduction::getAmount).sum();
        double balance = totalAmount - totalDeductions;

        // Total Amount and Balance
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.drawString("Total Amount: " + String.format("%.2f", totalAmount), 50, yOffset);
        g2d.drawString("Balance: " + String.format("%.2f", balance), 250, yOffset);
        yOffset += 30;

        // Date Range
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.drawString("Date Range: " + formatDates(startDate, endDate), 50, yOffset);
        yOffset += 30;

        // Deduction Details Table
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        FontMetrics metrics = g2d.getFontMetrics();
        int lineHeight = metrics.getHeight();

        // Define table headers and column widths
        String[] headers = {"Reason", "Amount", "Date"};
        int[] colWidths = {200, 100, 100};

        // Draw table headers
        int x = 50;
        for (int i = 0; i < headers.length; i++) {
            g2d.drawString(headers[i], x, yOffset);
            x += colWidths[i];
        }

        // Draw table rows
        yOffset += lineHeight;
        for (Deduction deduction : currentDeductions) {
            x = 50;
            g2d.drawString(deduction.getReason(), x, yOffset);
            x += colWidths[0];
            g2d.drawString(String.format("%.2f", deduction.getAmount()), x, yOffset);
            x += colWidths[1];
            g2d.drawString(deduction.getDateTime().format(DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)), x, yOffset);
            yOffset += lineHeight;
        }

        return Printable.PAGE_EXISTS;
    }

    private static String formatDates(LocalDate startDate, LocalDate endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);
        String start = startDate != null ? startDate.format(formatter) : "N/A";
        String end = endDate != null ? endDate.format(formatter) : "N/A";
        return start + " - " + end;
    }

    private static BufferedImage loadImage() {
        try (InputStream is = PrintHelper.class.getResourceAsStream(LOGO_PATH)) {
            if (is != null) {
                return ImageIO.read(is);
            } else {
                LOGGER.warning("Image file does not exist: " + LOGO_PATH);
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error reading image file", e);
        }
        return null;
    }
}
