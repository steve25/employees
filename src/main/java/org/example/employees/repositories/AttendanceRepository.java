package org.example.employees.repositories;

import org.example.employees.models.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    @Query("SELECT a FROM Attendance a JOIN FETCH a.employee WHERE a.id = :id")
    Optional<Attendance> findByIdWithEmployee(@Param("id") Long id);
    Optional<Attendance> findByEmployeeIdAndDate(Long employeeId, LocalDate date);

    Page<Attendance> findAll(Pageable pageable);

    List<Attendance> findByDate(LocalDate date);
}