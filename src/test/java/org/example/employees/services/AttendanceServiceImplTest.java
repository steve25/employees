package org.example.employees.services;

import org.example.employees.models.Attendance;
import org.example.employees.models.Employee;
import org.example.employees.repositories.AttendanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceImplTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private AttendanceServiceImpl attendanceService;

    private Attendance testAttendance;
    private Employee testEmployee;

    @BeforeEach
    void setUp() {
        testEmployee = new Employee("John", "Doe", "Developer");
        testAttendance = new Attendance();
        testAttendance.setId(1L);
        testAttendance.setEmployee(testEmployee);
        testAttendance.setPresent(true);
        testAttendance.setWorkedHours(8);
        testAttendance.setDate(LocalDate.now());
    }

    @Test
    void getAttendancesSorted_ShouldReturnPage() {
        Page<Attendance> page = new PageImpl<>(List.of(testAttendance));
        when(attendanceRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Attendance> result = attendanceService.getAttendancesSorted("date", "asc", 0, 10);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        verify(attendanceRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void addAttendanceForEmployee_ShouldReturnTrue_WhenValid() {
        when(employeeService.getEmployeeById(anyLong())).thenReturn(Optional.of(testEmployee));
        when(attendanceRepository.findByEmployeeIdAndDate(anyLong(), any(LocalDate.class))).thenReturn(Optional.empty());

        boolean result = attendanceService.addAttendanceForEmployee(1L, testAttendance);

        assertTrue(result);
        verify(attendanceRepository, times(1)).save(any(Attendance.class));
    }

    @Test
    void getAttendanceById_ShouldReturnAttendance() {
        when(attendanceRepository.findById(1L)).thenReturn(Optional.of(testAttendance));

        Optional<Attendance> result = attendanceService.getAttendanceById(1L);

        assertTrue(result.isPresent());
        assertEquals(testAttendance.getId(), result.get().getId());
    }

    @Test
    void deleteAttendance_ShouldCallRepository() {
        when(attendanceRepository.existsById(1L)).thenReturn(true);

        attendanceService.deleteAttendance(1L);

        verify(attendanceRepository, times(1)).deleteById(1L);
    }

    @Test
    void updateWorkedHours_ShouldIncrease_WhenTrue() {
        when(attendanceRepository.findById(1L)).thenReturn(Optional.of(testAttendance));

        attendanceService.updateWorkedHours(1L, true);

        assertEquals(9, testAttendance.getWorkedHours());
        verify(attendanceRepository, times(1)).save(testAttendance);
    }

    @Test
    void updateWorkedHours_ShouldDecrease_WhenFalse() {
        when(attendanceRepository.findById(1L)).thenReturn(Optional.of(testAttendance));

        attendanceService.updateWorkedHours(1L, false);

        assertEquals(7, testAttendance.getWorkedHours());
        verify(attendanceRepository, times(1)).save(testAttendance);
    }
}