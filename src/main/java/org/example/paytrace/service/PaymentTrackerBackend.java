package org.example.paytrace.service;

import org.example.paytrace.model.Deduction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PaymentTrackerBackend {
    private double totalAmount;
    private double remainingAmount;
    private List<Deduction> deductions;

    public PaymentTrackerBackend() {
        deductions = new ArrayList<>();
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
        this.remainingAmount = totalAmount;
    }

    public double getRemainingAmount() {
        return remainingAmount;
    }

    public List<Deduction> getDeductions() {
        return deductions;
    }

    public void addDeduction(double deductionAmount, String reason, LocalDateTime dateTime) throws IllegalArgumentException {
        if (deductionAmount > remainingAmount) {
            throw new IllegalArgumentException("Deduction amount exceeds the remaining amount. Please enter a valid amount.");
        }

        Deduction deduction = new Deduction(deductionAmount, reason, dateTime);
        deductions.add(deduction);
        remainingAmount -= deductionAmount;
    }

    public void addDeductionWithDate(double deductionAmount, String reason, LocalDate date) throws IllegalArgumentException {
        LocalDateTime dateTime = date.atStartOfDay();

        if (deductionAmount > remainingAmount) {
            throw new IllegalArgumentException("Deduction amount exceeds the remaining amount. Please enter a valid amount.");
        }

        Deduction deduction = new Deduction(deductionAmount, reason, dateTime);
        deductions.add(deduction);
        remainingAmount -= deductionAmount;
    }

    public void reset() {
        totalAmount = 0;
        remainingAmount = 0;
        deductions.clear();
    }
}
