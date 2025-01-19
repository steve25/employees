package org.example.employees.services;

import org.example.employees.models.Attendance;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceService {

    List<Attendance> getAllAttendances();

    List<Attendance> getAttendancesByDate(LocalDate date);

    Page<Attendance> getAttendancesSorted(String sortBy, String sortDirection, int page, int pageSize);

    Optional<Attendance> getAttendanceByIdWithEmployee(Long id);

    void deleteAttendance(Long id);

    void updateWorkedHours(Long id, boolean increase);

    boolean addAttendanceForEmployee(long employeeId, Attendance attendance);

    boolean updateAttendance(Long attendanceId, Attendance attendance, Long employeeId);
}