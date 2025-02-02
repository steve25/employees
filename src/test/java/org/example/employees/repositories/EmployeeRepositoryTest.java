package org.example.employees.repositories;

import org.example.employees.models.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {

        employeeRepository.deleteAll();

        employeeRepository.save(new Employee("John", "Down", "Boss"));
        employeeRepository.save(new Employee("Stefan", "Stefan", "Developer"));
        employeeRepository.save(new Employee("Anna", "Kovac", "Designer"));
        employeeRepository.save(new Employee("Peter", "Horvath", "Manager"));

    }

    @Test
    void findByNameLike_ShouldReturnMatchingEmployees() {
        //Act
        Page<Employee> result = employeeRepository.findByNameLike("a", PageRequest.of(0, 10));

        //Assert
        assertThat(result).isNotEmpty();
        assertThat(result.getTotalElements()).isEqualTo(3);
    }

    @Test
    void findByNameLike_ShouldReturnEmpty_WhenNoMatch() {
        //Act
        Page<Employee> result = employeeRepository.findByNameLike("xyz123", PageRequest.of(0, 10));

        //Assert
        assertThat(result.getContent()).isEmpty();
    }
}