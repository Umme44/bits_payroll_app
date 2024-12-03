package com.bits.hr.service.finalSettlement.dto.pf;

import java.util.Objects;

public class TotalPf {

    private double totalEmployeePortion;
    private double totalEmployerPortion;
    private double total;

    public TotalPf() {}

    public TotalPf(double totalEmployeePortion, double totalEmployerPortion) {
        this.totalEmployeePortion = totalEmployeePortion;
        this.totalEmployerPortion = totalEmployerPortion;
        this.total = this.totalEmployeePortion + this.totalEmployeePortion;
    }

    public TotalPf(double totalEmployeePortion, double totalEmployerPortion, double total) {
        this.totalEmployeePortion = totalEmployeePortion;
        this.totalEmployerPortion = totalEmployerPortion;
        this.total = total;
    }

    public double getTotalEmployeePortion() {
        return totalEmployeePortion;
    }

    public void setTotalEmployeePortion(double totalEmployeePortion) {
        this.totalEmployeePortion = totalEmployeePortion;
        this.total = this.totalEmployeePortion + this.totalEmployeePortion;
    }

    public double getTotalEmployerPortion() {
        return totalEmployerPortion;
    }

    public void setTotalEmployerPortion(double totalEmployerPortion) {
        this.totalEmployerPortion = totalEmployerPortion;
        this.total = this.totalEmployeePortion + this.totalEmployeePortion;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TotalPf totalPf = (TotalPf) o;
        return (
            Double.compare(totalPf.getTotalEmployeePortion(), getTotalEmployeePortion()) == 0 &&
            Double.compare(totalPf.getTotalEmployerPortion(), getTotalEmployerPortion()) == 0 &&
            Double.compare(totalPf.getTotal(), getTotal()) == 0
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTotalEmployeePortion(), getTotalEmployerPortion(), getTotal());
    }

    @Override
    public String toString() {
        return (
            "TotalPf{" +
            "totalEmployeePortion=" +
            totalEmployeePortion +
            ", totalEmployerPortion=" +
            totalEmployerPortion +
            ", total=" +
            total +
            '}'
        );
    }
}
