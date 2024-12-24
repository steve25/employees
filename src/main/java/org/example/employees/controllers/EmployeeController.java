package org.example.employees.controllers;

import org.example.employees.models.Employee;
import org.example.employees.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getAllEmployees(Model model) {
        List<Employee> employees = employeeService.getAllEmployees();

        model.addAttribute("pageTitle", "Home");
        model.addAttribute("contentFragment", "employees");
        model.addAttribute("employees", employees);

        return "layout";
    }

    @GetMapping("/{id}")
    public String getEmployeeById(@PathVariable Long id, Model model) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);

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
        employeeService.save(employee);

        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        if (employee.isEmpty()) {
            return "redirect:/";
        }

        model.addAttribute("employee", employee.get());
        model.addAttribute("contentFragment", "employee-edit");

        return "layout";
    }

    @PostMapping("/update")
    public String updateAttendance(@ModelAttribute Employee employee) {
        Optional<Employee> employeeFromDb = employeeService.getEmployeeById(employee.getId());

        if (employeeFromDb.isEmpty()) {
            return "redirect:/";
        }

        employeeFromDb.get().setFirstName(employee.getFirstName());
        employeeFromDb.get().setLastName(employee.getLastName());
        employeeFromDb.get().setPosition(employee.getPosition());

        employeeService.save(employeeFromDb.get());

        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteAttendance(@RequestParam Long id) {
        employeeService.deleteById(id);
        return "redirect:/employees";
    }
}