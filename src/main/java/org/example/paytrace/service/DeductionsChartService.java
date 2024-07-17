package org.example.paytrace.service;

import org.example.paytrace.model.Deduction;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DeductionsChartService {
    public JFreeChart createChart(List<Deduction> deductions, ChartConfig config) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(config.dateFormat);

        Map<LocalDate, Double> aggregatedDeductions = deductions.stream()
                .collect(Collectors.groupingBy(
                        d -> d.getDateTime().toLocalDate(),
                        Collectors.summingDouble(Deduction::getAmount)
                ));

        aggregatedDeductions.forEach((date, amount) ->
                dataset.addValue(amount, "Deductions", date.format(formatter)));

        JFreeChart chart = ChartFactory.createBarChart(config.title, config.xAxisLabel, config.yAxisLabel, dataset);
        chart.setBackgroundPaint(config.backgroundColor);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, config.barColor);
        renderer.setDrawBarOutline(false);
        if (config.showItemLabels) {
            renderer.setDefaultItemLabelsVisible(true);
            renderer.setDefaultItemLabelFont(new Font(config.fontName, Font.PLAIN, config.fontSize));
        }

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setTickLabelFont(new Font(config.fontName, Font.PLAIN, config.fontSize));

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setTickLabelFont(new Font(config.fontName, Font.PLAIN, config.fontSize));
        rangeAxis.setNumberFormatOverride(new DecimalFormat(config.valueFormat));

        return chart;
    }

    public static class ChartConfig {
        String title = "Deductions History", xAxisLabel = "Date", yAxisLabel = "Amount";
        Color barColor = Color.BLUE, backgroundColor = Color.WHITE;
        String valueFormat = "#,##0.00", dateFormat = "yyyy-MM-dd", fontName = "SansSerif";
        boolean showItemLabels = true;
        int fontSize = 12;

        public ChartConfig setTitle(String title) { this.title = title; return this; }
        public ChartConfig setXAxisLabel(String xAxisLabel) { this.xAxisLabel = xAxisLabel; return this; }
        public ChartConfig setYAxisLabel(String yAxisLabel) { this.yAxisLabel = yAxisLabel; return this; }
        public ChartConfig setBarColor(Color barColor) { this.barColor = barColor; return this; }
        public ChartConfig setBackgroundColor(Color backgroundColor) { this.backgroundColor = backgroundColor; return this; }
        public ChartConfig setValueFormat(String valueFormat) { this.valueFormat = valueFormat; return this; }
        public ChartConfig setDateFormat(String dateFormat) { this.dateFormat = dateFormat; return this; }
        public ChartConfig setShowItemLabels(boolean showItemLabels) { this.showItemLabels = showItemLabels; return this; }
        public ChartConfig setFontName(String fontName) { this.fontName = fontName; return this; }
        public ChartConfig setFontSize(int fontSize) { this.fontSize = fontSize; return this; }

        public String getTitle() { return title; }

        public static class Builder {
            private final ChartConfig config = new ChartConfig();
            public Builder title(String title) { config.title = title; return this; }
            public Builder xAxisLabel(String xAxisLabel) { config.xAxisLabel = xAxisLabel; return this; }
            public Builder yAxisLabel(String yAxisLabel) { config.yAxisLabel = yAxisLabel; return this; }
            public Builder barColor(Color barColor) { config.barColor = barColor; return this; }
            public Builder backgroundColor(Color backgroundColor) { config.backgroundColor = backgroundColor; return this; }
            public Builder valueFormat(String valueFormat) { config.valueFormat = valueFormat; return this; }
            public Builder dateFormat(String dateFormat) { config.dateFormat = dateFormat; return this; }
            public Builder showItemLabels(boolean showItemLabels) { config.showItemLabels = showItemLabels; return this; }
            public Builder fontName(String fontName) { config.fontName = fontName; return this; }
            public Builder fontSize(int fontSize) { config.fontSize = fontSize; return this; }
            public ChartConfig build() { return config; }
        }
    }

    public static class DeductionsChartGUI {
        private final DeductionsChartService chartService;

        public DeductionsChartGUI() {
            this.chartService = new DeductionsChartService();
        }

        public void displayChart(List<Deduction> deductions, ChartConfig config) {
            JFreeChart chart = chartService.createChart(deductions, config);
            JFrame frame = new JFrame(config.getTitle());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(800, 600);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(1000, 600)); // Set preferred size for horizontal scrolling

            JScrollPane scrollPane = new JScrollPane(chartPanel);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

            frame.add(scrollPane, BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }
}
