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

    private final EmployeeServiceImpl employeeServiceImpl;

    @Autowired
    public EmployeeController(EmployeeServiceImpl employeeServiceImpl) {
        this.employeeServiceImpl = employeeServiceImpl;
    }

    @GetMapping
    public String getAllEmployees(
            @RequestParam(value = "orderBy", required = false, defaultValue = "lastName") String orderBy,
            @RequestParam(value = "orderDirection", required = false, defaultValue = "asc") String orderDirection,
            Model model
    ) {
        List<Employee> employees = employeeServiceImpl.getAllEmployees(orderBy, orderDirection);

        orderDirection = orderDirection.equalsIgnoreCase("asc") ? "desc" : "asc";

        model.addAttribute("pageTitle", "Home");
        model.addAttribute("contentFragment", "employees");
        model.addAttribute("employees", employees);
        model.addAttribute("orderBy", orderBy);
        model.addAttribute("orderDirection", orderDirection);

        return "layout";
    }

    @GetMapping("/{id}")
    public String getEmployeeById(@PathVariable Long id, Model model) {
        Optional<Employee> employee = employeeServiceImpl.getEmployeeById(id);

        if (employee.isEmpty()) {
            return "redirect:/";
        }

        model.addAttribute("pageTitle", "Details");
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