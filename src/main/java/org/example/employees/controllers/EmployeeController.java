package org.example.employees.controllers;

import org.example.employees.models.Attendance;
import org.example.employees.models.Employee;
import org.example.employees.services.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getAllEmployees(Model model) {
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "index";
    }

    @GetMapping("/{id}")
    public String getEmployeeById(@PathVariable Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id).orElse(null);
        if (employee == null) {
            return "employee/not-found";
        }
        model.addAttribute("employee", employee);
        return "employee/detail";
    }

    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("attendance", new Attendance());
        return "form";
    }

    @PostMapping("/submitForm")
    public String submitForm(@ModelAttribute Employee employee, @ModelAttribute Attendance attendance) {

        attendance.setEmployee(employee);
        employee.getAttendanceRecords().add(attendance);

        employeeService.save(employee);

        return "result";
    }
}