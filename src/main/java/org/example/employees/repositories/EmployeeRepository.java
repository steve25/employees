package org.example.employees.repositories;

import org.example.employees.models.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT e FROM Employee e WHERE e.firstName LIKE %:searchQuery% OR e.lastName LIKE %:searchQuery%")
    Page<Employee> findByNameLike(@Param("searchQuery") String searchQuery, Pageable pageable);
}
