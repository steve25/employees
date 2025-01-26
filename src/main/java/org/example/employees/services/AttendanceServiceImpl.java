package org.example.employees.services;

import org.example.employees.models.Attendance;
import org.example.employees.models.Employee;
import org.example.employees.repositories.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeService employeeService;

    @Autowired
    public AttendanceServiceImpl(AttendanceRepository attendanceRepository, EmployeeService employeeService) {
        this.attendanceRepository = attendanceRepository;
        this.employeeService = employeeService;
    }

    @Override
    public Page<Attendance> getAttendancesSorted(String sortBy, String sortDirection, int page, int pageSize) {
        String validatedSortBy = validateSortBy(sortBy);
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;

        return attendanceRepository.findAll(PageRequest.of(page, pageSize, Sort.by(direction, validatedSortBy)));
    }

    private String validateSortBy(String sortBy) {
        return switch (sortBy != null ? sortBy.toLowerCase() : "") {
            case "employeelastname" -> "employee.lastName";
            case "workedhours" -> "workedHours";
            case "present" -> "present";
            default -> "date";
        };
    }

    @Override
    public boolean addAttendanceForEmployee(long employeeId, Attendance attendance) {
        if (!attendance.getDate().equals(LocalDate.now())) {
            return false;
        }

        Optional<Employee> employeeOptional = employeeService.getEmployeeById(employeeId);
        if (employeeOptional.isEmpty()) {
            return false;
        }

        Optional<Attendance> existingAttendance = attendanceRepository.findByEmployeeIdAndDate(employeeId, attendance.getDate());
        if (existingAttendance.isPresent()) {
            return false;
        }

        attendance.setEmployee(employeeOptional.get());
        attendanceRepository.save(attendance);
        return true;
    }

    @Override
    public Optional<Attendance> getAttendanceById(Long id) {
        return attendanceRepository.findById(id);
    }

    @Override
    public void deleteAttendance(Long id) {
        if (attendanceRepository.existsById(id)) {
            attendanceRepository.deleteById(id);
        }
    }

    @Override
    public boolean updateAttendance(Attendance attendance) {
        Optional<Attendance> existingAttendanceOptional = attendanceRepository.findById(attendance.getId());
        if (existingAttendanceOptional.isEmpty()) {
            return false;
        }

        Attendance existingAttendance = existingAttendanceOptional.get();

        existingAttendance.setDate(attendance.getDate());
        existingAttendance.setWorkedHours(attendance.getWorkedHours());
        existingAttendance.setPresent(attendance.isPresent());

        attendanceRepository.save(existingAttendance);
        return true;
    }

    @Override
    public void updateWorkedHours(Long id, boolean increase) {
        attendanceRepository.findById(id).ifPresent(attendance -> {
            int currentHours = attendance.getWorkedHours();
            if (increase) {
                attendance.setWorkedHours(currentHours + 1);
            } else if (currentHours > 0) {
                attendance.setWorkedHours(currentHours - 1);
            }
            attendanceRepository.save(attendance);
        });
    }
}
