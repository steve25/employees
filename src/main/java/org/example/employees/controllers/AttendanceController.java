package org.example.employees.controllers;

import org.example.employees.models.Attendance;
import org.example.employees.models.Employee;
import org.example.employees.services.AttendanceService;
import org.example.employees.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        Optional<Attendance> attendance = attendanceService.getAttendanceById(id);

        if (attendance.isEmpty()) {
            return "redirect:/";
        }

        model.addAttribute("pageTitle", "Details");
        model.addAttribute("contentFragment", "attendance-detail");
        model.addAttribute("attendance", attendance.get());

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

    @GetMapping("/attendance/edit")
    public String showUpdateForm(@RequestParam Long id, Model model) {
        Optional<Attendance> attendanceOptional = attendanceService.getAttendanceById(id);
        if (attendanceOptional.isEmpty()) {
            return "redirect:/";
        }

        model.addAttribute("attendance", attendanceOptional.get());
        model.addAttribute("employee", attendanceOptional.get().getEmployee());
        model.addAttribute("contentFragment", "attendance-edit");

        return "layout";
    }

    @PostMapping("/attendance/update")
    public String updateAttendance(@ModelAttribute Attendance attendance) {
        System.out.println("Updating Attendance ID: " + attendance.getId());

        Optional<Attendance> existingAttendance = attendanceService.getAttendanceById(attendance.getId());
        if (existingAttendance.isEmpty()) {
            return "redirect:/";
        }

        Attendance updatedAttendance = existingAttendance.get();
        updatedAttendance.setDate(attendance.getDate());
        updatedAttendance.setWorkedHours(attendance.getWorkedHours());
        updatedAttendance.setPresent(attendance.isPresent());

        attendanceService.save(updatedAttendance);

        return "redirect:/";
    }

    @PostMapping("/attendance/delete")
    public String deleteAttendance(@RequestParam Long id) {
        attendanceService.deleteAttendance(id);
        return "redirect:/";
    }
}