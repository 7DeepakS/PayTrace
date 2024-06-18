package org.example.paytrace.service;

import javax.swing.*;
import java.awt.*;
import java.awt.print.*;

public class PrintHelper {

    public static void printDeductions(String textToPrint) {
        try {
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            printerJob.setJobName("Deductions History");

            if (printerJob.printDialog()) {
                printerJob.setPrintable(new Printable() {
                    @Override
                    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                        if (pageIndex > 0) {
                            return Printable.NO_SUCH_PAGE;
                        }

                        Graphics2D g2d = (Graphics2D) graphics;
                        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                        FontMetrics metrics = g2d.getFontMetrics();
                        int lineHeight = metrics.getHeight();

                        String[] lines = textToPrint.split("\n");
                        int y = 50;
                        for (String line : lines) {
                            g2d.drawString(line, 100, y);
                            y += lineHeight;
                        }

                        return Printable.PAGE_EXISTS;
                    }
                });

                printerJob.print();
            }
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(null, "Error printing: " + ex.getMessage(), "Print Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
