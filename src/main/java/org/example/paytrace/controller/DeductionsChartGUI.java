package org.example.paytrace.controller;

import org.example.paytrace.model.Deduction;
import org.example.paytrace.service.ChartConfig;
import org.example.paytrace.service.DeductionsChartService;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DeductionsChartGUI {
    private final DeductionsChartService chartService;

    public DeductionsChartGUI() {
        this.chartService = new DeductionsChartService();
    }

    public void displayChart(List<Deduction> deductions, ChartConfig config) {
        JFreeChart chart = chartService.createChart(deductions, config);
        JFrame frame = new JFrame(config.getTitle());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new ChartPanel(chart), BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}