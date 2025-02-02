package org.example.employees.repositories;

import org.example.employees.models.Attendance;
import org.example.employees.models.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class AttendanceRepositoryTest {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    void setUp() {
        // Clear previous data
        attendanceRepository.deleteAll();
        employeeRepository.deleteAll();

        // Create test employees
        employee1 = new Employee("John", "Doe", "Developer");
        employee2 = new Employee("Jane", "Smith", "Manager");

        employee1 = employeeRepository.save(employee1);
        employee2 = employeeRepository.save(employee2);

        // Create test attendance records
        Attendance attendance1 = new Attendance();
        attendance1.setEmployee(employee1);
        attendance1.setPresent(true);
        attendance1.setWorkedHours(8);
        attendance1.setDate(LocalDate.of(2024, 2, 1));

        Attendance attendance2 = new Attendance();
        attendance2.setEmployee(employee2);
        attendance2.setPresent(false);
        attendance2.setWorkedHours(0);
        attendance2.setDate(LocalDate.of(2024, 2, 1));

        attendanceRepository.saveAll(List.of(attendance1, attendance2));
    }

    @AfterEach
    void tearDown() {
        attendanceRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Test
    void findByEmployeeIdAndDate_ShouldReturnAttendance() {
        // Act
        var foundAttendance = attendanceRepository.findByEmployeeIdAndDate(employee1.getId(), LocalDate.of(2024, 2, 1));

        // Assert
        assertThat(foundAttendance).isPresent();
        assertThat(foundAttendance.get().getEmployee().getId()).isEqualTo(employee1.getId());
    }

    @Test
    void findByEmployeeIdAndDate_ShouldReturnEmpty_WhenNotFound() {
        // Act
        var foundAttendance = attendanceRepository.findByEmployeeIdAndDate(999L, LocalDate.of(2024, 2, 1));

        // Assert
        assertThat(foundAttendance).isEmpty();
    }
}