package com.bits.hr.service.salaryGenerationFractional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Fraction {

    private LocalDate startDate;
    private LocalDate endDate;
    private double effectiveGross;

    public Fraction() {}

    public Fraction(LocalDate startDate, LocalDate endDate, double effectiveGross) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.effectiveGross = effectiveGross;
    }

    public int getDaysBetween() {
        int daysBetween = (int) ChronoUnit.DAYS.between(this.startDate, this.endDate) + 1;
        return daysBetween;
    }

    public double getPerDayMainGross() {
        return effectiveGross / this.startDate.lengthOfMonth();
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public double getEffectiveGross() {
        return effectiveGross;
    }

    public void setEffectiveGross(double effectiveGross) {
        this.effectiveGross = effectiveGross;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fraction fraction = (Fraction) o;
        return (
            Double.compare(fraction.effectiveGross, effectiveGross) == 0 &&
            Objects.equals(startDate, fraction.startDate) &&
            Objects.equals(endDate, fraction.endDate)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, effectiveGross);
    }

    @Override
    public String toString() {
        return "Fraction{" + "startDate=" + startDate + ", endDate=" + endDate + ", effectiveGross=" + effectiveGross + '}';
    }
}
