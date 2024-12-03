package com.bits.hr.domain;

import java.sql.Timestamp;

public class IntegratedAttendance {

    private Long id;
    private String employeePin;
    private Timestamp timestamp;
    private Integer terminal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeePin() {
        return employeePin;
    }

    public void setEmployeePin(String employeePin) {
        this.employeePin = employeePin;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getTerminal() {
        return terminal;
    }

    public void setTerminal(Integer terminal) {
        this.terminal = terminal;
    }

    @Override
    public String toString() {
        return (
            "IntegratedAttendance{" +
            "id=" +
            id +
            ", employeePin=" +
            employeePin +
            ", timestamp=" +
            timestamp +
            ", terminal=" +
            terminal +
            '}'
        );
    }
}
