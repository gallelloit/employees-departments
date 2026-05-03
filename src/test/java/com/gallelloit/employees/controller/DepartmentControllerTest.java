package com.gallelloit.employees.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gallelloit.employees.configuration.TestSecurityConfig;
import com.gallelloit.employees.dto.DepartmentDTO;
import com.gallelloit.employees.entity.Department;
import com.gallelloit.employees.service.DepartmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DepartmentController.class)
@Import(TestSecurityConfig.class)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DepartmentService departmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findAll_shouldReturnAllDepartments() throws Exception {
        DepartmentDTO departmentDTO = DepartmentDTO.builder()
                .id(1L)
                .name("Engineering")
                .build();

        when(departmentService.findAll()).thenReturn(List.of(departmentDTO));

        mockMvc.perform(get("/departments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Engineering"));
    }

    @Test
    void save_shouldCreateDepartment() throws Exception {
        Department department = Department.builder()
                .id(1L)
                .name("Engineering")
                .build();

        when(departmentService.save(any(Department.class))).thenReturn(department);

        mockMvc.perform(post("/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(department)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Engineering"));
    }
}
