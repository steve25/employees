package org.example.employees.repositories;

import org.example.employees.models.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EmployeeRepositoryTest {


    private final EmployeeRepository employeeRepository;

    @Autowired
    EmployeeRepositoryTest(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Test
    void findByNameLikeTest() {
        // Page<Employee> findByNameLike(@Param("searchQuery") String searchQuery, Pageable pageable);

        // Arrange
        Sort sort = Sort.by(Sort.Direction.ASC, "lastName");
        Pageable pageable = PageRequest.of(0, 4, sort);

        // Act
        List<Employee> employeesByNameLike = employeeRepository.findByNameLike("Roman", pageable).getContent();

        // Assert
        assertEquals(1, employeesByNameLike.size());
        assertEquals("Roman", employeesByNameLike.get(0).getFirstName());
    }

    @Test
    void findByIdTest() {
        // Page<Employee> findByNameLike(@Param("searchQuery") String searchQuery, Pageable pageable);

        // Act
        Optional<Employee> employeesById = employeeRepository.findById(1L);

        // Assert
        assertEquals("Roman", employeesById.get().getFirstName());
    }
}