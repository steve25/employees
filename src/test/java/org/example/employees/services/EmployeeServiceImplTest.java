package org.example.employees.services;

import org.example.employees.models.Employee;
import org.example.employees.repositories.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeServiceImplTest {

    EmployeeServiceImpl employeeService;

    @Mock
    EmployeeRepository employeeRepository;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        employeeService = new EmployeeServiceImpl(employeeRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getAllEmployeesTest() {

//        Sort.Direction sort = orderDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
//
//        return employeeRepository.findAll(Sort.by(sort, orderBy));

        // Arrange
//        List<Employee> bird = new Bird("Parrot", true, 50);
//        when(birdRepository.findById(1L)).thenReturn(Optional.of(bird));

        // Act
        List<Employee> result = employeeService.getAllEmployees("asc", "lastName");

        // Assert
        assertNotNull(result);
//        assertEquals("Parrot", result.getName());
//        verify(birdRepository, times(1)).findById(1L);
    }

    @Test
    void getAllEmployeesPageable() {
    }

    @Test
    void getEmployeeById() {
    }

    @Test
    void save() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void findByNameLike() {
    }
}