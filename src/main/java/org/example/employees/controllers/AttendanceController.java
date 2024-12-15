package org.example.employees.controllers;

import org.example.employees.models.Attendance;
import org.example.employees.models.Employee;
import org.example.employees.services.AttendanceService;
import org.example.employees.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class AttendanceController {

    private final EmployeeService employeeService;
    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(EmployeeService employeeService, AttendanceService attendanceService) {
        this.employeeService = employeeService;
        this.attendanceService = attendanceService;
    }

    @GetMapping
    public String getAllAttendances(Model model) {
        List<Attendance> attendances = attendanceService.getAllAttendances();
        List<Employee> employees = employeeService.getAllEmployees();

        model.addAttribute("pageTitle", "Home");
        model.addAttribute("contentFragment", "home");
        model.addAttribute("attendances", attendances);
        model.addAttribute("employees", employees);

        return "layout";
    }

    @GetMapping("/{id}")
    public String getAttendanceById(@PathVariable Long id, Model model) {
        Attendance attendance = attendanceService.getAttendanceById(id);

        model.addAttribute("pageTitle", "Details");
        model.addAttribute("contentFragment", "attendance-detail");
        model.addAttribute("attendance", attendance);

        return "layout";
    }

    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("attendance", new Attendance());
        return "form";
    }

    @PostMapping("/addEmployee")
    public String addEmployee(@ModelAttribute Employee employee) {
        employeeService.save(employee);

        return "redirect:/";
    }

    @PostMapping("/addAttendance")
    public String addAttendance(@RequestParam long employeeId, @ModelAttribute Attendance attendance) {
        Optional<Employee> employee = employeeService.getEmployeeById(employeeId);

        if (employee.isEmpty()) {
            return "redirect:/";
        }

        attendance.setEmployee(employee.get());
        attendanceService.save(attendance);

        return "redirect:/";
    }
}