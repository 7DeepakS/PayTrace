package org.example.paytrace.service;

import org.example.paytrace.model.Deduction;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

public class DeductionsChart {

    public JPanel createChartPanel(List<Deduction> deductions) {
        DefaultCategoryDataset dataset = createDataset(deductions);
        JFreeChart chart = createChart(dataset);
        customizeChartAppearance(chart);
        return new ChartPanel(chart);
    }

    private DefaultCategoryDataset createDataset(List<Deduction> deductions) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Deduction deduction : deductions) {
            dataset.addValue(deduction.getAmount(), "Deductions", deduction.getDateTime().toString());
        }
        return dataset;
    }

    private JFreeChart createChart(DefaultCategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBarChart(
                "Deductions History",
                "Date",
                "Amount (₹)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.lightGray);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setSeriesPaint(0, new Color(79, 129, 189)); // Blue bars
        renderer.setDrawBarOutline(false);

        DecimalFormat decimalFormat = new DecimalFormat("₹0.00");
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", decimalFormat));
        renderer.setDefaultItemLabelsVisible(true);
        renderer.setDefaultPositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_CENTER));
        renderer.setDefaultItemLabelFont(new Font("SansSerif", Font.BOLD, 12));

        return chart;
    }

    private void customizeChartAppearance(JFreeChart chart) {
        chart.setBackgroundPaint(new Color(240, 240, 240));
        chart.getTitle().setPaint(new Color(59, 89, 182));

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(new Color(171, 171, 171));

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(79, 129, 189)); // Blue bars
        renderer.setDrawBarOutline(false);

        DecimalFormat decimalFormat = new DecimalFormat("₹0.00");
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", decimalFormat));
        renderer.setDefaultItemLabelsVisible(true);
        renderer.setDefaultPositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_CENTER));
        renderer.setDefaultItemLabelFont(new Font("SansSerif", Font.BOLD, 12));
    }
}
