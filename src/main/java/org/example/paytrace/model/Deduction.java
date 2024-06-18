package org.example.paytrace.model;

import java.time.LocalDateTime;

public class Deduction {
    private double amount;
    private String reason;
    private LocalDateTime dateTime;

    public Deduction(double amount, String reason, LocalDateTime dateTime) {
        this.amount = amount;
        this.reason = reason;
        this.dateTime = dateTime;
    }

    public double getAmount() {
        return amount;
    }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return "Deduction{" +
                "amount=" + amount +
                ", reason='" + reason + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}
