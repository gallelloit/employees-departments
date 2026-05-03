package com.gallelloit.employees.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gallelloit.employees.configuration.TestSecurityConfig;
import com.gallelloit.employees.dto.EmployeeCreateRequest;
import com.gallelloit.employees.dto.EmployeeDTO;
import com.gallelloit.employees.dto.EmployeeUpdateRequest;
import com.gallelloit.employees.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@Import(TestSecurityConfig.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findAll_shouldReturnAllEmployees() throws Exception {
        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .departmentId(1L)
                .departmentName("Engineering")
                .build();

        when(employeeService.findAll()).thenReturn(List.of(employeeDTO));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"));
    }

    @Test
    void save_shouldCreateEmployee() throws Exception {
        EmployeeCreateRequest request = new EmployeeCreateRequest(
                "Jane Smith",
                "jane.smith@example.com",
                1L
        );

        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .id(1L)
                .name("Jane Smith")
                .email("jane.smith@example.com")
                .departmentId(1L)
                .departmentName("Engineering")
                .build();

        when(employeeService.create(any(EmployeeCreateRequest.class))).thenReturn(employeeDTO);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Jane Smith"))
                .andExpect(jsonPath("$.email").value("jane.smith@example.com"));
    }

    @Test
    void updateEmployee_shouldUpdateEmployee() throws Exception {
        EmployeeUpdateRequest updateRequest = new EmployeeUpdateRequest(
                "John Updated",
                "john.updated@example.com"
        );

        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .id(1L)
                .name("John Updated")
                .email("john.updated@example.com")
                .departmentId(1L)
                .departmentName("Engineering")
                .build();

        when(employeeService.updateEmployee(anyLong(), any(EmployeeUpdateRequest.class))).thenReturn(employeeDTO);

        mockMvc.perform(put("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Updated"))
                .andExpect(jsonPath("$.email").value("john.updated@example.com"));
    }

    @Test
    void deleteEmployee_shouldDeleteEmployee() throws Exception {
        mockMvc.perform(delete("/employees/1"))
                .andExpect(status().isOk());
    }
}
