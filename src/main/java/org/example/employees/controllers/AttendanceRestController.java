package org.example.employees.controllers;

import org.example.employees.models.Attendance;
import org.example.employees.models.Employee;
import org.example.employees.models.dtos.AttendanceDTO;
import org.example.employees.services.AttendanceService;
import org.example.employees.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*")
public class AttendanceRestController {

    private final AttendanceService attendanceService;
    private final EmployeeService employeeService;

    @Autowired
    public AttendanceRestController(AttendanceService attendanceService, EmployeeService employeeService) {
        this.attendanceService = attendanceService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<AttendanceDTO>> getAllAttendances() {
        List<Attendance> attendances = attendanceService.getAllAttendances();
        List<AttendanceDTO> attendanceDTOs = attendances.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(attendanceDTOs);
    }

    @GetMapping("/by-date")
    public ResponseEntity<List<AttendanceDTO>> getAttendancesByDate(@RequestParam("date") String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            List<Attendance> attendances = attendanceService.getAttendancesByDate(localDate);
            List<AttendanceDTO> attendanceDTOs = attendances.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(attendanceDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(List.of());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceDTO> getAttendanceById(@PathVariable Long id) {
        return attendanceService.getAttendanceByIdWithEmployee(id)
                .map(attendance -> ResponseEntity.ok(convertToDTO(attendance)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/addattendance")
    public ResponseEntity<String> createAttendance(@RequestBody AttendanceDTO attendanceDTO) {
        Attendance attendance = convertToEntity(attendanceDTO);
        boolean success = attendanceService.addAttendanceForEmployee(attendanceDTO.getEmployeeId(), attendance);
        if (!success) {
            return new ResponseEntity<>("Attendance could not be created. Possible conflict or invalid employee ID.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Attendance created successfully.", HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAttendance(@PathVariable Long id, @RequestBody AttendanceDTO attendanceDTO) {
        System.out.println("Updating Attendance ID: " + id);
        System.out.println("DTO Data: " + attendanceDTO);

        Attendance attendance = convertToEntity(attendanceDTO);
        attendance.setId(id);

        boolean success = attendanceService.updateAttendance(id, attendance, attendanceDTO.getEmployeeId());

        if (!success) {
            System.out.println("Failed to update attendance.");
            return new ResponseEntity<>("Failed to update attendance. Check employee ID or duplicates.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Attendance updated successfully.", HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAttendance(@PathVariable Long id) {
        attendanceService.deleteAttendance(id);
        return new ResponseEntity<>("Attendance deleted successfully.", HttpStatus.OK);
    }

    @PatchMapping("/{id}/increase-hours")
    public ResponseEntity<String> increaseWorkedHours(@PathVariable Long id) {
        attendanceService.updateWorkedHours(id, true);
        return new ResponseEntity<>("Worked hours increased.", HttpStatus.OK);
    }

    @PatchMapping("/{id}/decrease-hours")
    public ResponseEntity<String> decreaseWorkedHours(@PathVariable Long id) {
        attendanceService.updateWorkedHours(id, false);
        return new ResponseEntity<>("Worked hours decreased.", HttpStatus.OK);
    }

    private AttendanceDTO convertToDTO(Attendance attendance) {
        return new AttendanceDTO(
                attendance.getDate(),
                attendance.getWorkedHours(),
                attendance.isPresent(),
                attendance.getEmployee().getId()
        );
    }

    private Attendance convertToEntity(AttendanceDTO attendanceDTO) {
        Attendance attendance = new Attendance();
        attendance.setDate(attendanceDTO.getDate());
        attendance.setWorkedHours(attendanceDTO.getWorkedHours());
        attendance.setPresent(attendanceDTO.isPresent());

        Optional<Employee> employeeOptional = employeeService.getEmployeeById(attendanceDTO.getEmployeeId());
        employeeOptional.ifPresent(attendance::setEmployee);

        return attendance;
    }
}