package com.gallelloit.employees.service;

import com.gallelloit.employees.dto.DepartmentDTO;
import com.gallelloit.employees.entity.Department;
import com.gallelloit.employees.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository repository;

    @InjectMocks
    private DepartmentService departmentService;

    private Department testDepartment;

    @BeforeEach
    void setUp() {
        testDepartment = Department.builder()
                .id(1L)
                .name("Engineering")
                .build();
    }

    @Test
    void findAll_shouldReturnAllDepartments() {
        List<Department> departments = List.of(testDepartment);
        when(repository.findAll()).thenReturn(departments);

        List<DepartmentDTO> result = departmentService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        DepartmentDTO departmentDTO = result.get(0);
        assertEquals(testDepartment.getId(), departmentDTO.id());
        assertEquals(testDepartment.getName(), departmentDTO.name());

        verify(repository, times(1)).findAll();
    }

    @Test
    void findAll_shouldReturnEmptyListWhenNoDepartments() {
        when(repository.findAll()).thenReturn(List.of());

        List<DepartmentDTO> result = departmentService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    void save_shouldSaveDepartment() {
        Department departmentToSave = Department.builder()
                .name("Marketing")
                .build();

        Department savedDepartment = Department.builder()
                .id(2L)
                .name("Marketing")
                .build();

        when(repository.save(any(Department.class))).thenReturn(savedDepartment);

        Department result = departmentService.save(departmentToSave);

        assertNotNull(result);
        assertEquals(savedDepartment.getId(), result.getId());
        assertEquals(savedDepartment.getName(), result.getName());

        verify(repository, times(1)).save(departmentToSave);
    }

    @Test
    void save_shouldReturnSavedDepartment() {
        when(repository.save(any(Department.class))).thenReturn(testDepartment);

        Department result = departmentService.save(testDepartment);

        assertNotNull(result);
        assertEquals(testDepartment.getId(), result.getId());
        assertEquals(testDepartment.getName(), result.getName());

        verify(repository, times(1)).save(testDepartment);
    }
}
