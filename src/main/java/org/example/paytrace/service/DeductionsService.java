package org.example.paytrace.service;

import org.example.paytrace.model.Deduction;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DeductionsService {

    public List<Deduction> filterAndSortDeductionsByDate(List<Deduction> deductions, LocalDate startDate, LocalDate endDate) {
        return deductions.stream()
                .filter(deduction -> {
                    LocalDate deductionDate = deduction.getDateTime().toLocalDate();
                    return (startDate == null || !deductionDate.isBefore(startDate)) &&
                            (endDate == null || !deductionDate.isAfter(endDate));
                })
                .sorted(Comparator.comparing(deduction -> deduction.getDateTime().toLocalDate()))
                .collect(Collectors.toList());
    }
}