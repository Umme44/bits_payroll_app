package com.bits.hr.service.search;

import java.util.Objects;

public class AttendanceSummaryFilterDto {

    private String text;

    public AttendanceSummaryFilterDto() {}

    public AttendanceSummaryFilterDto(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AttendanceSummaryFilterDto)) return false;
        AttendanceSummaryFilterDto filterDto = (AttendanceSummaryFilterDto) o;
        return getText().equals(filterDto.getText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getText());
    }
}
