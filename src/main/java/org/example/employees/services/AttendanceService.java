package org.example.employees.services;

import org.example.employees.models.Attendance;

import java.util.List;
import java.util.Optional;

public interface AttendanceService {
    List<Attendance> getAllAttendances();

    Optional<Attendance> getAttendanceById(Long id);

    void deleteAttendance(Long id);

    void updateWorkedHours(Long id, boolean increase);

    boolean addAttendanceForEmployee(long employeeId, Attendance attendance);

    boolean updateAttendance(Long attendanceId, Attendance attendance, Long employeeId);
}
