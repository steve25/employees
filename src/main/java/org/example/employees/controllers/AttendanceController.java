package org.example.employees.controllers;

import org.example.employees.models.Attendance;
import org.example.employees.services.AttendanceService;
import org.example.employees.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

        model.addAttribute("pageTitle", "Home");
        model.addAttribute("contentFragment", "home");
        model.addAttribute("attendances", attendances);

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

    @PostMapping("/submitForm")
    public String submitForm(@ModelAttribute Employee employee, @ModelAttribute Attendance attendance) {

        attendance.setEmployee(employee);
        employee.getAttendanceRecords().add(attendance);

        employeeService.save(employee);

        return "result";
    }
}