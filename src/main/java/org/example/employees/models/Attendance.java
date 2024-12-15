package org.example.employees.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "attendances")
@Getter @Setter
public class Attendance extends BaseEntity {
    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "worked_hours", nullable = false)
    private int workedHours = 0;

    @Column(name = "is_present", nullable = false)
    private boolean isPresent = true;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;
}
