package org.example.paytrace.service;

import org.example.paytrace.model.Deduction;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DeductionsChartBackend {

    public JFreeChart createChart(List<Deduction> deductions, ChartConfig config) {
        DefaultCategoryDataset dataset = createDataset(deductions, config);
        JFreeChart chart = createBaseChart(dataset, config);
        customizeChart(chart, config);
        return chart;
    }

    private DefaultCategoryDataset createDataset(List<Deduction> deductions, ChartConfig config) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(config.getDateFormat());
        for (Deduction deduction : deductions) {
            dataset.addValue(deduction.getAmount(), "Deductions",
                    deduction.getDateTime().format(formatter));
        }
        return dataset;
    }

    private JFreeChart createBaseChart(DefaultCategoryDataset dataset, ChartConfig config) {
        return ChartFactory.createBarChart(
                config.getTitle(),
                config.getXAxisLabel(),
                config.getYAxisLabel(),
                dataset
        );
    }

    private void customizeChart(JFreeChart chart, ChartConfig config) {
        chart.setBackgroundPaint(config.getBackgroundColor());

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        customizeRenderer(plot, config);
        customizeAxes(plot, config);
    }

    private void customizeRenderer(CategoryPlot plot, ChartConfig config) {
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, config.getBarColor());
        renderer.setDrawBarOutline(false);

        if (config.isShowItemLabels()) {
            renderer.setDefaultItemLabelsVisible(true);
            renderer.setDefaultItemLabelFont(new Font(config.getFontName(), Font.PLAIN, config.getFontSize()));
        }
    }

    private void customizeAxes(CategoryPlot plot, ChartConfig config) {
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setTickLabelFont(new Font(config.getFontName(), Font.PLAIN, config.getFontSize()));

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setTickLabelFont(new Font(config.getFontName(), Font.PLAIN, config.getFontSize()));
        rangeAxis.setNumberFormatOverride(new DecimalFormat(config.getValueFormat()));
    }
}