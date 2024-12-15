package org.example.employees.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Attendance extends BaseEntity {
    @Column(nullable = false)
    private LocalDate date;

    private int workedHours;

    private boolean isPresent;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Empleyee employee;
}
