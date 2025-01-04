package org.example.employees.controllers;

import org.example.employees.models.Attendance;
import org.example.employees.models.Employee;
import org.example.employees.services.AttendanceService;
import org.example.employees.services.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class AttendanceController {

    private final EmployeeServiceImpl employeeServiceImpl;
    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(EmployeeServiceImpl employeeServiceImpl, AttendanceService attendanceService) {
        this.employeeServiceImpl = employeeServiceImpl;
        this.attendanceService = attendanceService;
    }

    @GetMapping
    public String getAllAttendances(Model model) {
        List<Attendance> attendances = attendanceService.getAllAttendances();
        List<Employee> employees = employeeServiceImpl.getAllEmployees("lastName", "asc");

        model.addAttribute("pageTitle", "Home");
        model.addAttribute("contentFragment", "attendances");
        model.addAttribute("attendances", attendances);
        model.addAttribute("employees", employees);

        return "layout";
    }

    @GetMapping("/attendance/details")
    public String showAttendanceForm(@RequestParam Long id, Model model) {
        var attendanceOptional = attendanceService.getAttendanceById(id);

        if (attendanceOptional.isEmpty()) {
            return "redirect:/error";
        }

        model.addAttribute("attendance", attendanceOptional.get());
        model.addAttribute("employees", employeeServiceImpl.getAllEmployees("lastName", "asc"));
        return "attendance-edit";
    }

    @GetMapping("/attendance/edit")
    public String showUpdateForm(@RequestParam Long id, Model model) {
        Optional<Attendance> attendanceOptional = attendanceService.getAttendanceById(id);

        if (attendanceOptional.isEmpty()) {
            return "redirect:/error";
        }

        model.addAttribute("attendance", attendanceOptional.get());
        model.addAttribute("employees", employeeServiceImpl.getAllEmployees("lastName", "asc"));
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
        boolean success = attendanceService.updateAttendance(attendance.getId(), attendance, employeeId);

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

    @PostMapping("/attendance/increaseHours")
    public String increaseWorkedHours(@RequestParam Long id) {
        attendanceService.updateWorkedHours(id, true);
        return "redirect:/";
    }

    @PostMapping("/attendance/decreaseHours")
    public String decreaseWorkedHours(@RequestParam Long id) {
        attendanceService.updateWorkedHours(id, false);
        return "redirect:/";
    }

}

