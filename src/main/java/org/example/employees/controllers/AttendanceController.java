package org.example.employees.controllers;

import org.example.employees.models.Attendance;
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

    private final EmployeeServiceImpl employeeServiceImpl;
    private final AttendanceService attendanceService;
    private final EmployeeService employeeService;

    @Autowired
    public AttendanceController(EmployeeServiceImpl employeeServiceImpl, AttendanceService attendanceService, EmployeeService employeeService) {
        this.employeeServiceImpl = employeeServiceImpl;
        this.attendanceService = attendanceService;
        this.employeeService = employeeService;
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
        model.addAttribute("page", page);
        model.addAttribute("totalPages", attendancePage.getTotalPages());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("nextSortDirection", nextSortDirection);
        model.addAttribute("contentFragment", "attendances");

        model.addAttribute("employees", employeeServiceImpl.getAllEmployees("lastName", "asc"));

        return "layout";
    }

    @GetMapping("/attendance/details")
    public String showAttendanceForm(@RequestParam Long id, Model model) {
        Optional<Attendance> attendanceOptional = attendanceService.getAttendanceByIdWithEmployee(id);

        if (attendanceOptional.isEmpty()) {
            model.addAttribute("error", "Attendance record not found.");
            return "redirect:/error";
        }

        Attendance attendance = attendanceOptional.get();

        model.addAttribute("attendance", attendance);
        return "attendance-detail";
    }

    @GetMapping("/attendance/edit")
    public String showUpdateForm(@RequestParam Long id, Model model) {
        Optional<Attendance> attendanceOptional = attendanceService.getAttendanceByIdWithEmployee(id);

        if (attendanceOptional.isEmpty()) {
            return "redirect:/error";
        }

        model.addAttribute("attendance", attendanceOptional.orElseThrow(() -> new RuntimeException("Attendance not found")));
        model.addAttribute("employees", employeeServiceImpl.getAllEmployees("lastName", "asc"));
        model.addAttribute("editMode", true);
        return "attendance-edit";
    }

    @PostMapping("/addAttendance")
    public String addAttendance(@RequestParam long employeeId, @ModelAttribute Attendance attendance, Model model) {
        boolean success = attendanceService.addAttendanceForEmployee(employeeId, attendance);

        if (!success) {
            model.addAttribute("error", "A record for this day already exists.");
            model.addAttribute("employees", employeeServiceImpl.getAllEmployees("lastName", "asc"));
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
    public String updateAttendance(@ModelAttribute Attendance attendance, @RequestParam Long employeeId) {

        System.out.println("Updating Attendance ID: " + attendance.getId());
        System.out.println("Employee ID: " + employeeId);

        boolean success = attendanceService.updateAttendance(attendance.getId(), attendance, employeeId);

        if (!success) {
            return "redirect:/error";
        }

        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String getAttendanceDetails(@PathVariable Long id, Model model) {
        Optional<Attendance> attendanceOptional = attendanceService.getAttendanceByIdWithEmployee(id);

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

    @GetMapping("/favicon.ico")
    @ResponseBody
    public void returnNoFavicon() {
    }
}