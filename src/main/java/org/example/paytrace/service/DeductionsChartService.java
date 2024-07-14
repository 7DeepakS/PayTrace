package org.example.paytrace.service;

import org.example.paytrace.model.Deduction;
import org.jfree.chart.JFreeChart;

import java.util.List;

public class DeductionsChartService {
    private final DeductionsChartBackend backend;

    public DeductionsChartService() {
        this.backend = new DeductionsChartBackend();
    }

    public JFreeChart createChart(List<Deduction> deductions, ChartConfig config) {
        return backend.createChart(deductions, config);
    }
}