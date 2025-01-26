package org.example.employees.services;

import org.example.employees.models.Attendance;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface AttendanceService {
    Page<Attendance> getAttendancesSorted(String sortBy, String sortDirection, int page, int pageSize);

    Optional<Attendance> getAttendanceById(Long id);

    void deleteAttendance(Long id);

    void updateWorkedHours(Long id, boolean increase);

    boolean addAttendanceForEmployee(long employeeId, Attendance attendance);

    boolean updateAttendance(Attendance attendance);
}