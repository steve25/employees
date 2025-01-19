package org.example.employees.models.dtos;

import java.time.LocalDate;

public class AttendanceDTO {

    private LocalDate date;
    private int workedHours;
    private boolean present;
    private Long employeeId;

    public AttendanceDTO() {}

    public AttendanceDTO(LocalDate date, int workedHours, boolean present, Long employeeId) {
        this.date = date;
        this.workedHours = workedHours;
        this.present = present;
        this.employeeId = employeeId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getWorkedHours() {
        return workedHours;
    }

    public void setWorkedHours(int workedHours) {
        this.workedHours = workedHours;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
