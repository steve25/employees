package org.example.employees.controllers;

import org.example.employees.models.Attendance;
import org.example.employees.models.Employee;
import org.example.employees.services.AttendanceService;
import org.example.employees.services.EmployeeService;
import org.example.employees.services.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String getAllAttendances(
            @RequestParam(required = false, defaultValue = "date") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDirection,
            @RequestParam( required = false, defaultValue = "0") int page,
            Model model) {

        int pageSize = 50;
        Page<Attendance> attendancePage = attendanceService.getAttendancesSorted(sortBy, sortDirection, page, pageSize);

        String nextSortDirection = sortDirection.equalsIgnoreCase("asc") ? "desc" : "asc";

        model.addAttribute("attendances", attendancePage.getContent());
        model.addAttribute("employees", employeeService.getAllEmployees("lastName", "asc"));
        model.addAttribute("page", page);
        model.addAttribute("totalPages", attendancePage.getTotalPages());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("nextSortDirection", nextSortDirection);
        model.addAttribute("contentFragment", "attendances");

        return "layout";
    }

    @GetMapping("/attendance/details")
    public String showAttendanceForm(@RequestParam Long id, Model model) {
        var attendanceOptional = attendanceService.getAttendanceById(id);

        if (attendanceOptional.isEmpty()) {
            return "redirect:/error";
        }

        model.addAttribute("attendance", attendanceOptional.get());
        model.addAttribute("employees", employeeService.getAllEmployees("lastName", "asc"));
        model.addAttribute("contentFragment", "attendance-detail");

        return "layout";
    }

    @GetMapping("/attendance/edit")
    public String showUpdateForm(@RequestParam Long id, Model model) {
        Optional<Attendance> attendanceOptional = attendanceService.getAttendanceById(id);

        if (attendanceOptional.isEmpty()) {
            return "redirect:/error";
        }

        model.addAttribute("attendance", attendanceOptional.get());
        model.addAttribute("employees", employeeService.getAllEmployees("lastName", "asc"));
        model.addAttribute("contentFragment", "attendance-edit");

        return "layout";
    }

    @PostMapping("/addAttendance")
    public String addAttendance(@RequestParam long employeeId, @ModelAttribute Attendance attendance, Model model) {
        boolean success = attendanceService.addAttendanceForEmployee(employeeId, attendance);

        if (!success) {
            model.addAttribute("error", "A record for this day already exists.");
            model.addAttribute("employees", employeeService.getAllEmployees("lastName", "asc"));
            return "redirect:/error";
        }

        return "redirect:/";
    }

    @PostMapping("/attendance/delete")
    public String deleteAttendance(@RequestParam Long id) {
        attendanceService.deleteAttendance(id);
        return "redirect:/";
    }

    @PostMapping("/attendance/update")
    public String updateAttendance(@ModelAttribute Attendance attendance) {
        boolean success = attendanceService.updateAttendance(attendance);

        if (!success) {
            return "redirect:/error";
        }

        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String getAttendanceDetails(@PathVariable Long id, Model model) {
        Optional<Attendance> attendanceOptional = attendanceService.getAttendanceById(id);

        if (attendanceOptional.isEmpty()) {
            return "redirect:/error";
        }

        model.addAttribute("attendance", attendanceOptional.get());
        return "attendance-detail";
    }

    @GetMapping("/attendance/increaseHours")
    public String increaseWorkedHours(@RequestParam Long id) {
        attendanceService.updateWorkedHours(id, true);
        return "redirect:/";
    }

    @GetMapping("/attendance/decreaseHours")
    public String decreaseWorkedHours(@RequestParam Long id) {
        attendanceService.updateWorkedHours(id, false);
        return "redirect:/";
    }
}