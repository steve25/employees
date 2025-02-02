package org.example.employees.controllers;

import org.example.employees.models.Attendance;
import org.example.employees.models.Employee;
import org.example.employees.services.AttendanceService;
import org.example.employees.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AttendanceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AttendanceService attendanceService;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private AttendanceController attendanceController;

    private Attendance testAttendance;


    @BeforeEach
    void setUp() {

        testAttendance = new Attendance();
        testAttendance.setId(1L);
        testAttendance.setEmployee(new Employee("Bill", "Gates", "Developer"));
        testAttendance.setPresent(true);
        testAttendance.setWorkedHours(8);
        testAttendance.setDate(LocalDate.now());

        mockMvc = MockMvcBuilders.standaloneSetup(attendanceController).build();
    }

    @Test
    void getAllAttendances_ShouldReturnAttendanceList() throws Exception {

        Page<Attendance> mockPage = new PageImpl<>(List.of(testAttendance), PageRequest.of(0, 10), 1);

        when(attendanceService.getAttendancesSorted(anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(mockPage);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("layout"))
                .andExpect(model().attributeExists("attendances"))
                .andExpect(model().attribute("attendances", hasSize(1)));

    }

    @Test
    void showAttendance_ShouldReturnAttendanceDetailPage() throws Exception {
        Long attendanceId = 1L;
        when(attendanceService.getAttendanceById(attendanceId))
                .thenReturn(Optional.of(testAttendance));
        when(employeeService.getAllEmployees(anyString(), anyString()))
                .thenReturn(List.of(testAttendance.getEmployee()));

        mockMvc.perform(get("/attendance/details")
                        .param("id", attendanceId.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("layout"))
                .andExpect(model().attributeExists("attendance"))
                .andExpect(model().attributeExists("employees"))
                .andExpect(model().attribute("attendance", testAttendance));
    }

    @Test
    void showUpdateForm_ShouldReturnEditPage() throws Exception {
        Long attendanceId = 1L;
        when(attendanceService.getAttendanceById(attendanceId))
                .thenReturn(Optional.of(testAttendance));
        when(employeeService.getAllEmployees(anyString(), anyString()))
                .thenReturn(List.of(testAttendance.getEmployee()));

        mockMvc.perform(get("/attendance/edit")
                        .param("id", attendanceId.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("layout"))
                .andExpect(model().attributeExists("attendance"))
                .andExpect(model().attributeExists("employees"))
                .andExpect(model().attribute("attendance", testAttendance));
    }

    @Test
    void addAttendance_ShouldRedirectOnSuccess() throws Exception {
        Long employeeId = 1L;
        when(attendanceService.addAttendanceForEmployee(anyLong(), any())).thenReturn(true);

        mockMvc.perform(post("/addAttendance")
                        .param("employeeId", employeeId.toString())
                        .flashAttr("attendance", testAttendance))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void addAttendance_ShouldRedirectToErrorOnFailure() throws Exception {
        Long employeeId = 1L;
        when(attendanceService.addAttendanceForEmployee(anyLong(), any())).thenReturn(false);
        when(employeeService.getAllEmployees(anyString(), anyString())).thenReturn(List.of(testAttendance.getEmployee()));

        mockMvc.perform(post("/addAttendance")
                .param("employeeId", employeeId.toString())
                .flashAttr("attendance", testAttendance))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }

    @Test
    void deleteAttendance_ShouldRedirectAfterDeletion() throws Exception {
        Long attendanceId = 1L;

        mockMvc.perform(post("/attendance/delete")
                        .param("id", attendanceId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void updateAttendance_ShouldRedirectOnSuccess() throws Exception {
        when(attendanceService.updateAttendance(any())).thenReturn(true);

        mockMvc.perform(post("/attendance/update")
                        .flashAttr("attendance", testAttendance))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void updateAttendance_ShouldRedirectToErrorOnFailure() throws Exception {
        when(attendanceService.updateAttendance(any())).thenReturn(false);

        mockMvc.perform(post("/attendance/update")
                        .flashAttr("attendance", testAttendance))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }

    @Test
    void increaseWorkedHours_ShouldRedirectAfterUpdate() throws Exception {
        Long attendanceId = 1L;

        mockMvc.perform(get("/attendance/increaseHours")
                        .param("id", attendanceId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void decreaseWorkedHours_ShouldRedirectAfterUpdate() throws Exception {
        Long attendanceId = 1L;

        mockMvc.perform(get("/attendance/decreaseHours")
                        .param("id", attendanceId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

}