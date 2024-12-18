package org.example.employees.services;

import org.example.employees.models.Attendance;

import java.util.List;
import java.util.Optional;

public interface AttendanceService {
    List<Attendance> getAllAttendances();
    Optional<Attendance> getAttendanceById(Long id);
    void save(Attendance attendance);
    void deleteAttendance(Long id);
    void increaseWorkedHours(Long id);
}
