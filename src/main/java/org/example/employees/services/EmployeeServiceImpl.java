package org.example.employees.services;

import org.example.employees.models.Employee;
import org.example.employees.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees(String orderBy, String orderDirection) {
        Sort.Direction sort = orderDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        return employeeRepository.findAll(Sort.by(sort, orderBy));
    }

    public Page<Employee> getAllEmployeesPageable(String orderBy, String orderDirection, int page, int size) {
        Sort.Direction sortDirection = orderDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(sortDirection, orderBy);

        Pageable pageable = PageRequest.of(page, size, sort);

        return employeeRepository.findAll(pageable);
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public Page<Employee> findByNameLike(String searchQuery, String orderBy, String orderDirection, int page, int size) {
        Sort.Direction sortDirection = orderDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(sortDirection, orderBy);

        Pageable pageable = PageRequest.of(page, size, sort);

        return employeeRepository.findByNameLike(searchQuery, pageable);
    }
}
