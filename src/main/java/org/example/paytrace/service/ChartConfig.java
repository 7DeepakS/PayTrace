package org.example.paytrace.service;

import java.awt.*;

public class ChartConfig {
    private String title = "Deductions History";
    private String xAxisLabel = "Date";
    private String yAxisLabel = "Amount";
    private Color barColor = Color.BLUE;
    private Color backgroundColor = Color.WHITE;
    private String valueFormat = "#,##0.00";
    private String dateFormat = "yyyy-MM-dd";
    private boolean showItemLabels = true;
    private String fontName = "SansSerif";
    private int fontSize = 12;

    // Getter methods
    public String getTitle() { return title; }
    public String getXAxisLabel() { return xAxisLabel; }
    public String getYAxisLabel() { return yAxisLabel; }
    public Color getBarColor() { return barColor; }
    public Color getBackgroundColor() { return backgroundColor; }
    public String getValueFormat() { return valueFormat; }
    public String getDateFormat() { return dateFormat; }
    public boolean isShowItemLabels() { return showItemLabels; }
    public String getFontName() { return fontName; }
    public int getFontSize() { return fontSize; }

    // Setter methods with chaining
    public ChartConfig setTitle(String title) {
        this.title = title;
        return this;
    }

    public ChartConfig setXAxisLabel(String xAxisLabel) {
        this.xAxisLabel = xAxisLabel;
        return this;
    }

    public ChartConfig setYAxisLabel(String yAxisLabel) {
        this.yAxisLabel = yAxisLabel;
        return this;
    }

    public ChartConfig setBarColor(Color barColor) {
        this.barColor = barColor;
        return this;
    }

    public ChartConfig setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public ChartConfig setValueFormat(String valueFormat) {
        this.valueFormat = valueFormat;
        return this;
    }

    public ChartConfig setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        return this;
    }

    public ChartConfig setShowItemLabels(boolean showItemLabels) {
        this.showItemLabels = showItemLabels;
        return this;
    }

    public ChartConfig setFontName(String fontName) {
        this.fontName = fontName;
        return this;
    }

    public ChartConfig setFontSize(int fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    // Builder class
    public static class Builder {
        private final ChartConfig config = new ChartConfig();

        public Builder title(String title) {
            config.title = title;
            return this;
        }

        public Builder xAxisLabel(String xAxisLabel) {
            config.xAxisLabel = xAxisLabel;
            return this;
        }

        public Builder yAxisLabel(String yAxisLabel) {
            config.yAxisLabel = yAxisLabel;
            return this;
        }

        public Builder barColor(Color barColor) {
            config.barColor = barColor;
            return this;
        }

        public Builder backgroundColor(Color backgroundColor) {
            config.backgroundColor = backgroundColor;
            return this;
        }

        public Builder valueFormat(String valueFormat) {
            config.valueFormat = valueFormat;
            return this;
        }

        public Builder dateFormat(String dateFormat) {
            config.dateFormat = dateFormat;
            return this;
        }

        public Builder showItemLabels(boolean showItemLabels) {
            config.showItemLabels = showItemLabels;
            return this;
        }

        public Builder fontName(String fontName) {
            config.fontName = fontName;
            return this;
        }

        public Builder fontSize(int fontSize) {
            config.fontSize = fontSize;
            return this;
        }

        public ChartConfig build() {
            return config;
        }
    }

    @Override
    public String toString() {
        return "ChartConfig{" +
                "title='" + title + '\'' +
                ", xAxisLabel='" + xAxisLabel + '\'' +
                ", yAxisLabel='" + yAxisLabel + '\'' +
                ", barColor=" + barColor +
                ", backgroundColor=" + backgroundColor +
                ", valueFormat='" + valueFormat + '\'' +
                ", dateFormat='" + dateFormat + '\'' +
                ", showItemLabels=" + showItemLabels +
                ", fontName='" + fontName + '\'' +
                ", fontSize=" + fontSize +
                '}';
    }
}
