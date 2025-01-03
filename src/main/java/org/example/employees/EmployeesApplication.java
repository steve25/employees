package org.example.employees;

import org.example.employees.models.Attendance;
import org.example.employees.models.Employee;
import org.example.employees.models.Role;
import org.example.employees.models.User;
import org.example.employees.repositories.AttendanceRepository;
import org.example.employees.repositories.EmployeeRepository;
import org.example.employees.repositories.RoleRepository;
import org.example.employees.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.*;

@SpringBootApplication
public class EmployeesApplication implements ApplicationRunner {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public EmployeesApplication(AttendanceRepository attendanceRepository, EmployeeRepository employeeRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(EmployeesApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        seedData();
    }

    public void seedData() {
        List<Employee> employees = createEmployees();
        List<Attendance> attendances = new ArrayList<>();

        Role role = new Role();
        role.setName("ROLE_USER");

        roleRepository.save(role);

        for (Employee employee : employees) {
            User user = new User(employee.getFirstName().toLowerCase(), "123");
            user.setEmployee(employee);
            user.setRoles(List.of(role));
            userRepository.save(user);
            employeeRepository.save(employee);
            attendances.addAll(createAttendancesForEmployee(employee));
        }

        attendanceRepository.saveAll(attendances);
    }

    private List<Employee> createEmployees() {
        return List.of(
                new Employee("Roman", "Gatek", "Boss"),
                new Employee("Stefan", "Pocsik", "Developer"),
                new Employee("Anna", "Kovac", "Designer"),
                new Employee("Peter", "Horvath", "Manager"),
                new Employee("Eva", "Miklos", "Tester"),
                new Employee("Marek", "Sabol", "Architect"),
                new Employee("Jana", "Tothova", "Developer"),
                new Employee("Tomas", "Szabo", "Support"),
                new Employee("Ivana", "Nagy", "HR Specialist"),
                new Employee("Miroslav", "Farkas", "Sales"),
                new Employee("Lucia", "Hajdukova", "Accountant"),
                new Employee("Ondrej", "Urban", "Marketing Specialist"),
                new Employee("Martin", "Cerny", "Backend Developer"),
//                new Employee("Veronika", "Dubcova", "Frontend Developer"),
                 new Employee("Juraj", "Stancik", "DevOps Engineer")
        );
    }

    private List<Attendance> createAttendancesForEmployee(Employee employee) {
        List<Attendance> attendances = new ArrayList<>();
        Random random = new Random();
        int numberOfRecords = 10 + random.nextInt(6); // 10 to 15 records

        for (int i = 0; i < numberOfRecords; i++) {
            Attendance attendance = new Attendance();
            attendance.setEmployee(employee);
            attendance.setPresent(random.nextBoolean());
            attendance.setWorkedHours(random.nextInt(4) + 5); // Worked hours: 5 to 8
            attendance.setDate(LocalDate.now().minusDays(random.nextInt(30))); // Dates within the last 30 days
            attendances.add(attendance);
        }
        return attendances;
    }
}
