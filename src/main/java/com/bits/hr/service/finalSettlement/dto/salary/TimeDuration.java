package com.bits.hr.service.finalSettlement.dto.salary;

import java.util.Objects;
import lombok.Data;

public class TimeDuration {

    private int year;
    private int month;
    private int day;

    public TimeDuration() {}

    public int getYear() {
        return year;
    }

    public TimeDuration setYear(int year) {
        this.year = year;
        return this;
    }

    public int getMonth() {
        return month;
    }

    public TimeDuration setMonth(int month) {
        this.month = month;
        return this;
    }

    public int getDay() {
        return day;
    }

    public TimeDuration setDay(int day) {
        this.day = day;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeDuration that = (TimeDuration) o;
        return getYear() == that.getYear() && getMonth() == that.getMonth() && getDay() == that.getDay();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getYear(), getMonth(), getDay());
    }

    @Override
    public String toString() {
        return "TimeDuration{" + "year=" + year + ", month=" + month + ", day=" + day + '}';
    }
}
