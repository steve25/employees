package org.example.employees.repositories;

import org.example.employees.models.Attendance;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByEmployeeIdAndDate(Long employeeId, LocalDate date);

    List<Attendance> findAllByOrderByDateAsc();
    List<Attendance> findAllByOrderByDateDesc();
    List<Attendance> findAllByOrderByEmployeeLastNameAsc();
    List<Attendance> findAllByOrderByEmployeeLastNameDesc();
    List<Attendance> findAllByOrderByWorkedHoursAsc();
    List<Attendance> findAllByOrderByWorkedHoursDesc();
    List<Attendance> findAllByOrderByPresentAsc();
    List<Attendance> findAllByOrderByPresentDesc();
}