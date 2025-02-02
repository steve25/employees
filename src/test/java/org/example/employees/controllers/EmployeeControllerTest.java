package org.example.employees.controllers;

import org.example.employees.models.Employee;
import org.example.employees.services.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeServiceImpl employeeServiceImpl;

    @InjectMocks
    private EmployeeController employeeController;

    private Employee testEmployee;

    @BeforeEach
    void setUp() {
        testEmployee = new Employee();
        testEmployee.setId(1L);
        testEmployee.setFirstName("Bill");
        testEmployee.setLastName("Gates");
        testEmployee.setPosition("Developer");

        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    void getAllEmployees_ShouldReturnEmployeeList() throws Exception {
        Page<Employee> employeePage = new PageImpl<>(List.of(testEmployee));

        when(employeeServiceImpl.getAllEmployeesPageable(any(), any(), anyInt(), anyInt()))
                .thenReturn(employeePage);

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(view().name("layout"))
                .andExpect(model().attributeExists("employees"))
                .andExpect(model().attribute("employees", hasSize(1)));
    }

    @Test
    void getEmployeeById_ShouldReturnEmployeeDetail() throws Exception {
        when(employeeServiceImpl.getEmployeeById(1L)).thenReturn(Optional.of(testEmployee));

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("layout"))
                .andExpect(model().attributeExists("employee"))
                .andExpect(model().attribute("employee", testEmployee));
    }

    @Test
    void addEmployee_ShouldRedirectAfterAdding() throws Exception {
        mockMvc.perform(post("/employees/add-employee")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("position", "Developer"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees"));

        verify(employeeServiceImpl, times(1)).save(any(Employee.class));
    }

    @Test
    void showUpdateForm_ShouldReturnEditPage() throws Exception {
        when(employeeServiceImpl.getEmployeeById(1L)).thenReturn(Optional.of(testEmployee));

        mockMvc.perform(get("/employees/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("layout"))
                .andExpect(model().attributeExists("employee"))
                .andExpect(model().attribute("employee", testEmployee));
    }

    @Test
    void updateEmployee_ShouldRedirectAfterUpdating() throws Exception {
        when(employeeServiceImpl.getEmployeeById(1L)).thenReturn(Optional.of(testEmployee));

        mockMvc.perform(post("/employees/update")
                        .param("id", "1")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("position", "Senior Developer"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(employeeServiceImpl, times(1)).save(any(Employee.class));
    }

    @Test
    void deleteEmployee_ShouldRedirectAfterDeletion() throws Exception {
        mockMvc.perform(post("/employees/delete").param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees"));

        verify(employeeServiceImpl, times(1)).deleteById(1L);
    }
}