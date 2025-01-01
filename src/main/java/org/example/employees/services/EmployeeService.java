package org.example.employees.services;

import org.example.employees.models.Employee;
import org.example.employees.repositories.EmployeeRepository;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    List<Employee> getAllEmployees(String orderBy, String orderDirection);
    List<Employee> getAllEmployeesPageable(String orderBy, String orderDirection, int page, int size);
    Optional<Employee> getEmployeeById(Long id);
    Employee save(Employee employee);
    void deleteById(Long id);
}
