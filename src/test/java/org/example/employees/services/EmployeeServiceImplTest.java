package org.example.employees.services;

import org.example.employees.models.Employee;
import org.example.employees.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee testEmployee;

    @BeforeEach
    void setUp() {
        testEmployee = new Employee("Jane", "Doe", "Manager");
        testEmployee.setId(1L);
    }

    @Test
    void getAllEmployees_ShouldReturnList() {
        when(employeeRepository.findAll(any(Sort.class))).thenReturn(List.of(testEmployee));

        List<Employee> employees = employeeService.getAllEmployees("lastName", "asc");

        assertFalse(employees.isEmpty());
        assertEquals(1, employees.size());
        verify(employeeRepository, times(1)).findAll(any(Sort.class));
    }

    @Test
    void getAllEmployeesPageable_ShouldReturnPage() {
        Page<Employee> page = new PageImpl<>(List.of(testEmployee));
        when(employeeRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Employee> result = employeeService.getAllEmployeesPageable("lastName", "asc", 0, 5);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        verify(employeeRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getEmployeeById_ShouldReturnEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));

        Optional<Employee> result = employeeService.getEmployeeById(1L);

        assertTrue(result.isPresent());
        assertEquals(testEmployee.getId(), result.get().getId());
    }

    @Test
    void save_ShouldReturnSavedEmployee() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

        Employee result = employeeService.save(testEmployee);

        assertNotNull(result);
        assertEquals(testEmployee.getId(), result.getId());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void deleteById_ShouldCallRepository() {
        doNothing().when(employeeRepository).deleteById(1L);

        employeeService.deleteById(1L);

        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void findByNameLike_ShouldReturnPage() {
        Page<Employee> page = new PageImpl<>(List.of(testEmployee));
        when(employeeRepository.findByNameLike(anyString(), any(Pageable.class))).thenReturn(page);

        Page<Employee> result = employeeService.findByNameLike("Jane", "lastName", "asc", 0, 5);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        verify(employeeRepository, times(1)).findByNameLike(anyString(), any(Pageable.class));
    }
}