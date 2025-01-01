package org.example.employees.controllers;

import org.example.employees.models.Employee;
import org.example.employees.services.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final int PAGE_SIZE = 5;
    private final EmployeeServiceImpl employeeServiceImpl;

    @Autowired
    public EmployeeController(EmployeeServiceImpl employeeServiceImpl) {
        this.employeeServiceImpl = employeeServiceImpl;
    }

    @GetMapping
    public String getAllEmployees(
            @RequestParam(required = false, defaultValue = "lastName") String orderBy,
            @RequestParam(required = false, defaultValue = "asc") String orderDirection,
            @RequestParam(required = false, defaultValue = "0") int page,
            Model model
    ) {
        List<Employee> employees = employeeServiceImpl.getAllEmployeesPageable(orderBy, orderDirection, page, PAGE_SIZE);

        orderDirection = orderDirection.equalsIgnoreCase("asc") ? "desc" : "asc";

        model.addAttribute("contentFragment", "employees");
        model.addAttribute("employees", employees);
        model.addAttribute("orderBy", orderBy);
        model.addAttribute("orderDirection", orderDirection);
        model.addAttribute("page", page);
        model.addAttribute("pageSize", PAGE_SIZE);

        return "layout";
    }

    @GetMapping("/{id}")
    public String getEmployeeById(@PathVariable Long id, Model model) {
        Optional<Employee> employee = employeeServiceImpl.getEmployeeById(id);

        if (employee.isEmpty()) {
            return "redirect:/";
        }

        model.addAttribute("contentFragment", "employee-detail");
        model.addAttribute("employee", employee.get());

        return "layout";
    }

    @PostMapping("/add-employee")
    public String addEmployee(@ModelAttribute Employee employee) {
        employeeServiceImpl.save(employee);

        return "redirect:/employees";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Optional<Employee> employee = employeeServiceImpl.getEmployeeById(id);
        if (employee.isEmpty()) {
            return "redirect:/";
        }

        model.addAttribute("employee", employee.get());
        model.addAttribute("contentFragment", "employee-edit");

        return "layout";
    }

    @PostMapping("/update")
    public String updateAttendance(@ModelAttribute Employee employee) {
        Optional<Employee> employeeFromDb = employeeServiceImpl.getEmployeeById(employee.getId());

        if (employeeFromDb.isEmpty()) {
            return "redirect:/employees";
        }

        employeeFromDb.get().setFirstName(employee.getFirstName());
        employeeFromDb.get().setLastName(employee.getLastName());
        employeeFromDb.get().setPosition(employee.getPosition());

        employeeServiceImpl.save(employeeFromDb.get());

        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteAttendance(@RequestParam Long id) {
        employeeServiceImpl.deleteById(id);
        return "redirect:/employees";
    }
}