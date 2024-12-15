package org.example.employees.services;

import org.example.employees.models.Attendance;

import java.util.List;

public interface AttendanceService {
    List<Attendance> getAllAttendances();
    Attendance getAttendanceById(Long id);
    void save(Attendance attendance);
}
