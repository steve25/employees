package org.example.employees;

import org.example.employees.models.Attendance;
import org.example.employees.models.Employee;
import org.example.employees.repositories.AttendanceRepository;
import org.example.employees.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class EmployeesApplication implements ApplicationRunner {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeesApplication(AttendanceRepository attendanceRepository, EmployeeRepository employeeRepository) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(EmployeesApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Employee employee1 = new Employee("Roman", "Gatek", "Boss");
        Employee employee2 = new Employee("Stefan", "Pocsik", "Developer");

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        Attendance attendance1 = new Attendance();
        attendance1.setEmployee(employee1);
        attendance1.setPresent(true);
        attendance1.setWorkedHours(8);
        attendance1.setDate(LocalDate.now());

        Attendance attendance2 = new Attendance();
        attendance2.setEmployee(employee2);
        attendance2.setPresent(true);
        attendance2.setWorkedHours(7);
        attendance2.setDate(LocalDate.now().minusDays(1));

        attendanceRepository.save(attendance1);
        attendanceRepository.save(attendance2);

    }
}
